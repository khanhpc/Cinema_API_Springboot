package com.khanhdtk.QuanLyBanVeXemPhim.service.user;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Cinema;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaUserService {
    private final CinemaRepository cinemaRepository;
    public List<Cinema> getAllCinema(){
        return cinemaRepository.findAllByDeletedFalseOrderByIdAsc();
    }
}
