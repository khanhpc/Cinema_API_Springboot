package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ComboBongNuoc;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.ComboBongNuocService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/combo-bongnuoc")
public class ComboBongNuocController {
    private final ComboBongNuocService comboBongNuocService;

    @PostMapping("/create")
    public ResponseEntity<ComboBongNuoc> createCombo(@RequestBody ComboBongNuoc comboBongNuoc) {
        return ResponseEntity.ok(comboBongNuocService.createCombo(comboBongNuoc));
    }

    @GetMapping
    public ResponseEntity<List<ComboBongNuoc>> getAllComboBongNuoc() {
        return ResponseEntity.ok(comboBongNuocService.getAllComboBongNuoc());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComboBongNuoc(@PathVariable Long id) {
        comboBongNuocService.deleteComboBongNuoc(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ComboBongNuoc> updateCombo(@RequestBody ComboBongNuoc comboBongNuoc, @PathVariable Long id) {
        return ResponseEntity.ok(comboBongNuocService.updateCombo(id, comboBongNuoc));
    }
}
