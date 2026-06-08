package com.khanhdtk.QuanLyBanVeXemPhim.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tmdb_id")
    private Long tmdbId;

    private String title;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    private Integer duration;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private Boolean deleted = false;

    @Column(nullable = false, name = "avg_rating")
    private Float avgRating = 0.0F;
}
