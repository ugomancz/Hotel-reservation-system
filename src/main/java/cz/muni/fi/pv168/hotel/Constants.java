package cz.muni.fi.pv168.hotel;

import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

public final class Constants {

    public static final Color BACKGROUND_COLOR = Color.lightGray;
    public static final int DAYS_IN_WEEK = 7;
    public static final String CONFIG_FILE_PATH = System.getProperty("user.home") + "/hotel_config";
    public static final String DELIMITER = ";";
    public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 14);
    public static final Color BUTTON_BACKGROUND = new Color(240, 240, 240);
    public static final Map<RoomDao.RoomPriceCategory, Integer> ROOM_PRICES = Map.ofEntries(
            Map.entry(RoomDao.RoomPriceCategory.SINGLE_ROOM, 849),
            Map.entry(RoomDao.RoomPriceCategory.DOUBLE_ROOM, 1199),
            Map.entry(RoomDao.RoomPriceCategory.TRIPLE_ROOM, 1649),
            Map.entry(RoomDao.RoomPriceCategory.APARTMENT, 2199)
    );
    private Constants() {
        throw new AssertionError();
    }
}
