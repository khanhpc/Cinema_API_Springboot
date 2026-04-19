package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;


import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/admin/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/import/{tmdbId}")
    public Movie importMovie(@PathVariable Long tmdbId) {
        return movieService.addMovieFromTMDB(tmdbId);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> listMovies = movieService.getAllMovies();
        return ResponseEntity.ok(listMovies);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Đã xóa thành công id " + id);
    }

}
