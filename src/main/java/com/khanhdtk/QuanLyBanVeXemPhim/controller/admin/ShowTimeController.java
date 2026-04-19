package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.ShowTimeRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ShowTime;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.ShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/showtimes")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @PostMapping("/create")
    public ResponseEntity<?> createShowTime(@RequestBody ShowTimeRequest request) {
        showTimeService.createShowTime(request);
        return ResponseEntity.ok("Đã tạo thành công showtime");
    }

    @GetMapping
    public ResponseEntity<List<ShowTime>> getAllShowtimes() {
        return ResponseEntity.ok(showTimeService.getAllShowtimes());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteShowtime(@PathVariable Long id) {
        showTimeService.deleteShowtime(id);
        return ResponseEntity.ok("Đã Xóa Thành Công");
    }
}
