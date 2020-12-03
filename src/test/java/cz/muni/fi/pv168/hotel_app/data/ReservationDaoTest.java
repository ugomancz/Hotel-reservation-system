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

    @Test
    void findAllEmpty() {
        assertThat(reservationDao.findAll())
                .isEmpty();
    }

    @Test
    void findAll() {
        var testRes1 = new Reservation("Tester Smith", "777777777", "tester@test.com", 4, 2,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");
        var testRes2 = new Reservation("Testerina Hero", "77769777", "testerina@hero.com", 2, 1,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");
        var testRes3 = new Reservation("Tester Jester", "778654777", "tester@test.com", 1, 3,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");
        var testRes4 = new Reservation("Tester J. Morgan", "771237777", "tmorg@test.com", 4, 5,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");
        var testRes5 = new Reservation("Test R. Boy", "777333477", "tester@test.com", 5, 8,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");
        var testRes6 = new Reservation("T. S. Ter", "420777769", "tester@test.com", 4, 10,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");


        reservationDao.create(testRes1);
        reservationDao.create(testRes2);
        reservationDao.create(testRes3);
        reservationDao.create(testRes4);
        reservationDao.create(testRes5);
        reservationDao.create(testRes6);

        assertThat(reservationDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(testRes1, testRes2, testRes3, testRes4, testRes5, testRes6);
    }


    @Test
    void delete() {
        var res = new Reservation("Test R. Boy", "777333477", "tester@test.com", 5, 8,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");

        reservationDao.create(res);
        reservationDao.delete(res);

        assertThat(reservationDao.findAll())
                .isEmpty();
    }

    @Test
    void update() {
        var updatedRes = new Reservation("Test R. Boy", "777333477", "tester@test.com", 5, 8,
                LocalDate.now(), LocalDate.now().plusDays(4), "PLANNED");

        reservationDao.create(updatedRes);

        updatedRes.setArrival(LocalDate.now().plusDays(4));
        updatedRes.setName("Ben Dover");
        updatedRes.setRoomNumber(6);

        reservationDao.update(updatedRes);
        assertThat(reservationDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactly(updatedRes);
    }














}
