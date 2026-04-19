package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SeatStatusDTO {
    private Long id;
    private String seatRow;
    private int seatNumber;
    private String type;
    private Long surcharge;
    private boolean isBooked;
}
