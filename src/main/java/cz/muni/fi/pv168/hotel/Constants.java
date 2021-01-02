package cz.muni.fi.pv168.hotel;

import java.awt.Color;
import java.awt.Font;

public final class Constants {

    public static final Color BACKGROUND_COLOR = Color.lightGray;
    public static final int DAYS_IN_WEEK = 7;
    public static final String CONFIG_FILE_PATH = System.getProperty("user.home") + "/hotel_config";
    public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 14);
    public static final Color BUTTON_BACKGROUND = new Color(240, 240, 240);

    private Constants() {
        throw new AssertionError();
    }
}
