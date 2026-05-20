package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.MovieComment;
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
    List<MovieComment> findByMovieIdOrderByCreatedAtDesc(Long movieId);

    boolean existsByMovieIdAndUserId(Long movieId, Long userId);
}
