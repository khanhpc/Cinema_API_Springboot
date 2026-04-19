package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.Invoice;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.InvoiceCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceComboRepository extends JpaRepository<InvoiceCombo, Long> {
    void deleteByInvoice(Invoice invoice);
}
