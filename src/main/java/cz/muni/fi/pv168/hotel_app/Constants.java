package cz.muni.fi.pv168.hotel_app;

import cz.muni.fi.pv168.hotel_app.rooms.BedType;
import cz.muni.fi.pv168.hotel_app.rooms.Room;
import cz.muni.fi.pv168.hotel_app.rooms.RoomType;

import java.awt.*;

public final class Constants {

    public final static Color BACKGROUND_COLOR = Color.lightGray;
    public final static int NUMBER_OF_ROOMS = 15;
    public final static int DAYS_IN_WEEK = 7;
    public final static Color PLANNED_RESERVATION = new Color(13,218,13);
    public final static Color ONGOING_RESERVATION = Color.orange;
    public final static Color PAST_RESERVATION = Color.lightGray;
    public final static Color CONCURRING_RESERVATIONS = new Color(60,160,50);

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



    public Constants() {
        throw new AssertionError();
    }
}
