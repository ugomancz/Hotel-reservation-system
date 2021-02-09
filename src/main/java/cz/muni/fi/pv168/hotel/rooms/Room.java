package cz.muni.fi.pv168.hotel.rooms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.gui.I18N;

import java.util.Objects;

public class Room {

    private static final I18N I18N = new I18N(Room.class);
    private final int roomNumber;
    private final RoomDao.RoomPriceCategory roomPriceCategory;
    private final int standardBeds;
    private final int kingsizeBeds;

    public Room(int roomNumber, RoomDao.RoomPriceCategory roomPriceCategory, int standardBeds, int kingsizeBeds) {
        this.roomNumber = roomNumber;
        this.roomPriceCategory = roomPriceCategory;
        this.standardBeds = standardBeds;
        this.kingsizeBeds = kingsizeBeds;
    }

    public RoomDao.RoomPriceCategory getRoomPriceCategory() {
        return roomPriceCategory;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getPricePerNight() {
        return Constants.ROOM_PRICES.get(roomPriceCategory);
    }

    public int getStandardBeds() {
        return standardBeds;
    }

    public int getKingsizeBeds() {
        return kingsizeBeds;
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
