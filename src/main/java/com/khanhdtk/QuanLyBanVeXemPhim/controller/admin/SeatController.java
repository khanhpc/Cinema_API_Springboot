package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.SeatRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/admin/seats")
public class SeatController {
    private final SeatService seatService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateSeat(@RequestBody SeatRequest seatDTO) {
        seatService.generateSeatsForRoom(seatDTO.getRoomId(), seatDTO.getRows(), seatDTO.getPerRow());
        return ResponseEntity.ok("Đã tạo ghế thành công ");
    }
}
