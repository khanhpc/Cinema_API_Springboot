package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.CinemaShowtimeDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.ShowtimeGroupDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ShowTime;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.ShowtimeUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/public/showtimes")
@RequiredArgsConstructor
public class ShowTimeUserController {
    private final ShowtimeUserService showtimeUserService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<CinemaShowtimeDTO>> getMovieSchedule(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeUserService.getShowtimesByMovie(movieId));
    }

    @GetMapping("{id}")
    public ResponseEntity<ShowTime> getShowtime(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeUserService.getShowtime(id));
    }
}
