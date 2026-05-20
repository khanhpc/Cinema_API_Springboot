package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.DailyDetailResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.DailyRevenueResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.OverviewStatsResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.TopMovieResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    public ResponseEntity<OverviewStatsResponse> getOverview(
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        OverviewStatsResponse stats = statisticsService.getOverview(startStr, endStr);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue-7-days")
    public ResponseEntity<List<DailyRevenueResponse>> getChartData(
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        List<DailyRevenueResponse> data = statisticsService.getChartData(startStr, endStr);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/top-movies")
    public ResponseEntity<List<TopMovieResponse>> getTopMovies(
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        List<TopMovieResponse> topMovies = statisticsService.getTopMovies(startStr, endStr);
        return ResponseEntity.ok(topMovies);
    }

    @GetMapping("/detail")
    public ResponseEntity<List<DailyDetailResponse>> getDailyDetail(@RequestParam("day") int day) {

        List<DailyDetailResponse> details = statisticsService.getDailyDetail(day);
        return ResponseEntity.ok(details);
    }
}