package com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ComboBongNuoc;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice_combo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceCombo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    private ComboBongNuoc comboBongNuoc;

    private Long quantity;

    private Long priceAtBooking;
}
