package cz.muni.fi.pv168.hotel.clients;

import cz.muni.fi.pv168.hotel.DataAccessException;

import javax.sql.DataSource;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * @author Denis Kollar
 */
public class ClientDao {
    private final DataSource dataSource;

    public ClientDao(DataSource dataSource) {
        this.dataSource = dataSource;
        if (!tableExists("APP", "CLIENT")) {
            createTable();
        }
    }

    public void create(Client client) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO CLIENT (NAME, GUESTID) VALUES (?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, client.getName());
            st.setString(2, client.getGuestId());
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

    public void delete(Client client) {
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
                     "UPDATE CLIENT SET NAME = ?, GUESTID = ? WHERE ID = ?")) {
            st.setString(1, client.getName());
            st.setString(2, client.getGuestId());
            st.setLong(3, client.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing client: " + client);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update client " + client, ex);
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

            st.executeUpdate("CREATE TABLE APP.CLIENT (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
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

