package cz.muni.fi.pv168.hotel.rooms;

import cz.muni.fi.pv168.hotel.reservations.Reservation;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomDaoTest {
    private static EmbeddedDataSource dataSource;
    private RoomDao roomDao;

    @BeforeAll
    static void initTestDataSource() {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:hotel-app-test");
        dataSource.setCreateDatabase("create");
    }

    @BeforeEach
    void createReservationDao() throws SQLException {
        roomDao = new RoomDao(dataSource);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.ROOM");
        }
    }

    @AfterEach
    void cleanUp() {
        roomDao.dropTable();
    }

//    @Test
//    void getRoom(){
//        var testRoom = roomDao.getRoom(5);
//        assertThat(testRoom)
//                .isNotNull();
//        assertThat(testRoom.toString()).contains(roomDao.toString());
//    }
    @Test
    void createRooms(){
        assertThat(roomDao.numberOfRooms()).isEqualTo(20);
    }


}
