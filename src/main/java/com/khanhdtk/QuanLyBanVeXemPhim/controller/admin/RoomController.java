package com.khanhdtk.QuanLyBanVeXemPhim.controller.admin;

import com.khanhdtk.QuanLyBanVeXemPhim.dto.admin.RoomRequest;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.Room;
import com.khanhdtk.QuanLyBanVeXemPhim.service.admin.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@RequestBody RoomRequest request) {
        Room room = roomService.createRoom(request);
        return ResponseEntity.ok(room);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }
}
