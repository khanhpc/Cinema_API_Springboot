package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.MovieComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieCommentRepository extends JpaRepository<MovieComment, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<MovieComment> findByMovieIdOrderByCreatedAtDesc(
            Long movieId,
            Pageable pageable
    );

    Integer countByMovieId(Long movieId);

    boolean existsByMovieIdAndUserId(Long movieId, Long userId);

    MovieComment findByMovieIdAndUserId(Long movieId, Long userId);

    @Query("""
            SELECT COALESCE(AVG(mc.rating), 0)
            FROM MovieComment mc
            WHERE mc.movie.id = :movieId
            """)
    Double avgRating(Long movieId);
}
