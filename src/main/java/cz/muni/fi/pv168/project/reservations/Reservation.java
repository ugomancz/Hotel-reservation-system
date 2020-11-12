package cz.muni.fi.pv168.project.reservations;

import cz.muni.fi.pv168.project.Main;

import java.time.LocalDate;

public class Reservation {
    private LocalDate arrival;
    private LocalDate departure;
    private int roomNumber;
    private int hosts;
    private String name;
    private String phone;
    private String email;
    private ReservationStatus status;

    public Reservation(String name, String phone, String email, int hosts,
                       int roomNumber, LocalDate arrival, LocalDate departure) {
        this.name = name;
        this.hosts = hosts;
        this.roomNumber = roomNumber;
        this.arrival = arrival;
        this.departure = departure;
        this.status = ReservationStatus.planned;
        this.phone = phone;
        this.email = email;
        Main.reservations.add(this);
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getHosts() {
        return hosts;
    }

    public void setHosts(int hosts) {
        this.hosts = hosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}