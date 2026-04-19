package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.CinemaRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Cinema;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/cinemas")
public class CinemaController {
    private final CinemaService cinemaService;

    @PostMapping("/create")
    public ResponseEntity<Cinema> createCinema(@RequestBody CinemaRequest request) {
        Cinema cinema = cinemaService.createCenima(request);
        return ResponseEntity.ok(cinema);
    }

    @GetMapping
    public ResponseEntity<List<Cinema>> getALlCinema() {
        List<Cinema> danhSachCinema = cinemaService.getALlCinema();
        return ResponseEntity.ok(danhSachCinema);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return ResponseEntity.ok().build();
    }
}
