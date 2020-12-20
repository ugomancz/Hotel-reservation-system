package cz.muni.fi.pv168.hotel.clients;

import cz.muni.fi.pv168.hotel.DataAccessException;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Denis Kollar
 */
public class ClientDao {
    private final DataSource dataSource;

    public ClientDao(DataSource dataSource) {
        this.dataSource = dataSource;
        if (!tableExits("APP", "HOST")) {
            createTable();
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

            st.executeUpdate("CREATE TABLE APP.HOST (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "FNAME VARCHAR(100) NOT NULL," +
                    "LNAME VARCHAR(100) NOT NULL," +
                    "GUESTID VARCHAR(100))");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create HOST table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.RESERVATION");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop HOST table", ex);
        }
    }
}

