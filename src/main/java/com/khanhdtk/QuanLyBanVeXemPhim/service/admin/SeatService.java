package com.khanhdtk.QuanLyBanVeXemPhim.service.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.user.SeatStatusDTO;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Room;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Seat;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.RoomRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    public void generateSeatsForRoom(Long roomId, int totalRows, int seatsPerRow) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thấy phòng"));

        for (int i = 0; i < totalRows; i++) {
            char rowChar = (char) ('A' + i);
            boolean isLastRow = (i == totalRows - 1);

            int actualSeats = isLastRow ? seatsPerRow / 2 : seatsPerRow;

            for (int j = 1; j <= actualSeats; j++) { // bắt đầu từ 1 cho đẹp
                Seat seat = new Seat();
                seat.setRoom(room);
                seat.setSeatRow(String.valueOf(rowChar));
                seat.setSeatNumber(j);

                if (isLastRow) {
                    seat.setType("SWEETBOX");
                    seat.setSurcharge(50000L);
                } else if (i >= 4) {
                    seat.setType("VIP");
                    seat.setSurcharge(5000L);
                } else {
                    seat.setType("STANDARD");
                    seat.setSurcharge(0L);
                }

                seatRepository.save(seat);
            }
        }
    }

    public List<SeatStatusDTO> getSeatStatus(Long roomId, Long showtimeId){
        List<Object[]> results = seatRepository.findSeatStatusByShowtime(roomId, showtimeId);
        return results.stream().map(result -> new SeatStatusDTO(
                ((Number) result[0]).longValue(), // id
                (String) result[1],               // seatRow
                ((Number) result[2]).intValue(),  // seatNumber
                (String) result[3],               // type
                (Long) result[4],           // surcharge
                (Boolean) result[5]               // isBooked
        )).toList();
    }
}
