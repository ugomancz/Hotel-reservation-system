package cz.muni.fi.pv168.hotel_app.reservations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private int hosts;
    private int roomNumber;
    private LocalDate arrival;
    private LocalDate departure;
    private ReservationStatus status;
    private String guestID;

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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestID() {
        return guestID;
    }

    public void setGuestID(String guestID) {
        this.guestID = guestID;
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
        return getDeparture().compareTo(getArrival());
    }

    @Override
    public String toString() {
        return getName() + ", room: " + getRoomNumber() + ", "
                + DateTimeFormatter.ofPattern("dd.MM.").format(getArrival())
                + " - " + DateTimeFormatter.ofPattern("dd.MM.").format(getDeparture());
    }
}
