package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoomShowtimesDTO {
    private Long roomId;
    private String roomName;
    private List<ShowtimeSimpleDTO> times;
}
