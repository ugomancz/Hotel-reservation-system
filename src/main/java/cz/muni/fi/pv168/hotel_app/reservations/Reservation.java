package cz.muni.fi.pv168.hotel_app.reservations;

import cz.muni.fi.pv168.hotel_app.Main;
import cz.muni.fi.pv168.hotel_app.gui.SidePanel;

import java.time.LocalDate;

public class Reservation {
    private Long id;
    private final String name;
    private final String phone;
    private final String email;
    private final int hosts;
    private final int roomNumber;
    private final LocalDate arrival;
    private final LocalDate departure;
    private ReservationStatus status;
    private final int length;

    public Reservation(String name, String phone, String email, int hosts,
                       int roomNumber, LocalDate arrival, LocalDate departure, String status) {
        this.name = name;
        this.hosts = hosts;
        this.roomNumber = roomNumber;
        this.arrival = arrival;
        this.departure = departure;
        this.status = ReservationStatus.valueOf(status.toUpperCase());
        this.phone = phone;
        this.email = email;
        this.length = departure.compareTo(arrival);
        Main.reservations.add(this);
    }

    public void moveToNewDate(LocalDate newDate) {
        SidePanel.getCalendar().setSelectedDate(newDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public int getLength() {
        return length;
    }
}
