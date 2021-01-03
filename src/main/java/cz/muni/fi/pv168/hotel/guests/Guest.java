package cz.muni.fi.pv168.hotel.guests;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return name.equals(guest.name) &&
                birthDate.equals(guest.birthDate) &&
                guestId.equals(guest.guestId) &&
                reservationId.equals(guest.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthDate, guestId, reservationId);
    }
}
