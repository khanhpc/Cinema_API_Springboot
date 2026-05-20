package com.khanhdtk.QuanLyBanVeXemPhim.service.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.MovieCommentResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.MovieComment;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.User;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.BadRequestException;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.BookingRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.MovieCommentRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.MovieRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieCommentService {
    private final MovieCommentRepository movieCommentRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Cacheable(value = "movie_comments", key = "#movieId")
    public List<MovieCommentResponse> getComments(Long movieId) {
        return movieCommentRepository.findByMovieIdOrderByCreatedAtDesc(movieId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = {"movie_comments", "top_movies"}, key = "#movieId")
    public MovieCommentResponse createComment(Long movieId,
                                              MovieCommentRequest request) {

        validateRequest(request);

        String email = getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Người dùng không tồn tại"));

        Movie movie = movieRepository.findByIdAndDeletedFalse(movieId);

        if (movie == null) {
            throw new ResourceNotFoundException("Phim không tồn tại");
        }

        MovieComment comment = movieCommentRepository
                .findByMovieIdAndUserId(movieId, user.getId());

        if (comment == null) {

            boolean watchedMovie = bookingRepository.existsWatchedMovie(
                    user.getId(),
                    movieId,
                    LocalDateTime.now()
            );

            if (!watchedMovie) {
                throw new BadRequestException(
                        "Bạn chỉ có thể bình luận sau khi đã xem xong phim"
                );
            }

            comment = new MovieComment();
            comment.setMovie(movie);
            comment.setUser(user);
            comment.setCreatedAt(LocalDateTime.now());
        }

        comment.setContent(request.getContent().trim());
        comment.setRating(request.getRating());

        MovieComment savedComment = movieCommentRepository.save(comment);

        movie.setAvgRating(
                movieCommentRepository.avgRating(movieId).floatValue()
        );

        return toResponse(savedComment);
    }

    private void validateRequest(MovieCommentRequest request) {
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 10) {
            throw new BadRequestException("Số sao đánh giá phải từ 1 đến 10");
        }
        if (request.getContent() == null || request.getContent().trim().isBlank()) {
            throw new BadRequestException("Nội dung bình luận không được để trống");
        }
    }

    private MovieCommentResponse toResponse(MovieComment comment) {
        return new MovieCommentResponse(
                comment.getId(),
                comment.getUser().getEmail(),
                comment.getContent(),
                comment.getRating(),
                comment.getCreatedAt()
        );
    }

    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("Bạn cần đăng nhập để thực hiện chức năng này");
        }
        return auth.getName();
    }
}