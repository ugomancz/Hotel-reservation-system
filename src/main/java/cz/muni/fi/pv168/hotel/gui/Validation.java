package cz.muni.fi.pv168.hotel.gui;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * @author Ondrej Kostik
 */
public class Validation {

    private Validation() {
        throw new AssertionError();
    }

    public static boolean isNotNumeric(String input) {
        try {
            Double.parseDouble(input);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static boolean isNotEmail(String input) {
        return !EmailValidator.getInstance().isValid(input);
    }

    public static boolean isAlpha(String input) {
        return input.replaceAll("\\s+", "").chars().allMatch(Character::isLetter);
    }
}
