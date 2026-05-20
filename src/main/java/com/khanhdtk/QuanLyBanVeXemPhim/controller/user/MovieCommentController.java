package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.MovieCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovieCommentController {
    private final MovieCommentService movieCommentService;

    @GetMapping("public/movies/{movieId}/comments")
    public ResponseEntity<List<MovieCommentResponse>> getComments(@PathVariable Long movieId) {
        return ResponseEntity.ok(movieCommentService.getComments(movieId));
    }

    @PostMapping("user/movies/{movieId}/comments")
    public ResponseEntity<MovieCommentResponse> createComment(
            @PathVariable Long movieId,
            @RequestBody MovieCommentRequest request
    ) {
        return ResponseEntity.ok(movieCommentService.createComment(movieId, request));
    }
}
