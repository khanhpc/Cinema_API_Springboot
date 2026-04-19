package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ShowtimeGroupDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<RoomShowtimesDTO> rooms;
}

