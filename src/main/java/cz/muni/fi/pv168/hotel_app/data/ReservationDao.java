package cz.muni.fi.pv168.hotel_app.data;


import javax.sql.DataSource;
import java.sql.SQLException;


/**
 * @author Denis Kollar
 */
@SuppressWarnings("SqlNoDataSourceInspection")
public final class ReservationDao {
    private final DataSource dataSource;

    public ReservationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE RESERVATIONS (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "ARRIVAL DATE NOT NULL" +
                    "DEPARTURE DATE NOT NULL" +
                    "ROOMNUMBER INT(10) NOT NULL," +
                    "HOSTS INT," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "PHONE VARCHAR(100) NOT NULL," +
                    "EMAIL VARCHAR(100)," +
                    "STATUS VARCHAR(100) NOT NULL," +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create EMPLOYEE table", ex);
        }
    }

}
