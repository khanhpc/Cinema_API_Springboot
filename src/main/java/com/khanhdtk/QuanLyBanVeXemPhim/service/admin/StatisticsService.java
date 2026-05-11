package com.khanhdtk.QuanLyBanVeXemPhim.service.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.DailyDetailResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.DailyRevenueResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.OverviewStatsResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.TopMovieResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Booking;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.Invoice;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.BookingRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public OverviewStatsResponse getOverview(String startStr, String endStr) {
        LocalDateTime start = LocalDate.parse(startStr).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endStr).atTime(23, 59, 59);

        double ticketRev = bookingRepository.sumTicketRevenue(start, end);
        int totalTickets = bookingRepository.countTickets(start, end);

        double comboRev = invoiceRepository.sumComboRevenue(start, end);
        Long combosObj = invoiceRepository.sumTotalCombos(start, end);
        int totalCombos = combosObj != null ? combosObj.intValue() : 0;

        double totalRev = ticketRev + comboRev;

        LocalDateTime startOfYear = LocalDate.now().withDayOfYear(1).atStartOfDay();
        double yearlyTicketRev = bookingRepository.sumTicketRevenue(startOfYear, LocalDateTime.now());
        double yearlyComboRev = invoiceRepository.sumComboRevenue(startOfYear, LocalDateTime.now());
        double yearlyRev = yearlyTicketRev + yearlyComboRev;

        return new OverviewStatsResponse(totalRev, totalTickets, totalCombos, yearlyRev);
    }

    public List<DailyRevenueResponse> getChartData(String startStr, String endStr) {
        LocalDate startDate = LocalDate.parse(startStr);
        LocalDate endDate = LocalDate.parse(endStr);
        List<DailyRevenueResponse> data = new ArrayList<>();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if(daysBetween > 31) daysBetween = 31;

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate d = startDate.plusDays(i);
            double dailyTicket = bookingRepository.sumTicketRevenue(d.atStartOfDay(), d.atTime(23, 59, 59));
            double dailyCombo = invoiceRepository.sumComboRevenue(d.atStartOfDay(), d.atTime(23, 59, 59));
            data.add(new DailyRevenueResponse("Ngày " + d.getDayOfMonth(), dailyTicket + dailyCombo));
        }
        return data;
    }

    public List<TopMovieResponse> getTopMovies(String startStr, String endStr) {
        LocalDateTime start = LocalDate.parse(startStr).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endStr).atTime(23, 59, 59);

        return bookingRepository.getTopMovies(start, end, PageRequest.of(0, 3));
    }

    @Transactional
    public List<DailyDetailResponse> getDailyDetail(int day) {
        LocalDate targetDate = LocalDate.now();
        for(int i = 0; i <= 31; i++) {
            LocalDate d = LocalDate.now().minusDays(i);
            if(d.getDayOfMonth() == day) {
                targetDate = d;
                break;
            }
        }

        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.atTime(23, 59, 59);

        List<DailyDetailResponse> details = bookingRepository.getDailyDetails(start, end);
        List<Object[]> comboStats = invoiceRepository.getInvoiceComboDetailsWithNames(start, end);

        if (comboStats != null && !comboStats.isEmpty() && details != null) {
            Map<Long, Map<String, Long>> invoiceComboMap = new HashMap<>();
            for (Object[] c : comboStats) {
                Long invId = (Long) c[0];
                String comboName = (String) c[1];
                long qty = ((Number) c[2]).longValue();
                invoiceComboMap.computeIfAbsent(invId, k -> new HashMap<>()).merge(comboName, qty, Long::sum);
            }

            List<Invoice> invoices = invoiceRepository.findAllById(invoiceComboMap.keySet());
            Map<String, Map<String, Long>> roomCombos = new HashMap<>();
            Map<String, Long> roomComboRevenue = new HashMap<>();

            for (Invoice inv : invoices) {
                if (inv.getBookings() != null && !inv.getBookings().isEmpty()) {
                    Booking firstBooking = inv.getBookings().get(0);
                    String cinema = firstBooking.getShowTime().getRoom().getCinema().getName();
                    String movie = firstBooking.getShowTime().getMovie().getTitle();
                    String room = firstBooking.getShowTime().getRoom().getName();

                    String roomKey = cinema + "-" + movie + "-" + room;
                    Map<String, Long> currentRoomCombos = roomCombos.computeIfAbsent(roomKey, k -> new HashMap<>());
                    Map<String, Long> combosOfThisInvoice = invoiceComboMap.get(inv.getId());

                    if (combosOfThisInvoice != null) {
                        for (Map.Entry<String, Long> entry : combosOfThisInvoice.entrySet()) {
                            currentRoomCombos.merge(entry.getKey(), entry.getValue(), Long::sum);
                        }
                    }

                    long comboMoney = 0L;
                    if (inv.getInvoiceCombos() != null) {
                        for (var ic : inv.getInvoiceCombos()) {
                            comboMoney += (ic.getPriceAtBooking() * ic.getQuantity());
                        }
                    }
                    roomComboRevenue.merge(roomKey, comboMoney, Long::sum);
                }
            }

            for (DailyDetailResponse d : details) {
                String roomKey = d.getCinema() + "-" + d.getMovie() + "-" + d.getRoom();
                Map<String, Long> combos = roomCombos.get(roomKey);

                if (combos == null || combos.isEmpty()) {
                    d.setCombos("0");
                } else {
                    List<String> parts = new ArrayList<>();
                    for (Map.Entry<String, Long> c : combos.entrySet()) {
                        parts.add(c.getValue() + " " + c.getKey());
                    }
                    d.setCombos(String.join(", ", parts));
                }

                Long extraMoney = roomComboRevenue.get(roomKey);
                if (extraMoney != null && extraMoney > 0) {
                    d.setRevenue(d.getRevenue() + extraMoney);
                }
            }
        } else if (details != null) {
            for (DailyDetailResponse d : details) {
                d.setCombos("0");
            }
        }

        return details != null ? details : new ArrayList<>();
    }
}