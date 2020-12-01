package cz.muni.fi.pv168.hotel_app.rooms;

public final class RoomDao {

    public final static RoomType STANDARD = new RoomType(3500, BedType.STANDARD, 2);
    public final static RoomType DOUBLE = new RoomType(6500, BedType.KING_SIZE, 2);

    public final static Room roomOne = new Room(1, STANDARD);
    public final static Room roomTwo = new Room(2, STANDARD);
    public final static Room roomThree = new Room(3, DOUBLE);
    public final static Room roomFour = new Room(4, DOUBLE);
    public final static Room roomFive = new Room(5, DOUBLE);
    public final static Room roomSix = new Room(6, STANDARD);
    public final static Room roomSeven = new Room(7, DOUBLE);
    public final static Room roomEight = new Room(8, DOUBLE);
    public final static Room roomNine = new Room(9, DOUBLE);
    public final static Room roomTen = new Room(10, DOUBLE);
    public final static Room roomEleven = new Room(11, STANDARD);
    public final static Room roomTwelve = new Room(12, STANDARD);
    public final static Room roomThirteen = new Room(13, STANDARD);
    public final static Room roomFourteen = new Room(14, DOUBLE);
    public final static Room roomFifteen = new Room(15, DOUBLE);
}
