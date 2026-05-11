package com.khanhdtk.QuanLyBanVeXemPhim.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyRevenueResponse {
    private String name;
    private double revenue;
}