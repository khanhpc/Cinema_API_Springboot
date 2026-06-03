package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ComboBongNuoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComboBongNuocRepository extends JpaRepository<ComboBongNuoc, Long> {
    List<ComboBongNuoc> findAllByDeletedFalseOrderByIdDesc();
}
