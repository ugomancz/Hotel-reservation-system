package cz.muni.fi.pv168.hotel.rooms;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomDaoTest {

    private static RoomDao roomDao;

    @BeforeAll
    static void initTestDataSource() {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:hotel-app-test");
        dataSource.setCreateDatabase("create");
        roomDao = new RoomDao(dataSource);
    }

    @Test
    void createRooms() {
        assertThat(roomDao.numberOfRooms()).isEqualTo(14);
    }

    @Test
    void allRooms() {
        assertThat(roomDao.findAll()).contains(
                new Room(101, RoomDao.RoomPriceCategory.SINGLE_ROOM, 1, 0),
                new Room(102, RoomDao.RoomPriceCategory.SINGLE_ROOM, 1, 0),
                new Room(103, RoomDao.RoomPriceCategory.SINGLE_ROOM, 1, 0),
                new Room(104, RoomDao.RoomPriceCategory.SINGLE_ROOM, 1, 0),
                new Room(105, RoomDao.RoomPriceCategory.DOUBLE_ROOM, 2, 0),
                new Room(106, RoomDao.RoomPriceCategory.DOUBLE_ROOM, 2, 0),
                new Room(201, RoomDao.RoomPriceCategory.DOUBLE_ROOM, 0, 1),
                new Room(202, RoomDao.RoomPriceCategory.DOUBLE_ROOM, 0, 1),
                new Room(203, RoomDao.RoomPriceCategory.DOUBLE_ROOM, 0, 1),
                new Room(204, RoomDao.RoomPriceCategory.TRIPLE_ROOM, 1, 1),
                new Room(205, RoomDao.RoomPriceCategory.TRIPLE_ROOM, 1, 1),
                new Room(301, RoomDao.RoomPriceCategory.APARTMENT, 2, 1),
                new Room(302, RoomDao.RoomPriceCategory.APARTMENT, 2, 1),
                new Room(303, RoomDao.RoomPriceCategory.APARTMENT, 2, 1)
        );
    }
}
