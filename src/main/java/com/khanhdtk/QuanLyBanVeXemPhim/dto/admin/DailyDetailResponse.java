package com.khanhdtk.QuanLyBanVeXemPhim.dto.admin;

public class DailyDetailResponse {
    private String cinema;
    private String movie;
    private String room;
    private long tickets;
    private String combos;
    private long revenue;

    public DailyDetailResponse() {
    }

    // Constructor 1: Dùng cho code mới của anh em mình (Nhận String combos)
    public DailyDetailResponse(String cinema, String movie, String room, long tickets, String combos, long revenue) {
        this.cinema = cinema;
        this.movie = movie;
        this.room = room;
        this.tickets = tickets;
        this.combos = combos;
        this.revenue = revenue;
    }

    // Constructor 2: "Cứu cánh" cho code cũ của Khanh trong BookingRepository (Nhận long combos)
    public DailyDetailResponse(String cinema, String movie, String room, long tickets, long combos, long revenue) {
        this.cinema = cinema;
        this.movie = movie;
        this.room = room;
        this.tickets = tickets;
        this.combos = String.valueOf(combos); // Tự động ép kiểu số của Khanh thành chữ
        this.revenue = revenue;
    }

    public String getCinema() { return cinema; }
    public void setCinema(String cinema) { this.cinema = cinema; }

    public String getMovie() { return movie; }
    public void setMovie(String movie) { this.movie = movie; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public long getTickets() { return tickets; }
    public void setTickets(long tickets) { this.tickets = tickets; }

    public String getCombos() { return combos; }
    public void setCombos(String combos) { this.combos = combos; }

    public long getRevenue() { return revenue; }
    public void setRevenue(long revenue) { this.revenue = revenue; }
}