package com.khanhdtk.QuanLyBanVeXemPhim.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverviewStatsResponse {
    private double totalRevenue;// Doanh thu theo kỳ lọc
    private int totalTickets;
    private int totalCombos;
    private double yearlyRevenue;
}