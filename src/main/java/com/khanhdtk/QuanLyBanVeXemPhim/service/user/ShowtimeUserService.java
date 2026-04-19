package com.khanhdtk.QuanLyBanVeXemPhim.service.user;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.CinemaShowtimeDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.RoomShowtimesDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.ShowtimeGroupDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.ShowtimeSimpleDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Cinema;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Room;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ShowTime;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.ShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeUserService {
    private final ShowTimeRepository showTimeRepository;

    public List<CinemaShowtimeDTO> getShowtimesByMovie(Long movieId) {
        // Lấy toàn bộ suất chiếu của phim từ thời điểm hiện tại trở đi
        List<ShowTime> allShowtimes = showTimeRepository
                .findAvailableShowtimes(movieId, LocalDateTime.now());

        // BƯỚC 1: Nhóm toàn bộ suất chiếu theo CỤM RẠP (Cinema)
        Map<Cinema, List<ShowTime>> groupByCinema = allShowtimes.stream()
                .collect(Collectors.groupingBy(st -> st.getRoom().getCinema()));

        // Duyệt qua từng Cụm Rạp
        return groupByCinema.entrySet().stream().map(cinemaEntry -> {
            Cinema cinema = cinemaEntry.getKey();
            List<ShowTime> stInCinema = cinemaEntry.getValue();

            // BƯỚC 2: Trong mỗi cụm rạp, nhóm tiếp theo NGÀY (LocalDate)
            Map<LocalDate, List<ShowTime>> groupByDate = stInCinema.stream()
                    .collect(Collectors.groupingBy(st -> st.getStartTime().toLocalDate()));

            // Duyệt qua từng Ngày
            List<ShowtimeGroupDTO> scheduleDtos = groupByDate.entrySet().stream().map(dateEntry -> {
                        LocalDate date = dateEntry.getKey();
                        List<ShowTime> stInDate = dateEntry.getValue();

                        // BƯỚC 3: Trong mỗi ngày, nhóm tiếp theo PHÒNG (Room)
                        Map<Room, List<ShowTime>> groupByRoom = stInDate.stream()
                                .collect(Collectors.groupingBy(ShowTime::getRoom));

                        // Duyệt qua từng Phòng và map ra List<RoomShowtimesDTO>
                        List<RoomShowtimesDTO> roomDtos = groupByRoom.entrySet().stream().map(roomEntry -> {
                            Room room = roomEntry.getKey();
                            List<ShowTime> stInRoom = roomEntry.getValue();

                            // Map suất chiếu ra DTO cuối cùng
                            List<ShowtimeSimpleDTO> timeDtos = stInRoom.stream()
                                    .map(st -> new ShowtimeSimpleDTO(st.getId(), st.getStartTime()))
                                    .sorted(Comparator.comparing(ShowtimeSimpleDTO::getStartTime)) // Sắp xếp giờ từ sớm đến muộn
                                    .collect(Collectors.toList());

                            return new RoomShowtimesDTO(room.getId(), room.getName(), timeDtos);
                        }).collect(Collectors.toList());

                        return new ShowtimeGroupDTO(date, roomDtos);

                    })
                    .sorted(Comparator.comparing(ShowtimeGroupDTO::getDate)) // Sắp xếp ngày từ gần đến xa
                    .collect(Collectors.toList());

            // Gom tất cả vào DTO Tổng của Rạp
            return new CinemaShowtimeDTO(cinema.getId(), cinema.getName(), scheduleDtos);

        }).collect(Collectors.toList());
    }

    public ShowTime getShowtime(Long id) {
        return showTimeRepository.findById(id).orElse(null);
    }
}
