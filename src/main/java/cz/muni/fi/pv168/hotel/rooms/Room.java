package cz.muni.fi.pv168.hotel.rooms;

import cz.muni.fi.pv168.hotel.gui.I18N;

public class Room {
    private int roomNumber;
    private int pricePerNight;
    private int standardBeds;
    private int kingsizeBeds;
    private static final I18N I18N = new I18N(Room.class);

    public Room(int roomNumber, int pricePerNight, int standardBeds, int kingsizeBeds) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.standardBeds = standardBeds;
        this.kingsizeBeds = kingsizeBeds;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getStandardBeds() {
        return standardBeds;
    }

    public void setStandardBeds(int standardBeds) {
        this.standardBeds = standardBeds;
    }

    public int getKingsizeBeds() {
        return kingsizeBeds;
    }

    public void setKingsizeBeds(int kingsizeBeds) {
        this.kingsizeBeds = kingsizeBeds;
    }

    @Override
    public String toString() {
        return I18N.getString("roomLabel") + getRoomNumber() + ", " + I18N.getString("standardBeds") + ": " + getStandardBeds()
                + ", " + I18N.getString("kingsizeBeds") + ": " + getKingsizeBeds()
                + ", "  + I18N.getString("pricePerNight") + ": " + getPricePerNight();
    }
}
