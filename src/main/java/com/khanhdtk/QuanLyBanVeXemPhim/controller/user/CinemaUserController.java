package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Cinema;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.CinemaService;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.CinemaUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/public/cinemas")
@RequiredArgsConstructor
public class CinemaUserController {
    private final CinemaUserService cinemaUserService;

    @GetMapping
    public List<Cinema> getAllCinemas(){
        return cinemaUserService.getAllCinema();
    }
}
