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

    @Query("SELECT COALESCE(SUM(i.totalPrice), 0) FROM Invoice i WHERE i.status IN ('PAID', 'CONFIRMED') AND i.createdAt BETWEEN :start AND :end")
    double sumInvoiceRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(ic.quantity * ic.priceAtBooking), 0) " +
            "FROM InvoiceCombo ic " +
            "WHERE ic.invoice.status IN ('PAID', 'CONFIRMED') " +
            "AND ic.invoice.createdAt BETWEEN :start AND :end")
    double sumComboRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(ic.quantity), 0) FROM InvoiceCombo ic WHERE ic.invoice.status IN ('PAID', 'CONFIRMED') AND ic.invoice.createdAt BETWEEN :start AND :end")
    Long sumTotalCombos(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.invoice.status IN ('PAID', 'CONFIRMED') AND b.invoice.createdAt BETWEEN :start AND :end")
    Long countTotalTickets(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT i.id, c.name, m.title, r.name, COUNT(b), i.totalPrice " +
            "FROM Booking b " +
            "JOIN b.invoice i JOIN b.showTime st JOIN st.movie m JOIN st.room r JOIN r.cinema c " +
            "WHERE i.status IN ('PAID', 'CONFIRMED') AND i.createdAt BETWEEN :start AND :end " +
            "GROUP BY i.id, c.name, m.title, r.name, i.totalPrice")
    List<Object[]> getInvoiceTicketDetails(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT i.id, c.name, ic.quantity " +
            "FROM Invoice i " +
            "JOIN i.invoiceCombos ic " +
            "JOIN ic.comboBongNuoc c " +
            "WHERE i.createdAt BETWEEN :start AND :end " +
            "AND i.status IN ('PAID', 'CONFIRMED')")
    List<Object[]> getInvoiceComboDetailsWithNames(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Invoice> findByUserAndStatusNotOrderByCreatedAtDesc(User user, String status);
}