package cz.muni.fi.pv168.hotel.rooms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.gui.I18N;

import java.util.Objects;

public class Room {

    private static final I18N I18N = new I18N(Room.class);
    private int roomNumber;
    private RoomDao.RoomPriceCategory roomPriceCategory;
    private int standardBeds;
    private int kingsizeBeds;

    public Room(int roomNumber, RoomDao.RoomPriceCategory roomPriceCategory, int standardBeds, int kingsizeBeds) {
        this.roomNumber = roomNumber;
        this.roomPriceCategory = roomPriceCategory;
        this.standardBeds = standardBeds;
        this.kingsizeBeds = kingsizeBeds;
    }

    public RoomDao.RoomPriceCategory getRoomPriceCategory() {
        return roomPriceCategory;
    }

    public void setRoomPriceCategory(RoomDao.RoomPriceCategory roomPriceCategory) {
        this.roomPriceCategory = roomPriceCategory;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getPricePerNight() {
        return Constants.ROOM_PRICES.get(roomPriceCategory);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return getRoomNumber() == room.getRoomNumber() && getStandardBeds() == room.getStandardBeds()
                && getKingsizeBeds() == room.getKingsizeBeds() && getRoomPriceCategory() == room.getRoomPriceCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomNumber(), getRoomPriceCategory(), getStandardBeds(), getKingsizeBeds());
    }

    @Override
    public String toString() {
        return I18N.getString("roomLabel") + getRoomNumber() + ", " + I18N.getString("standardBeds") + ": " + getStandardBeds()
                + ", " + I18N.getString("kingsizeBeds") + ": " + getKingsizeBeds()
                + ", " + I18N.getString("pricePerNight") + ": " + getPricePerNight();
    }
}
