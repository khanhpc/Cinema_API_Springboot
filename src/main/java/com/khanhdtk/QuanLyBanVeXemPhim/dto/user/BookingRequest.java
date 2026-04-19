package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookingRequest {
    private Long showtimeId;
    private List<Long> seatId;
    private Long userId;
}
