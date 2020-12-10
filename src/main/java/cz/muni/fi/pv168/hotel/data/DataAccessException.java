package cz.muni.fi.pv168.hotel.data;

/**
 * @author Denis Kollar
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

