package com.khanhdtk.QuanLyBanVeXemPhim.entity.ThanhToan;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.Booking;
import com.khanhdtk.QuanLyBanVeXemPhim.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String ticketCode;

    private Long totalPrice;

    private String status; //PENDING, PAID, CANCELLED

    private LocalDateTime createdAt;

    // Danh sách các combo trong hóa đơn này
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceCombo> invoiceCombos;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Booking> bookings; // Một hóa đơn có nhiều ghế đã đặt
}
