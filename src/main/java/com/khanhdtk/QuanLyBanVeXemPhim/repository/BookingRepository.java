package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.DailyDetailResponse;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.TopMovieResponse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    public boolean existsByShowTimeIdAndSeatIdAndStatusNot(Long showtimeId, Long seatId, String status);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = 'CANCELLED' WHERE b.status = 'PENDING' AND b.createdAt <= :expiryTime")
    int cancelExpiredBookings(@Param("expiryTime") LocalDateTime expiryTime);

    // Tính tổng doanh thu vé
    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Booking b WHERE b.status = 'CONFIRMED' AND b.createdAt BETWEEN :start AND :end")
    double sumTicketRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Đếm tổng số vé: Tìm trạng thái 'CONFIRMED'
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CONFIRMED' AND b.createdAt BETWEEN :start AND :end")
    int countTickets(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.TopMovieResponse(m.title, SUM(b.price)) " +
            "FROM Booking b " +
            "JOIN b.showTime st " +
            "JOIN st.movie m " +
            "WHERE b.status = 'CONFIRMED' AND b.createdAt BETWEEN :start AND :end " +
            "AND m.deleted = false " +
            "GROUP BY m.title ORDER BY SUM(b.price) DESC")
    List<TopMovieResponse> getTopMovies(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    // Lấy doanh thu theo Phim, Rạp, Phòng
    @Query("SELECT new com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.DailyDetailResponse(" +
            "c.name, m.title, r.name, COUNT(b), 0L, SUM(b.price)) " +
            "FROM Booking b " +
            "JOIN b.showTime st " +
            "JOIN st.movie m " +
            "JOIN st.room r " +
            "JOIN r.cinema c " +
            "WHERE b.status = 'CONFIRMED' AND b.createdAt BETWEEN :start AND :end " +
            "GROUP BY c.name, m.title, r.name")
    List<DailyDetailResponse> getDailyDetails(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Booking> findByUserIdAndStatusNotOrderByCreatedAtDesc(Long userId, String status);

    Long countByUserIdAndStatus(Long userId, String status);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b " +
            "JOIN b.showTime st " +
            "JOIN st.movie m " +
            "WHERE b.user.id = :userId " +
            "AND m.id = :movieId " +
            "AND b.status = 'CONFIRMED' " +
            "AND st.endTime <= :now")
    boolean existsWatchedMovie(@Param("userId") Long userId, @Param("movieId") Long movieId, @Param("now") LocalDateTime now);
}
