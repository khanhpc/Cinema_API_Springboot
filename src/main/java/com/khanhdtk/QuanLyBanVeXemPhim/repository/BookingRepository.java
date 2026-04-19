package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    public boolean existsByShowTimeIdAndSeatIdAndStatusNot(Long showtimeId, Long seatId, String status);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = 'CANCELLED' WHERE b.status = 'PENDING' AND b.createdAt <= :expiryTime")
    int cancelExpiredBookings(@Param("expiryTime") LocalDateTime expiryTime);

    List<Booking> findByUserIdAndStatusNotOrderByCreatedAtDesc(Long userId, String status);

    Long countByUserIdAndStatus(Long userId, String status);
}
