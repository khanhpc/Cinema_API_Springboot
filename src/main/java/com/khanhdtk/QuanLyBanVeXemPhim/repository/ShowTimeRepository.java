package com.khanhdtk.QuanLyBanVeXemPhim.repository;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    @Query("""
                SELECT s FROM ShowTime s
                WHERE s.room.id = :roomId
                AND s.startTime < :end
                AND s.endTime > :start
            """)
    List<ShowTime> findConflictingShowtimes(
            @Param("roomId") Long roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT st FROM ShowTime st
    WHERE st.movie.id = :movieId
      AND st.startTime > :time
      AND st.room.deleted = false
    ORDER BY st.startTime ASC
""")
    List<ShowTime> findAvailableShowtimes(
            @Param("movieId") Long movieId,
            @Param("time") LocalDateTime time
    );
}
