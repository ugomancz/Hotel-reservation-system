package cz.muni.fi.pv168.hotel.clients;

/**
 * @author Denis Kollar
 */
public class Client {
    private Long id;
    private String firstName;
    private String lastName;
    private String guestId;

    public Client(String firstName, String lastName, String guestId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.guestId = guestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }
}
