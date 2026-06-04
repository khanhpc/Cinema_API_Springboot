package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByDeletedFalseOrderByIdDesc();

    Movie findByIdAndDeletedFalse(Long id);

    Optional<Movie> findBytmdbId(Long movieId);

    @Query(value = """
            SELECT\s
                m.*
            FROM public.movies m
            JOIN public.showtimes s\s
                ON m.id = s.movie_id
            JOIN public.bookings b\s
                ON s.id = b.showtime_id
            WHERE deleted = false
            GROUP BY\s
                m.id,
                m.title,
                m.description,
                m.duration
            ORDER BY COUNT(b.id) DESC
            LIMIT 5;""", nativeQuery = true)
    List<Movie> findTopMovies_ChieuNhieuNhat();
}
