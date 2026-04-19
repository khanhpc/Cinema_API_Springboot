package com.khanhdtk.QuanLyBanVeXemPhim.dto.Auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
}
