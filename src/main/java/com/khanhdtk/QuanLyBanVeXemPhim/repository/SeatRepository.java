package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query(value = "SELECT s.id, s.seat_row, s.seat_number, s.type, s.surcharge, " +
            "CASE WHEN COUNT(b.id) > 0 THEN true ELSE false END as is_booked " +
            "FROM seats s " +
            "LEFT JOIN bookings b ON s.id = b.seat_id " +
            "AND b.showtime_id = :showtimeId " +
            "AND b.status <> 'CANCELLED' " +
            "WHERE s.room_id = :roomId " +
            "GROUP BY s.id, s.seat_row, s.seat_number, s.type, s.surcharge " +
            "ORDER BY s.seat_row, s.seat_number",
            nativeQuery = true)
    List<Object[]> findSeatStatusByShowtime(Long roomId, Long showtimeId);
}
