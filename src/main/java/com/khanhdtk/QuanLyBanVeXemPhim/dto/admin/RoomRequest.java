package com.khanhdtk.QuanLyBanVeXemPhim.dto.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Cinema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomRequest {
    private String name;
    private Long cinemaId;
}
