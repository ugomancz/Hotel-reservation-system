package cz.muni.fi.pv168.hotel.guests;

import cz.muni.fi.pv168.hotel.DataAccessException;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * @author Denis Kollar
 */
public class GuestDao {

    private final DataSource dataSource;

    public GuestDao(DataSource dataSource) {
        this.dataSource = dataSource;
        if (!tableExists("APP", "GUEST")) {
            createTable();
        }
    }

    public void create(Guest guest) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO GUEST (NAME, BIRTHDATE, GUESTID, RESERVATIONID) VALUES (?, ?, ?,?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, guest.getName());
            st.setDate(2, Date.valueOf(guest.getBirthDate()));
            st.setString(3, guest.getGuestId());
            st.setLong(4, guest.getReservationId());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    guest.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for guest: " + guest);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store guest " + guest, ex);
        }
    }

    public void delete(Guest guest) {
        if (guest.getId() == null) {
            throw new IllegalArgumentException("Guest has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM GUEST WHERE ID = ?")) {
            st.setLong(1, guest.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing guest: " + guest);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete guest " + guest, ex);
        }
    }

    public void update(Guest guest) {
        if (guest.getId() == null) {
            throw new IllegalArgumentException("Guest has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE GUEST SET NAME = ?, BIRTHDATE = ?, GUESTID = ?, RESERVATIONID=? WHERE ID = ?")) {
            st.setString(1, guest.getName());
            st.setDate(2, Date.valueOf(guest.getBirthDate()));
            st.setString(3, guest.getGuestId());
            st.setLong(4, guest.getReservationId());
            st.setLong(5, guest.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing guest: " + guest);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update guest " + guest, ex);
        }
    }

    private boolean tableExists(String schemaName, String tableName) {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, schemaName, tableName, null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + schemaName + "." + tableName + " exists", ex);
        }
    }

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.GUEST (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "BIRTHDATE DATE NOT NULL," +
                    "GUESTID VARCHAR(100)," +
                    "RESERVATIONID BIGINT REFERENCES Reservation(ID))");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create GUEST table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.GUEST");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop GUEST table", ex);
        }
    }
}

