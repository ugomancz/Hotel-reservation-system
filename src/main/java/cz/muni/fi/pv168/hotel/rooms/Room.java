package cz.muni.fi.pv168.hotel.rooms;

import cz.muni.fi.pv168.hotel.gui.I18N;

public class Room {
    private final int roomNumber;
    private final RoomType roomType;
    private static final I18N I18N = new I18N(Room.class);

    public Room(int roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return I18N.getString("roomLabel") + getRoomNumber() + ", " + I18N.getString("type") + ": " + getRoomType().getBedType().toString()
                + ", " + I18N.getString("pricePerNight") + ": " + getRoomType().getPricePerNight();
    }
}
