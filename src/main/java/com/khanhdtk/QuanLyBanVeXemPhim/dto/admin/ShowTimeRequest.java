package com.khanhdtk.QuanLyBanVeXemPhim.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShowTimeRequest {
    private Long movieId;
    private Long roomId;
    private String startTime;
    private Long price;
}
