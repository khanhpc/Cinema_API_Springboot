package com.khanhdtk.QuanLyBanVeXemPhim.exception;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}
