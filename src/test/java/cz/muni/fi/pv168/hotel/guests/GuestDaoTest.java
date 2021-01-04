package cz.muni.fi.pv168.hotel.guests;

import cz.muni.fi.pv168.hotel.DataAccessException;
import cz.muni.fi.pv168.hotel.reservations.Reservation;
import cz.muni.fi.pv168.hotel.reservations.ReservationDao;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;



public class GuestDaoTest {
    private static EmbeddedDataSource dataSource;
    private GuestDao guestDao;
    private static ReservationDao reservationDao;
    private static Reservation reservation;

    @BeforeAll
    static void initTestDataSource() {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:hotel-app-test");
        dataSource.setCreateDatabase("create");
        reservationDao = new ReservationDao(dataSource);
        reservation = new Reservation("name", "345234234",null,4,new Integer[]{1,2,3}, LocalDate.now(), LocalDate.now().plusDays(3), "PLANNED");
        reservationDao.create(reservation);
    }

    @BeforeEach
    void createGuestDao() throws SQLException {
        guestDao = new GuestDao(dataSource);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.GUEST");
        }
    }

    @AfterEach
    void cleanUp() { guestDao.dropTable(); }

    @Test
    void createGuest() {
        var testGuest = new Guest("Tester Smith",LocalDate.parse("1997-10-22") , "1111", reservation.getId());
        guestDao.create(testGuest);

        assertThat(testGuest.getId())
                .isNotNull();
        assertThat(guestDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactly(testGuest);
    }

    @Test
    void findAllEmpty() {
        assertThat(guestDao.findAll())
                .isEmpty();
    }

    @Test
    void findAll() {
        var guest1 = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", reservation.getId());
        var guest2 = new Guest("Testerina Hero", LocalDate.parse("1998-10-25") , "1112", reservation.getId());
        var guest3 = new Guest("Tester Smith", LocalDate.parse("1995-12-28") , "1113", reservation.getId());
        var guest4 = new Guest("Tester J. Morgan", LocalDate.parse("1989-10-13") , "1114", reservation.getId());
        var guest5 = new Guest("Test R. Boy", LocalDate.parse("1991-04-05") , "1115", reservation.getId());
        var guest6 = new Guest("T. S. Ter", LocalDate.parse("1999-11-23") , "1116", reservation.getId());

        guestDao.create(guest1);
        guestDao.create(guest2);
        guestDao.create(guest3);
        guestDao.create(guest4);
        guestDao.create(guest5);
        guestDao.create(guest6);

        assertThat(guestDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(guest1, guest2, guest3, guest4, guest5, guest6);
    }

    @Test
    void delete() {
        var guest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", reservation.getId());

        guestDao.create(guest);
        guestDao.delete(guest);

        assertThat(guestDao.findAll())
                .isEmpty();
    }
    @Test
    void deleteNonExisting() {
        var guest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", reservation.getId());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> guestDao.delete(guest))
                .withMessage("Guest has null ID");
    }

    @Test
    void update() {
        var updatedGuest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", reservation.getId());

        guestDao.create(updatedGuest);

        updatedGuest.setBirthDate(LocalDate.parse("1999-03-11"));
        updatedGuest.setName("Ben Dover");
        updatedGuest.setGuestId("22222");

        guestDao.update(updatedGuest);
        assertThat(guestDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactly(updatedGuest);
    }

    @Test
    void updateNonExisting() {
        var guest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", reservation.getId());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> guestDao.update(guest))
                .withMessage("Guest has null ID");
    }

}
