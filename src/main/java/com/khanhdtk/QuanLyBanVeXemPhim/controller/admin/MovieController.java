package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;


import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.MovieCommentRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.MovieService;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.MovieCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "all_movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> listMovies = movieService.getAllMovies();
        return ResponseEntity.ok(listMovies);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Đã xóa thành công id " + id);
    }

    @GetMapping("/update-rating/{id}")
    public ResponseEntity<?> updateRatingTest(@PathVariable Long id) {
        movieService.updateRatingTest(id);
        return ResponseEntity.ok("Update rating phim " + id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        movieService.updateMovie(id, movie);
        return ResponseEntity.ok("Update Phim " + movie.getTitle() + " thành công");
    }
}
