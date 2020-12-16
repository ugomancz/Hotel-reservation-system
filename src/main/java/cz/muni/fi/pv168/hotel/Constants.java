package cz.muni.fi.pv168.hotel;

import java.awt.Color;
import java.awt.Font;

public final class Constants {

    public final static Color BACKGROUND_COLOR = Color.lightGray;
    public final static int DAYS_IN_WEEK = 7;
    public final static int LOCAL_FEE = 50;
    public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 14);
    public static final Color BUTTON_BACKGROUND = new Color(240, 240, 240);

    private Constants() {
        throw new AssertionError();
    }
}
