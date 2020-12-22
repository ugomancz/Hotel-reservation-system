package cz.muni.fi.pv168.hotel.clients;

import cz.muni.fi.pv168.hotel.DataAccessException;
import cz.muni.fi.pv168.hotel.reservations.Reservation;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * @author Denis Kollar
 */
public class ClientDao {
    private final DataSource dataSource;

    public ClientDao(DataSource dataSource) {
        this.dataSource = dataSource;
        if (!tableExits("APP", "CLIENT")) {
            createTable();
        }
    }

    public void create(Client client) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO CLIENT (FNAME, LNAME, GUESTID) VALUES (?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, client.getFirstName());
            st.setString(2, client.getLastName());
            st.setString(3, client.getGuestId());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for client: " + client);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store client " + client, ex);
        }
    }

    public void delete(Reservation client) {
        if (client.getId() == null) {
            throw new IllegalArgumentException("Client has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM CLIENT WHERE ID = ?")) {
            st.setLong(1, client.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing client: " + client);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete client " + client, ex);
        }
    }

    public void update(Client client) {
        if (client.getId() == null) {
            throw new IllegalArgumentException("Client has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE CLIENT SET FNAME = ?, LNAME = ?, GUESTID = ? WHERE ID = ?")) {
            st.setString(1, client.getFirstName());
            st.setString(2, client.getLastName());
            st.setString(3, client.getGuestId());
            st.setLong(4, client.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing client: " + client);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update client " + client, ex);
        }
    }

    private boolean tableExits(String schemaName, String tableName) {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, schemaName, tableName, null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + schemaName + "." + tableName + " exist", ex);
        }
    }

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.CLIENT (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "FNAME VARCHAR(100) NOT NULL," +
                    "LNAME VARCHAR(100) NOT NULL," +
                    "GUESTID VARCHAR(100))");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create CLIENT table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.CLIENT");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop CLIENT table", ex);
        }
    }
}

