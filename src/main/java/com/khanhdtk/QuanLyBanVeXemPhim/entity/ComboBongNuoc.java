package com.khanhdtk.QuanLyBanVeXemPhim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "combo_bong_nuoc")
public class ComboBongNuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private Long price;
    private Boolean deleted = false;
}
