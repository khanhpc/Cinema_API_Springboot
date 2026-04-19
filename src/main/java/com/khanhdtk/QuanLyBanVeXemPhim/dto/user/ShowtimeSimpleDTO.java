package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShowtimeSimpleDTO {
    private Long showtimeId;
    private LocalDateTime startTime;
}
