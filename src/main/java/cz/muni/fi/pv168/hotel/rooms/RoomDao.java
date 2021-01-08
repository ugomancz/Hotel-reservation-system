package cz.muni.fi.pv168.hotel.rooms;

import cz.muni.fi.pv168.hotel.Constants;
import cz.muni.fi.pv168.hotel.DataAccessException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public final class RoomDao {

    private final DataSource dataSource;

    private final Map<Integer, Room> rooms = Map.ofEntries(
            // first floor, one bed
            Map.entry(101, new Room(101, RoomPriceCategory.SINGLE_ROOM, 1, 0)),
            Map.entry(102, new Room(102, RoomPriceCategory.SINGLE_ROOM, 1, 0)),
            Map.entry(103, new Room(103, RoomPriceCategory.SINGLE_ROOM, 1, 0)),
            Map.entry(104, new Room(104, RoomPriceCategory.SINGLE_ROOM, 1, 0)),
            // first floor, two separate beds
            Map.entry(105, new Room(105, RoomPriceCategory.DOUBLE_ROOM, 2, 0)),
            Map.entry(106, new Room(106, RoomPriceCategory.DOUBLE_ROOM, 2, 0)),
            // second floor, king-size bed
            Map.entry(201, new Room(201, RoomPriceCategory.DOUBLE_ROOM, 0, 1)),
            Map.entry(202, new Room(202, RoomPriceCategory.DOUBLE_ROOM, 0, 1)),
            Map.entry(203, new Room(203, RoomPriceCategory.DOUBLE_ROOM, 0, 1)),
            // second floor, king-size + one single bed
            Map.entry(204, new Room(204, RoomPriceCategory.TRIPLE_ROOM, 1, 1)),
            Map.entry(205, new Room(205, RoomPriceCategory.TRIPLE_ROOM, 1, 1)),
            // third floor, apartments (king-size + two single beds)
            Map.entry(301, new Room(301, RoomPriceCategory.APARTMENT, 2, 1)),
            Map.entry(302, new Room(302, RoomPriceCategory.APARTMENT, 2, 1)),
            Map.entry(303, new Room(303, RoomPriceCategory.APARTMENT, 2, 1))
    );
    public RoomDao(DataSource dataSource) {
        this.dataSource = dataSource;
        if (!tableExists()) {
            createTable();
            fillTable();
        }
    }

    public Room getRoom(int roomNumber) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ROOMNUMBER, PRICECATEGORY, STANDARD, KINGSIZE " +
                     "FROM ROOM WHERE ROOMNUMBER=?")) {
            st.setInt(1, roomNumber);
            return createRoom(st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find room with number " + roomNumber, ex);
        }
    }

    public int getPricePerNight(int roomNumber) {
        return getRoom(roomNumber).getPricePerNight();
    }

    public int getPricePerNight(Integer[] rooms) {
        int total = 0;
        for (Integer roomNumber : rooms) {
            total += getPricePerNight(roomNumber);
        }
        return total;
    }

    public int numberOfBeds(int roomNumber) {
        Room room = getRoom(roomNumber);
        return room.getKingsizeBeds() * 2 + room.getStandardBeds();
    }

    public int numberOfBeds(Integer[] rooms) {
        int total = 0;
        for (Integer roomNumber : rooms) {
            total += numberOfBeds(roomNumber);
        }
        return total;
    }

    public int numberOfRooms() {
        return rooms.size();
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE APP.ROOM");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop ROOM table", ex);
        }
    }

    public void printAll(List<Room> rooms) {
        for (Room room :
                rooms) {
            System.out.printf("Room number %s is free%n", room.getRoomNumber());
        }
    }

    public List<Room> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ROOMNUMBER, PRICECATEGORY, STANDARD, KINGSIZE FROM ROOM ORDER BY ROOMNUMBER")) {
            return createRooms(st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    private void create(Room room) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO ROOM (ROOMNUMBER, PRICECATEGORY, STANDARD, KINGSIZE) VALUES (?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            setRows(room, st);
            if (st.executeUpdate() == 0) {
                System.out.println("nothing added");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store ROOM " + room, ex);
        }
    }

    private void setRows(Room room, PreparedStatement st) throws SQLException {
        st.setInt(1, room.getRoomNumber());
        st.setString(2, room.getRoomPriceCategory().name());
        st.setInt(3, room.getStandardBeds());
        st.setInt(4, room.getKingsizeBeds());
    }

    private List<Room> createRooms(PreparedStatement st) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        try (var rs = st.executeQuery()) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("ROOMNUMBER"),
                        RoomPriceCategory.valueOf(rs.getString("PRICECATEGORY")),
                        rs.getInt("STANDARD"),
                        rs.getInt("KINGSIZE"));
                rooms.add(room);
            }
        }
        return rooms;
    }

    private Room createRoom(PreparedStatement st) throws SQLException {

        try (var rs = st.executeQuery()) {
            if (rs.next()) {
                return new Room(
                        rs.getInt("ROOMNUMBER"),
                        RoomPriceCategory.valueOf(rs.getString("PRICECATEGORY")),
                        rs.getInt("STANDARD"),
                        rs.getInt("KINGSIZE"));
            } else {
                throw new SQLException();
            }
        }
    }

    private boolean tableExists() {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, "APP", "ROOM", null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + "APP" + "." + "ROOM" + " exists", ex);
        }
    }

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.ROOM (" +
                    "ROOMNUMBER INT PRIMARY KEY," +
                    "PRICECATEGORY VARCHAR(255)," +
                    "STANDARD INT," +
                    "KINGSIZE INT)");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create ROOM table", ex);
        }
    }

    private void fillTable() {
        for (Room room : rooms.values()) {
            create(room);
        }
    }

    public enum RoomPriceCategory {
        SINGLE_ROOM,
        DOUBLE_ROOM,
        TRIPLE_ROOM,
        APARTMENT
    }
}
