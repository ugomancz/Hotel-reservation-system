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

    public Guest(String name, String guestId) {
        this.name = name;
        this.guestId = guestId;
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
