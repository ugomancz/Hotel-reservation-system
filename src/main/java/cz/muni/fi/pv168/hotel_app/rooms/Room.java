package cz.muni.fi.pv168.hotel_app.rooms;

public class Room {
    private final int roomNumber;
    private final RoomType roomType;

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
        return "Room n." + getRoomNumber() + ", bed type: " + getRoomType().getBedType().toString()
                + ", price per night: " + getRoomType().getPricePerNight();
    }
}
