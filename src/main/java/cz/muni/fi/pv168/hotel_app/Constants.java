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



    public Constants() {
        throw new AssertionError();
    }
}
