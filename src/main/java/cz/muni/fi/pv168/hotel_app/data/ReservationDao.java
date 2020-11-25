package cz.muni.fi.pv168.hotel_app.data;


import javax.sql.DataSource;
import java.sql.SQLException;


/**
 * @author Denis Kollar
 */
public final class ReservationDao {
    private final DataSource dataSource;

    public ReservationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.EMPLOYEE (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "FIRST_NAME VARCHAR(100) NOT NULL," +
                    "LAST_NAME VARCHAR(100) NOT NULL," +
                    "GENDER VARCHAR(6) NOT NULL CONSTRAINT GENDER_CHECK CHECK (GENDER IN ('MALE','FEMALE'))," +
                    "BIRTH_DATE DATE NOT NULL" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create EMPLOYEE table", ex);
        }
    }

}
