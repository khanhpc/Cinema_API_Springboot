package com.khanhdtk.QuanLyBanVeXemPhim.service.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ComboBongNuoc;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.BadRequestException;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.ComboBongNuocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComboBongNuocService {
    private final ComboBongNuocRepository comboBongNuocRepository;

    public ComboBongNuoc createCombo(ComboBongNuoc comboBongNuoc) {
        comboBongNuoc.setDeleted(false);
        return comboBongNuocRepository.save(comboBongNuoc);
    }

    public List<ComboBongNuoc> getAllComboBongNuoc() {
        return comboBongNuocRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public void deleteComboBongNuoc(Long id) {
        ComboBongNuoc comboBongNuoc = comboBongNuocRepository.findById(id).orElse(null);
        comboBongNuoc.setDeleted(true);
        comboBongNuocRepository.save(comboBongNuoc);
    }

    public ComboBongNuoc updateCombo(Long id, ComboBongNuoc comboBongNuoc) {
        ComboBongNuoc newComboBongNuoc = comboBongNuocRepository.findById(id).orElse(null);
        if (comboBongNuoc == null) {
            throw new ResourceNotFoundException("Không tìm thấy Combo này");
        }
        newComboBongNuoc.setName(comboBongNuoc.getName());
        newComboBongNuoc.setPrice(comboBongNuoc.getPrice());
        newComboBongNuoc.setDescription(comboBongNuoc.getDescription());
        return comboBongNuocRepository.save(newComboBongNuoc);
    }
}
