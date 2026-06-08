package com.khanhdtk.QuanLyBanVeXemPhim.controller.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.service.user.MovieCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("public/movies/{movieId}/comments/count")
    public ResponseEntity<Integer> countMovieComments(@PathVariable Long movieId) {
        return ResponseEntity.ok(movieCommentService.countMovieComments(movieId));
    }

    @PostMapping("user/movies/{movieId}/comments")
    public ResponseEntity<MovieCommentResponse> createComment(
            @PathVariable Long movieId,
            @RequestBody MovieCommentRequest request
    ) {
        return ResponseEntity.ok(movieCommentService.createComment(movieId, request));
    }
}
