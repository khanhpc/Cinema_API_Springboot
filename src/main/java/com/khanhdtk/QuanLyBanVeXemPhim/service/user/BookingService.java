package com.khanhdtk.QuanLyBanVeXemPhim.service.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.ComboRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.InvoiceHistoryDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.BookingRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.BookingResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.*;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.Invoice;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.InvoiceCombo;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.BadRequestException;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final ComboBongNuocRepository comboBongNuocRepository;
    private final InvoiceComboRepository invoiceComboRepository;

    @Transactional
    public List<BookingResponse> createBooking(BookingRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        ShowTime st = showTimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy suất chiếu"));

        List<Seat> seats = seatRepository.findAllById(request.getSeatId());

        long totalInvoicePrice = seats.stream()
                .mapToLong(s -> st.getPrice() + s.getSurcharge())
                .sum();

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setTotalPrice(totalInvoicePrice);
        invoice.setStatus("PENDING");
        invoice.setCreatedAt(LocalDateTime.now());
        invoice = invoiceRepository.save(invoice);
        invoice.setTicketCode("DTK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());


        List<Booking> bookings = new ArrayList<>();
        for (Seat s : seats) {
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setSeat(s);
            booking.setShowTime(st);
            booking.setInvoice(invoice);
            booking.setPrice(st.getPrice() + s.getSurcharge());
            booking.setStatus("PENDING");
            booking.setCreatedAt(LocalDateTime.now());

            bookings.add(booking);
        }

        try {
            bookingRepository.saveAll(bookings);

            // 4. Trả về kết quả (Bác có thể thêm invoiceId vào Response nếu cần)
            return bookings.stream().map(b -> {
                BookingResponse res = new BookingResponse();
                res.setMovieTitle(b.getShowTime().getMovie().getTitle());
                res.setRoomName(b.getShowTime().getRoom().getName());
                res.setSeat(b.getSeat().getSeatRow() + b.getSeat().getSeatNumber());
                res.setStartTime(b.getShowTime().getStartTime());
                res.setTotalPrice(b.getPrice());
                res.setStatus(b.getStatus());
                // res.setInvoiceId(b.getInvoice().getId()); // Nếu bác muốn React biết ID hóa đơn
                return res;
            }).toList();
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Một hoặc nhiều ghế đã bị đặt nhanh hơn bác rồi!");
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupExpiredBookings() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(10);

        int canceledCount = bookingRepository.cancelExpiredBookings(fiveMinutesAgo);
        int canceledCount1 = invoiceRepository.cancelExpiredInvoice(fiveMinutesAgo);

        if (canceledCount > 0) {
            System.out.println("Đã quét và tự động HỦY {} vé PENDING quá 5 phút!" + canceledCount);
        }
    }

    public List<InvoiceHistoryDTO> getHistory() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản này"));

        List<Invoice> myInvoices = invoiceRepository.findByUserAndStatusNotOrderByCreatedAtDesc(user, "CANCELLED");

        return myInvoices.stream().map(invoice -> {
            Booking firstBooking = invoice.getBookings().get(0);

            List<String> seatNames = invoice.getBookings().stream()
                    .map(b -> b.getSeat().getSeatRow() + b.getSeat().getSeatNumber())
                    .collect(Collectors.toList());

            List<String> comboNames = invoice.getInvoiceCombos().stream()
                    .map(ic -> ic.getComboBongNuoc().getName() + " (x" + ic.getQuantity() + ")")
                    .collect(Collectors.toList());

            return new InvoiceHistoryDTO(
                    invoice.getId(),
                    firstBooking.getShowTime().getMovie().getTitle(),
                    firstBooking.getShowTime().getMovie().getPosterUrl(),
                    firstBooking.getShowTime().getRoom().getName(),
                    firstBooking.getShowTime().getRoom().getCinema().getName(),
                    seatNames,
                    firstBooking.getShowTime().getStartTime(),
                    invoice.getTotalPrice(),
                    invoice.getStatus(),
                    invoice.getCreatedAt(),
                    comboNames,
                    invoice.getTicketCode()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateCombos(Long invoiceId, List<ComboRequest> comboRequests) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Hóa đơn không tồn tại"));

        if (!"PENDING".equals(invoice.getStatus())) {
            throw new BadRequestException("Hóa đơn đã xử lý, không thể thêm bắp nước!");
        }

        invoiceComboRepository.deleteByInvoice(invoice);
        invoice.getInvoiceCombos().clear();

        long newComboTotal = 0;

        for (ComboRequest req : comboRequests) {
            if (req.getQuantity() <= 0) continue;

            ComboBongNuoc combo = comboBongNuocRepository.findById(req.getComboId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy combo"));

            // Tạo bản ghi chi tiết bắp nước mới
            InvoiceCombo detail = new InvoiceCombo();
            detail.setInvoice(invoice);
            detail.setComboBongNuoc(combo);
            detail.setQuantity(req.getQuantity());
            detail.setPriceAtBooking(combo.getPrice());

            invoice.getInvoiceCombos().add(detail);

            newComboTotal += (combo.getPrice() * req.getQuantity());
        }

        long ticketTotal = invoice.getBookings().stream()
                .mapToLong(Booking::getPrice)
                .sum();

        invoice.setTotalPrice(ticketTotal + newComboTotal);

        invoiceRepository.save(invoice);
    }

    public List<ComboBongNuoc> getAllCombos() {
        return comboBongNuocRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }
}
