package cz.muni.fi.pv168.hotel_app.data;

import cz.muni.fi.pv168.hotel_app.reservations.Reservation;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Lukas Hasik
 */
public class ReservationDaoTest {
    private static EmbeddedDataSource dataSource;
    private ReservationDao reservationDao;

    @BeforeAll
    static void initTestDataSource() {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:hotel-app-test");
        dataSource.setCreateDatabase("create");
    }

    @BeforeEach
    void createEmployeeDao() throws SQLException {
        reservationDao = new ReservationDao(dataSource);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.RESERVATION");
        }
    }

    @AfterEach
    void cleanUp() {
        reservationDao.dropTable();
    }

    @Test
    void createReservation() {
        var testRes = new Reservation("Tester Smith", "777777777", "tester@test.com", 4, 2,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");
        reservationDao.create(testRes);

        assertThat(testRes.getId())
                .isNotNull();
        assertThat(reservationDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactly(testRes);
    }











}
