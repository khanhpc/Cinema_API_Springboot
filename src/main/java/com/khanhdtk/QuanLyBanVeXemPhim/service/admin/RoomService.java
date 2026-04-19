package com.khanhdtk.QuanLyBanVeXemPhim.service.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.RoomRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Cinema;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Room;
import com.khanhdtk.QuanLyBanVeXemPhim.exception.ResourceNotFoundException;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.CinemaRepository;
import com.khanhdtk.QuanLyBanVeXemPhim.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public Room createRoom(RoomRequest request) {
        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thầy Cinema"));

        Room room = new Room();
        room.setName(request.getName());
        room.setCinema(cinema);
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms(){
        return roomRepository.findAllByDeletedFalse();
    }

    public void deleteRoom(Long id){
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phòng"));
        room.setDeleted(true);
        roomRepository.save(room);
    }
}
