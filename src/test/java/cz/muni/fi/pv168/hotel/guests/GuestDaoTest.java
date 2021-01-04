package cz.muni.fi.pv168.hotel.guests;

import cz.muni.fi.pv168.hotel.DataAccessException;
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

    @BeforeAll
    static void initTestDataSource() {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:hotel-app-test");
        dataSource.setCreateDatabase("create");
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
        var testGuest = new Guest("Tester Smith",LocalDate.parse("1997-10-22") , "1111", 123456L);
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
        var guest1 = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", 123456L);
        var guest2 = new Guest("Testerina Hero", LocalDate.parse("1998-10-25") , "1112", 123457L);
        var guest3 = new Guest("Tester Smith", LocalDate.parse("1995-12-28") , "1113", 123458L);
        var guest4 = new Guest("Tester J. Morgan", LocalDate.parse("1989-10-13") , "1114", 123459L);
        var guest5 = new Guest("Test R. Boy", LocalDate.parse("1991-04-05") , "1115", 123460L);
        var guest6 = new Guest("T. S. Ter", LocalDate.parse("1999-11-23") , "1116", 123461L);

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
        var guest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", 123456L);

        guestDao.create(guest);
        guestDao.delete(guest);

        assertThat(guestDao.findAll())
                .isEmpty();
    }
    @Test
    void deleteNonExisting() {
        var guest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", 123456L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> guestDao.delete(guest))
                .withMessage("Failed to delete non-existing guest: %s", guest);
    }

    @Test
    void update() {
        var updatedGuest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", 123456L);

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
        var guest = new Guest("Tester Smith", LocalDate.parse("1997-10-22") , "1111", 123456L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> guestDao.update(guest))
                .withMessage("Failed to update non-existing guest: %s", guest);
    }

}
