package com.khanhdtk.QuanLyBanVeXemPhim.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopMovieResponse {
    private String movieName;
    private Long revenue;
}
