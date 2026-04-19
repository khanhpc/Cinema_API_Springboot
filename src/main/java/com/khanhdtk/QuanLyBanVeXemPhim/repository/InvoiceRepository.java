package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.Invoice;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Modifying
    @Query("UPDATE Invoice b SET b.status = 'CANCELLED' WHERE b.status = 'PENDING' AND b.createdAt <= :expiryTime")
    int cancelExpiredInvoice(@Param("expiryTime") LocalDateTime expiryTime);

    List<Invoice> findByUserAndStatusNotOrderByCreatedAtDesc(User user, String status);
}
