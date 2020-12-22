package cz.muni.fi.pv168.hotel.clients;

/**
 * @author Denis Kollar
 */
public class Client {
    private Long id;
    private String name;
    private String guestId;

    public Client(String name, String guestId) {
        this.name = name;
        this.guestId = guestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
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
