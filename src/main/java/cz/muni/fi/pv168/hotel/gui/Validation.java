package cz.muni.fi.pv168.hotel.gui;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * @author Ondrej Kostik
 */
public class Validation {

    private Validation() {
        throw new AssertionError();
    }

    public static boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isEmail(String input) {
        return EmailValidator.getInstance().isValid(input);
    }

    public static boolean isAlpha(String input) {
        return input.chars().allMatch(Character::isLetter);
    }
}
