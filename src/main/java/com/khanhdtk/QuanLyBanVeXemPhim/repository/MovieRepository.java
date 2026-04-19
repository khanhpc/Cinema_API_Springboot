package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByDeletedFalse();

    Movie findByIdAndDeletedFalse(Long id);

    Optional<Movie> findBytmdbId(Long movieId);
}
