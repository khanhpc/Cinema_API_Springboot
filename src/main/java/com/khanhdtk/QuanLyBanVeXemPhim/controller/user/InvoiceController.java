package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Booking;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ComboBongNuoc;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.Invoice;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.InvoiceRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class InvoiceController {
    private final BookingService bookingService;
    private final InvoiceRepository invoiceRepository;

    @GetMapping("public/combobongnuoc")
    public List<ComboBongNuoc> getAllCombos() {
        return bookingService.getAllCombos();
    }

    @GetMapping("/user/invoices/{invoidId}")
    public ResponseEntity<?> getInvoice(@PathVariable("invoidId") Long invoiceId) {
        Invoice invoice = bookingService.getInvoice(invoiceId);
        return ResponseEntity.ok(invoice.getTotalPrice());
    }

    @PostMapping("/user/invoices/{id}/confirm-payment")
    @Transactional
    @CacheEvict(value = "top_movies", allEntries = true)
    public ResponseEntity<?> confirmPayment(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hóa đơn lỏ rồi bác ơi"));

        invoice.setStatus("CONFIRMED");

        for (Booking b : invoice.getBookings()) {
            b.setStatus("CONFIRMED");
        }

        invoiceRepository.save(invoice);
        return ResponseEntity.ok("Thanh toán thành công!");
    }
}
