package cz.muni.fi.pv168.hotel.rooms;

import cz.muni.fi.pv168.hotel.DataAccessException;
import cz.muni.fi.pv168.hotel.reservations.Reservation;

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
            // one-bed rooms
            Map.entry(1, new Room(1, 1500, 1, 0)),
            Map.entry(2, new Room(2, 1500, 1, 0)),
            Map.entry(3, new Room(3, 1500, 1, 0)),
            Map.entry(4, new Room(4, 1500, 1, 0)),
            Map.entry(5, new Room(5, 1500, 1, 0)),
            // two-bed rooms
            Map.entry(6, new Room(6, 2800, 2, 0)),
            Map.entry(7, new Room(7, 2900, 0, 1)),
            Map.entry(8, new Room(8, 2800, 0, 1)),
            Map.entry(9, new Room(9, 2800, 0, 1)),
            Map.entry(10, new Room(10, 2900, 2, 0)),
            Map.entry(11, new Room(11, 2800, 2, 0)),
            Map.entry(12, new Room(12, 2850, 0, 1)),
            Map.entry(13, new Room(13, 2800, 2, 0)),
            // three-bed rooms
            Map.entry(14, new Room(14, 4200, 3, 0)),
            Map.entry(15, new Room(15, 4200, 1, 1)),
            Map.entry(16, new Room(16, 4200, 3, 0)),
            Map.entry(17, new Room(17, 4200, 1, 1)),
            Map.entry(18, new Room(18, 4200, 3, 0)),
            Map.entry(19, new Room(19, 4200, 1, 1)),
            Map.entry(20, new Room(20, 4200, 3, 0))
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
             var st = connection.prepareStatement("SELECT ROOMNUMBER, PRICE, STANDARD, KINGSIZE " +
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

    public void printAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ROOMNUMBER, PRICE, STANDARD, KINGSIZE FROM ROOM")) {
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt("ROOMNUMBER") + " " +
                            rs.getInt("PRICE") + " " +
                            rs.getInt("STANDARD") + " " +
                            rs.getInt("KINGSIZE"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public void printAll(List<Room> rooms) {
        for (Room room :
                rooms) {
            System.out.printf("Room number %s is free%n", room.getRoomNumber());
        }
    }

    private void create(Room room) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO ROOM (ROOMNUMBER, PRICE, STANDARD, KINGSIZE) VALUES (?, ?, ?, ?)",
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
        st.setInt(2, room.getPricePerNight());
        st.setInt(3, room.getStandardBeds());
        st.setInt(4, room.getKingsizeBeds());
    }

    public List<Room> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ROOMNUMBER, PRICE, STANDARD, KINGSIZE FROM ROOM")) {
            return createRooms(st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all rooms", ex);
        }
    }

    private List<Room> createRooms(PreparedStatement st) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        try (var rs = st.executeQuery()) {
            while (rs.next()) {
                Room room = new Room(rs.getInt("ROOMNUMBER"),rs.getInt("PRICE"),
                        rs.getInt("KINGSIZE"),rs.getInt("STANDARD"));
                rooms.add(room);
            }
        }
        return rooms;
    }

    private Room createRoom(PreparedStatement st) throws SQLException {

        try (var rs = st.executeQuery()) {
            if (rs.next()) {
                return new Room(rs.getInt("ROOMNUMBER"), rs.getInt("PRICE"), rs.getInt("STANDARD"), rs.getInt("KINGSIZE"));
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
                    "PRICE INT," +
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
}
