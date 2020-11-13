package cz.muni.fi.pv168.project.reservations;

import cz.muni.fi.pv168.project.Main;
import cz.muni.fi.pv168.project.gui.SidePanel;

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
    private int length;

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
        this.length = departure.compareTo(arrival);
        Main.reservations.add(this);
        SidePanel.getCalendar().setSelectedDate(arrival);
    }

    public LocalDate getArrival() {
        return arrival;
    }


    public LocalDate getDeparture() {
        return departure;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getHosts() {
        return hosts;
    }

    public String getName() {
        return name;
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

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

}
