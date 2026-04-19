package com.khanhdtk.QuanLyBanVeXemPhim.controller.Auth;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.Auth.LoginRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.Auth.RegisterRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.User;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.UserRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole(),
                    "message", "Đăng nhập thành công!"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Sai email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("email này đã tồn tại");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(encodedPassword);

        newUser.setRole("USER");

        userRepository.save(newUser);

        return ResponseEntity.ok(newUser);
    }
}
