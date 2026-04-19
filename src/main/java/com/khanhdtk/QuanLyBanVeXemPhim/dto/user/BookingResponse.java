package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private String ticketCode;
    private String movieTitle;
    private String roomName;
    private String seat;
    private LocalDateTime startTime;
    private double totalPrice;
    private String status;
}
