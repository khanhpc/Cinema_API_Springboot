package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class InvoiceHistoryDTO {
    private Long invoiceId;
    private String movieTitle;
    private String posterUrl;
    private String roomName;
    private List<String> seatNames; // Trả về danh sách ghế: ["J1", "J2", "J3"]
    private LocalDateTime showTime;
    private Long totalPrice; // Tổng tiền cả vé và combo
    private String status;
    private LocalDateTime createdAt; // Dùng để tính đếm ngược
    private List<String> comboNames; // Danh sách combo đã chọn (nếu có)
    private String ticketCode;
}
