package cz.muni.fi.pv168.hotel.rooms;

import java.util.Map;

public final class RoomDao {

    public static final RoomType STANDARD = new RoomType(3500, BedType.STANDARD, 2);
    public static final RoomType DOUBLE = new RoomType(6500, BedType.KING_SIZE, 2);
    public static final Map<Integer, Room> ROOMS = Map.ofEntries(
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
            Map.entry(15, new Room(15, DOUBLE)),
            Map.entry(16, new Room(16, DOUBLE)),
            Map.entry(17, new Room(17, STANDARD)),
            Map.entry(18, new Room(18, STANDARD)),
            Map.entry(19, new Room(19, STANDARD)),
            Map.entry(20, new Room(20, DOUBLE))
    );

    private RoomDao() {
        throw new AssertionError();
    }

    public static Room getRoom(int roomNumber) {
        return ROOMS.get(roomNumber);
    }

    public static int getPricePerNight(int roomNumber) {
        return ROOMS.get(roomNumber).getRoomType().getPricePerNight();
    }

    public static int numberOfBeds(int roomNumber) {
        return ROOMS.get(roomNumber).getRoomType().getNumOfBeds();
    }

    public static int numberOfRooms() {
        return ROOMS.size();
    }
}
