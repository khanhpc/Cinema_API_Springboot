package com.khanhdtk.QuanLyBanVeXemPhim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "seat_row")
    private String seatRow;

    @Column(name = "seat_number")
    private Integer seatNumber;

    private String type;

    private Long surcharge;
}
