package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.MovieUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/public/movies")
public class MovieUserController {
    private final MovieUserService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> layDanhSachMovie() {
        List<Movie> danhsachMovies = movieService.getAllMovies();
        return ResponseEntity.ok(danhsachMovies);
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    @GetMapping("/top-movies")
    ResponseEntity<List<Movie>> layDanhSachTopMovies() {
        List<Movie> danhsachMovies = movieService.getTopMovies();
        return ResponseEntity.ok(danhsachMovies);
    }

}
