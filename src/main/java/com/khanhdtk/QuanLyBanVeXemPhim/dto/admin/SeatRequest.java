package com.khanhdtk.QuanLyBanVeXemPhim.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatRequest {
    private Long roomId;
    private int rows;
    private int perRow;
}
