package com.khanhdtk.QuanLyBanVeXemPhim.entity;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private ShowTime showTime;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private String status;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private Long price;

    @Column(name = "create_at")
    private LocalDateTime createdAt;
}
