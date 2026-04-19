package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.SeatStatusDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/user/seats")
public class SeatUserController {
    private final SeatService seatService;

    @GetMapping("/showtime/{showtimeId}/room/{roomId}")
    public ResponseEntity<List<SeatStatusDTO>> getSeatForUser(@PathVariable Long showtimeId, @PathVariable Long roomId) {
        List<SeatStatusDTO> seats = seatService.getSeatStatus(roomId, showtimeId);
        return ResponseEntity.ok(seats);
    }
}
