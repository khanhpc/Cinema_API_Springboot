package com.khanhdtk.QuanLyBanVeXemPhim;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class QuanLyBanVeXemPhimApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
	}
	public static void main(String[] args) {
		SpringApplication.run(QuanLyBanVeXemPhimApplication.class, args);
	}

}
