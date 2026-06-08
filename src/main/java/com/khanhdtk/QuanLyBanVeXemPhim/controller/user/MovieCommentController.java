package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.MovieCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovieCommentController {
    private final MovieCommentService movieCommentService;

    @GetMapping("public/movies/{movieId}/comments")
    public ResponseEntity<Page<MovieCommentResponse>> getComments(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                movieCommentService.getComments(movieId, page, size)
        );
    }

    @PostMapping("user/movies/{movieId}/comments")
    public ResponseEntity<MovieCommentResponse> createComment(
            @PathVariable Long movieId,
            @RequestBody MovieCommentRequest request
    ) {
        return ResponseEntity.ok(movieCommentService.createComment(movieId, request));
    }
}
