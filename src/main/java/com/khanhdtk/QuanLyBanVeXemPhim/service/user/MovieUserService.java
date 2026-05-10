package com.khanhdtk.QuanLyBanVeXemPhim.service.user;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieUserService {
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByDeletedFalse();
    }

    public Movie getMovie(Long id) {
        return movieRepository.findByIdAndDeletedFalse(id);
    }

    public List<Movie> getTopMovies(){
        return movieRepository.findTopMovies_ChieuNhieuNhat();
    }
}
