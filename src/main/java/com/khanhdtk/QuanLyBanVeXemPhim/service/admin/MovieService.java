package com.khanhdtk.QuanLyBanVeXemPhim.service.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.controller.admin.MovieController;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.MovieCommentRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final MovieCommentRepository movieCommentRepository;

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final String TMDB_URL = "https://api.themoviedb.org/3/movie/";

    @CacheEvict(value = "all_movies", allEntries = true)
    public Movie addMovieFromTMDB(Long tmdbId) {
        String url = TMDB_URL + tmdbId + "?api_key=" + apiKey + "&language=vi-VN";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                Movie movie = movieRepository.findBytmdbId(tmdbId).orElse(null);

                if (movie != null) {
                    movie.setDeleted(false);
                } else {
                    movie = new Movie();
                    movie.setTmdbId(tmdbId);
                }

                movie.setTitle((String) response.get("title"));

                movie.setDescription((String) response.get("overview"));

                if (response.get("poster_path") != null) {
                    movie.setPosterUrl("https://image.tmdb.org/t/p/w500" + response.get("poster_path"));
                }

                Object runtime = response.get("runtime");
                movie.setDuration(runtime != null ? (Integer) runtime : 120); // Mặc định 120 phút nếu không có

                Object releaseDate = response.get("release_date");
                if (releaseDate != null && !((String) releaseDate).isEmpty()) {
                    movie.setReleaseDate(LocalDate.parse((String) releaseDate));
                }

                String trailerKey = fetchTrailerKey(tmdbId);
                movie.setTrailerUrl(trailerKey);

                return movieRepository.save(movie);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi kết nối TMDB hoặc không tìm thấy phim: " + e.getMessage());
        }

        throw new RuntimeException("Không tìm thấy dữ liệu từ TMDB bác ơi!");
    }

    public Movie addMovie(Movie request) {
        Movie movie = movieRepository.findBytitle(request.getTitle()).orElse(null);

        if (movie != null) {
            throw new RuntimeException("Phim này đã tồn tại rồi!");
        }
        request.setDeleted(false);
        return movieRepository.save(request);
    }


    // 2. HÀM PHỤ: Xử lý logic tìm Trailer (Tiếng Việt -> Tiếng Anh)
    private String fetchTrailerKey(Long tmdbId) {
        try {
            // Lần 1: Cố gắng lấy trailer Tiếng Việt
            String urlVi = TMDB_URL + tmdbId + "/videos?api_key=" + apiKey + "&language=vi-VN";
            String key = extractYoutubeKey(restTemplate.getForObject(urlVi, Map.class));

            // Lần 2: Nếu Tiếng Việt không có, lấy Tiếng Anh (en-US)
            if (key == null) {
                String urlEn = TMDB_URL + tmdbId + "/videos?api_key=" + apiKey + "&language=en-US";
                key = extractYoutubeKey(restTemplate.getForObject(urlEn, Map.class));
            }
            return key;
        } catch (Exception e) {
            System.err.println("Không thể lấy trailer cho phim ID " + tmdbId);
            return null;
        }
    }

    // 3. HÀM PHỤ: Bóc tách cục JSON để lấy đúng Key Youtube
    private String extractYoutubeKey(Map<String, Object> response) {
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            for (Map<String, Object> video : results) {
                // Chỉ lấy video trên YouTube và thuộc loại Trailer
                if ("YouTube".equals(video.get("site")) && "Trailer".equals(video.get("type"))) {
                    return (String) video.get("key");
                }
            }
        }
        return null;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    @CacheEvict(value = "all_movies", allEntries = true)
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim này"));
        movie.setDeleted(true);
        movieRepository.save(movie);
    }

    public void updateRatingTest(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Movie"));

        Float avg = movieCommentRepository.avgRating(movieId).floatValue();
        movie.setAvgRating(avg);

        movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Movie này"));

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setPosterUrl(movie.getPosterUrl());
        existingMovie.setTrailerUrl(movie.getTrailerUrl());
        existingMovie.setDuration(movie.getDuration());
        existingMovie.setReleaseDate(movie.getReleaseDate());
        existingMovie.setDescription(movie.getDescription());

        Movie savedMovie = movieRepository.save(existingMovie);

        updateRatingTest(id);

        return savedMovie;
    }
}
