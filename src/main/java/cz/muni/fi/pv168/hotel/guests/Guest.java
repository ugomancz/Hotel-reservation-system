package cz.muni.fi.pv168.hotel.guests;

import java.time.LocalDate;

/**
 * @author Denis Kollar
 */
public class Guest {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private String guestId;
    private Long reservationId;

    public Guest(String name, LocalDate birthDate, String guestId, Long reservationId) {
        this.name = name;
        this.birthDate = birthDate;
        this.guestId = guestId;
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }
}
