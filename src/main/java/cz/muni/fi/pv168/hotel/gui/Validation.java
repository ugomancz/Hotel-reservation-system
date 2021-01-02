package cz.muni.fi.pv168.hotel.gui;

/**
 * @author Ondrej Kostik
 */
public class Validation {

    public static boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Validation() {
        throw new AssertionError();
    }
}
