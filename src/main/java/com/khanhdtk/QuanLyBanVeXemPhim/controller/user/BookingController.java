package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.ComboRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.InvoiceHistoryDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.BookingRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.BookingResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ComboBongNuoc;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        List<BookingResponse> bookings = bookingService.createBooking(request);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/history")
    public ResponseEntity<List<InvoiceHistoryDTO>> getHistory() {
        List<InvoiceHistoryDTO> historyBooking = bookingService.getHistory();
        return ResponseEntity.ok(historyBooking);
    }



    @PostMapping("/invoices/{invoiceId}/combos")
    public ResponseEntity<?> updateInvoiceCombos(@PathVariable Long invoiceId, @RequestBody List<ComboRequest> comboRequests) {
        bookingService.updateCombos(invoiceId, comboRequests);
        return ResponseEntity.ok("Cập nhật bắp nước thành công!");
    }
}
