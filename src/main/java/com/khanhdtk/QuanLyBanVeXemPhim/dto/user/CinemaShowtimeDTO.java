package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class CinemaShowtimeDTO {
    private Long cinemaId;
    private String cinemaName;
    private List<ShowtimeGroupDTO> schedule;
}
