package com.khanhdtk.QuanLyBanVeXemPhim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MovieCommentResponse {
    private Long id;
    private String userEmail;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
}
