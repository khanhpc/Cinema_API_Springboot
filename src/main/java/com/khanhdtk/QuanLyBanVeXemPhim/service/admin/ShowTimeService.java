package com.khanhdtk.QuanLyBanVeXemPhim.service.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.ShowTimeRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Movie;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Room;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ShowTime;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.MovieRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.RoomRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.ShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowTimeService {
    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ShowTime createShowTime(ShowTimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thấy phim này"));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thầy phòng"));
        LocalDateTime start = LocalDateTime.parse(request.getStartTime());
        LocalDateTime end = start.plusMinutes(movie.getDuration() + 15);

        List<ShowTime> conflicts = showTimeRepository.findConflictingShowtimes(
                room.getId(), start, end
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Phòng đã có lịch chiếu trong khoảng thời gian này!");
        }

        ShowTime showTime = new ShowTime();
        showTime.setMovie(movie);
        showTime.setRoom(room);
        showTime.setStartTime(start);
        showTime.setEndTime(end);
        showTime.setPrice(request.getPrice());

        return showTimeRepository.save(showTime);
    }

    public List<ShowTime> getAllShowtimes() {
        return showTimeRepository.findAll();
    }

    public void deleteShowtime(Long id){
        showTimeRepository.deleteById(id);
    }
}
