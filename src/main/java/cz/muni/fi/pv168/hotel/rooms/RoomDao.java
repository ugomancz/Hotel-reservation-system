package cz.muni.fi.pv168.hotel.rooms;

import java.util.HashMap;
import java.util.Map;

public final class RoomDao {
    public final static RoomType STANDARD = new RoomType(3500, BedType.STANDARD, 2);
    public final static RoomType DOUBLE = new RoomType(6500, BedType.KING_SIZE, 2);
    public static final Map<Integer, Room> rooms = new HashMap<>(Map.ofEntries(
            Map.entry(1, new Room(1, STANDARD)),
            Map.entry(2, new Room(2, STANDARD)),
            Map.entry(3, new Room(3, DOUBLE)),
            Map.entry(4, new Room(4, DOUBLE)),
            Map.entry(5, new Room(5, DOUBLE)),
            Map.entry(6, new Room(6, STANDARD)),
            Map.entry(7, new Room(7, DOUBLE)),
            Map.entry(8, new Room(8, DOUBLE)),
            Map.entry(9, new Room(9, DOUBLE)),
            Map.entry(10, new Room(10, DOUBLE)),
            Map.entry(11, new Room(11, STANDARD)),
            Map.entry(12, new Room(12, STANDARD)),
            Map.entry(13, new Room(13, STANDARD)),
            Map.entry(14, new Room(14, DOUBLE)),
            Map.entry(15, new Room(15, DOUBLE))
    ));

    private RoomDao() {
        throw new AssertionError();
    }

    public static Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    public static int getPricePerNight(int roomNumber) {
        return rooms.get(roomNumber).getRoomType().getPricePerNight();
    }

    public static int numberOfBeds(int roomNumber) {
        return rooms.get(roomNumber).getRoomType().getNumOfBeds();
    }

    public static int numberOfRooms() {
        return rooms.size();
    }
}
