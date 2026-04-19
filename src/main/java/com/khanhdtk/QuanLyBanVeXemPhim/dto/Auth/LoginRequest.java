package com.khanhdtk.QuanLyBanVeXemPhim.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
