package com.khanhdtk.QuanLyBanVeXemPhim.service.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.CinemaRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Cinema;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public Cinema createCenima(CinemaRequest request) {
        Cinema cinema = new Cinema();
        cinema.setName(request.getName());
        cinema.setLocation(request.getLocation());
        return cinemaRepository.save(cinema);
    }

    public List<Cinema> getALlCinema() {
        return cinemaRepository.findAllByDeletedFalseOrderByIdAsc();
    }

    public void deleteCinema(Long id) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow();
        cinema.setDeleted(true);
        cinemaRepository.save(cinema);
    }

    public Cinema updateCinema(Long id, CinemaRequest cinema) {
        Cinema newCinema = cinemaRepository.findById(id).orElseThrow();
        newCinema.setLocation(cinema.getLocation());
        newCinema.setName(cinema.getName());
        return cinemaRepository.save(newCinema);
    }
}
