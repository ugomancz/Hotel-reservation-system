package cz.muni.fi.pv168.hotel.reservations;

import cz.muni.fi.pv168.hotel.DataAccessException;
import cz.muni.fi.pv168.hotel.rooms.Room;
import cz.muni.fi.pv168.hotel.rooms.RoomDao;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


/**
 * @author Denis Kollar
 */
@SuppressWarnings("SqlNoDataSourceInspection")
public final class ReservationDao {

    private final DataSource dataSource;
    private final ReservedRoom reservedRoom;
    private final RoomDao roomDao;

    public ReservationDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.roomDao = new RoomDao(dataSource);
        if (!tableExists()) {
            createTable();
        }
        this.reservedRoom = new ReservedRoom();
    }

    private boolean tableExists() {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, "APP", "RESERVATION", null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + "APP" + "." + "RESERVATION" + " exists", ex);
        }
    }

    public void create(Reservation reservation) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO RESERVATION (NAME, PHONE, EMAIL, HOSTS, ARRIVAL, DEPARTURE, STATUS, GUESTID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            setRows(reservation, st);
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setId(rs.getLong(1));
                    for (Integer roomNumber : reservation.getRoomNumbers()) {
                        reservedRoom.create(rs.getLong(1), roomNumber, roomDao.getPricePerNight(roomNumber));
                    }
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for reservation: " + reservation);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store reservation " + reservation, ex);
        }
    }

    public void delete(Reservation reservation) {
        if (reservation.getId() == null) {
            throw new IllegalArgumentException("Reservation has null ID");
        }
        reservedRoom.delete(reservation.getId());
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM RESERVATION WHERE ID = ?")) {
            st.setLong(1, reservation.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing reservation: " + reservation);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete reservation ", ex);
        }
    }

    public void update(Reservation reservation) {
        if (reservation.getId() == null) {
            throw new IllegalArgumentException("Reservation has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE RESERVATION SET NAME = ?, PHONE = ?, EMAIL = ?, HOSTS = ?,"
                             + " ARRIVAL = ?, DEPARTURE = ?, STATUS = ?, GUESTID = ? WHERE ID = ?")) {
            setRows(reservation, st);
            st.setLong(9, reservation.getId());
            reservedRoom.update(reservation);
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing reservation: " + reservation);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update reservation " + reservation, ex);
        }
    }

    public void updatePrice(long reservationId, int roomNumber, int price) {
        reservedRoom.updatePrice(reservationId, roomNumber, price);
    }

    public Integer getOldPrice(long reservationId, int roomNumber) {
        return reservedRoom.getPrice(reservationId, roomNumber);
    }

    private void setRows(Reservation reservation, PreparedStatement st) throws SQLException {
        st.setString(1, reservation.getName());
        st.setString(2, reservation.getPhone());
        st.setString(3, reservation.getEmail());
        st.setInt(4, reservation.getGuests());
        st.setDate(5, Date.valueOf(reservation.getArrival()));
        st.setDate(6, Date.valueOf(reservation.getDeparture()));
        st.setString(7, reservation.getStatus().name());
        st.setString(8, reservation.getGuestID());
    }

    public List<Reservation> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, PHONE, EMAIL, HOSTS," +
                     " ARRIVAL, DEPARTURE, STATUS, GUESTID FROM RESERVATION")) {
            return createReservation(st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public List<Reservation> getReservation(int room, LocalDate date) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, PHONE, EMAIL, HOSTS,"
                     + " ARRIVAL, DEPARTURE, STATUS, GUESTID FROM" +
                     " RESERVATION INNER JOIN RESERVEDROOM ON RESERVATION.ID = RESERVEDROOM.RESERVATIONID " +
                     "WHERE (RESERVEDROOM.ROOMID = ?) AND ((ARRIVAL<=? AND ?<=DEPARTURE))")) {
            st.setInt(1, room);
            st.setDate(2, Date.valueOf(date));
            st.setDate(3, Date.valueOf(date));
            return createReservation(st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    private List<Reservation> createReservation(PreparedStatement st) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (var rs = st.executeQuery()) {
            while (rs.next()) {
                Reservation reservation = new Reservation(rs.getString("NAME"), rs.getString("PHONE"),
                        rs.getString("EMAIL"), rs.getInt("HOSTS"), getReservationRoomNumbers(rs.getLong("ID")),
                        rs.getDate("ARRIVAL").toLocalDate(), rs.getDate("DEPARTURE").toLocalDate(),
                        rs.getString("STATUS"));
                reservation.setId(rs.getLong("ID"));
                reservation.setGuestID(rs.getString("GUESTID"));
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public Integer[] getReservationRoomNumbers(Long reservationId) {
        List<Integer> roomNumbers = new ArrayList<>();
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ROOMID FROM RESERVEDROOM " +
                     "WHERE RESERVATIONID = ?")) {
            st.setLong(1, reservationId);
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    roomNumbers.add(rs.getInt("ROOMID"));
                }
            }
            Integer[] tmp = new Integer[roomNumbers.size()];
            return roomNumbers.toArray(tmp);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public HashMap<Integer, Integer> getReservedRoomsPrice(long reservationId) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (Integer roomNumber : getReservationRoomNumbers(reservationId)) {
            hashMap.put(roomNumber, getOldPrice(reservationId, roomNumber));
        }
        return hashMap;
    }

    public int getNumOfReservations(LocalDate date) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT ROOMID FROM RESERVEDROOM WHERE RESERVATIONID IN (SELECT ID FROM RESERVATION WHERE STATUS<>? AND ((ARRIVAL<=? AND ?<=DEPARTURE)))")) {
            st.setString(1, "PAST");
            return collectRoomNumbers(date, date, st).size();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    private HashSet<Integer> collectRoomNumbers(LocalDate arrival, LocalDate departure, PreparedStatement st) throws SQLException {
        st.setDate(2, Date.valueOf(arrival));
        st.setDate(3, Date.valueOf(departure));
        HashSet<Integer> hashSet = new HashSet<>();
        try (var rs = st.executeQuery()) {

            while (rs.next()) {
                hashSet.add(rs.getInt("ROOMID"));
            }
        }
        return hashSet;
    }

    public List<Room> getFreeRooms(LocalDate arrival, LocalDate departure, RoomDao roomDao) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT ROOMID FROM RESERVEDROOM WHERE RESERVATIONID IN (SELECT ID FROM RESERVATION WHERE STATUS<>? AND ((DEPARTURE>? AND ARRIVAL<?)))")) {
            st.setString(1, ReservationStatus.PAST.name());
            return collectFreeRooms(arrival, departure, roomDao, st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public List<Room> getFreeRooms(LocalDate arrival, LocalDate departure, RoomDao roomDao, long reservationId) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT ROOMID FROM RESERVEDROOM WHERE RESERVATIONID IN (SELECT ID FROM RESERVATION WHERE STATUS<>? AND ((DEPARTURE>? AND ARRIVAL<?)) AND ID<>?)")) {
            st.setString(1, ReservationStatus.PAST.name());
            st.setLong(4, reservationId);
            return collectFreeRooms(arrival, departure, roomDao, st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    public void updateRoomNumber(long reservationId, int oldNumber, int newNumber) {
        reservedRoom.updateRoomNumber(reservationId, oldNumber, newNumber);
    }

    private List<Room> collectFreeRooms(LocalDate arrival, LocalDate departure, RoomDao roomDao, PreparedStatement st) throws SQLException {
        HashSet<Integer> hashSet = collectRoomNumbers(arrival, departure, st);
        List<Room> emptyRooms = roomDao.findAll();
        emptyRooms.removeIf(room -> hashSet.contains(room.getRoomNumber()));
        return emptyRooms;
    }

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.RESERVATION (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "PHONE VARCHAR(100) NOT NULL," +
                    "EMAIL VARCHAR(100)," +
                    "HOSTS INT," +
                    "ARRIVAL DATE NOT NULL," +
                    "DEPARTURE DATE NOT NULL," +
                    "STATUS VARCHAR(100) NOT NULL," +
                    "GUESTID VARCHAR(100))");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create RESERVATION table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE APP.RESERVATION");
            reservedRoom.dropTable();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop RESERVATION table", ex);
        }
    }

    private class ReservedRoom {

        private ReservedRoom() {
            if (!tableExists()) {
                createTable();
            }
        }

        private boolean tableExists() {
            try (var connection = dataSource.getConnection();
                 var rs = connection.getMetaData().getTables(null, "APP", "RESERVEDROOM", null)) {
                return rs.next();
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to detect if the table " + "APP" + "." + "RESERVEDROOM" + " exists", ex);
            }
        }

        public void create(long reservationId, int roomNumber, int pricePerNight) {
            try (var connection = dataSource.getConnection();
                 var st = connection.prepareStatement(
                         "INSERT INTO RESERVEDROOM (RESERVATIONID, ROOMID, PRICEPERNIGHT) VALUES (?, ?, ?)")) {
                st.setLong(1, reservationId);
                st.setInt(2, roomNumber);
                st.setInt(3, pricePerNight);
                st.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to store reservedRoom, reservationId: " + reservationId +
                        " roomNUmber: " + roomNumber +
                        " pricePerNight: " + pricePerNight
                        , ex);
            }
        }

        public void updatePrice(long reservationId, int roomNumber, int pricePerNight) {
            try (var connection = dataSource.getConnection();
                 var st = connection.prepareStatement(
                         "UPDATE RESERVEDROOM SET PRICEPERNIGHT = ? WHERE RESERVATIONID = ? AND ROOMID = ?")) {
                st.setInt(1, pricePerNight);
                st.setLong(2, reservationId);
                st.setInt(3, roomNumber);
                int rowsUpdated = st.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new DataAccessException("Failed to update non-existing " +
                            "reserved room for reservation: "
                            + reservationId + " and room: " + roomNumber);
                }
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to update reserved room, reservation: "
                        + reservationId + " and room: " + roomNumber, ex);
            }
        }

        private void updateRoomNumber(long reservationId, int oldRoom, int newRoom) {
            try (var connection = dataSource.getConnection();
                 var st = connection.prepareStatement(
                         "UPDATE RESERVEDROOM SET ROOMID = ? WHERE RESERVATIONID = ? AND ROOMID = ?")) {
                st.setInt(1, newRoom);
                st.setLong(2, reservationId);
                st.setInt(3, oldRoom);
                int rowsUpdated = st.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new DataAccessException("Failed to update non-existing " +
                            "reserved room for reservation: "
                            + reservationId + " and room: " + oldRoom);
                }
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to update reserved room, reservation: "
                        + reservationId + " and room: " + oldRoom, ex);
            }
        }

        public Integer getPrice(long reservationId, int roomNumber) {
            try (var connection = dataSource.getConnection();
                 var st = connection.prepareStatement(
                         "SELECT PRICEPERNIGHT FROM RESERVEDROOM WHERE RESERVATIONID = ? AND ROOMID = ?")) {
                st.setLong(1, reservationId);
                st.setInt(2, roomNumber);
                int price = -1;
                try (var rs = st.executeQuery()) {

                    if (rs.next()) {
                        price = rs.getInt("PRICEPERNIGHT");
                    }
                }
                return price;
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to get price for room " + roomNumber + ", reservation: "
                        + reservationId, ex);
            }
        }

        public void update(Reservation reservation) {
            List<Integer> roomNumbers = getRoomNumbers(reservation.getId());
            var deletedRooms = getRoomsForDeletion(roomNumbers, new ArrayList<>(Arrays.asList(reservation.getRoomNumbers())));
            for (Integer room : deletedRooms) {
                delete(reservation.getId(), room);
            }
            var newRooms = getNewRooms(roomNumbers, new ArrayList<>(Arrays.asList(reservation.getRoomNumbers())));
            for (Integer room : newRooms) {
                create(reservation.getId(), room, roomDao.getPricePerNight(room));
            }
        }

        private List<Integer> getNewRooms(List<Integer> roomNumbers, ArrayList<Integer> newNumbers) {
            var newRooms = new ArrayList<Integer>();
            for (Integer number : newNumbers) {
                if (!roomNumbers.contains(number)) {
                    newRooms.add(number);
                }
            }
            return newRooms;
        }

        private List<Integer> getRoomsForDeletion(List<Integer> roomNumbers, List<Integer> newNumbers) {
            var forDeletion = new ArrayList<Integer>();
            for (Integer number : roomNumbers) {
                if (!newNumbers.contains(number)) {
                    forDeletion.add(number);
                }
            }
            return forDeletion;
        }

        private List<Integer> getRoomNumbers(long reservationId) {
            List<Integer> list = new ArrayList<>();
            try (var connection = dataSource.getConnection();
                 var st = connection.prepareStatement(
                         "SELECT ROOMID FROM RESERVEDROOM WHERE reservationid = ?")) {
                st.setLong(1, reservationId);
                try (var rs = st.executeQuery()) {

                    while (rs.next()) {
                        list.add(rs.getInt("ROOMID"));
                    }
                }
                return list;
            } catch (SQLException ex) {
                System.out.println("ERROR");
                throw new DataAccessException("Failed to get room numbers for reservation with id " + reservationId, ex);
            }
        }

        public void delete(long reservationId) {
            try (var connection = dataSource.getConnection();
                 var st = connection.prepareStatement("DELETE FROM RESERVEDROOM WHERE RESERVATIONID = ?")) {
                st.setLong(1, reservationId);
                int rowsDeleted = st.executeUpdate();
                if (rowsDeleted == 0) {
                    System.out.println("ERROR");
                    throw new DataAccessException("Failed to delete non-existing reservationId: " + reservationId);
                }
            } catch (SQLException ex) {
                System.out.println("ERROR");
                throw new DataAccessException("Failed to delete from reservedRoom for reservationId " + reservationId, ex);
            }
        }

        public void delete(long reservationId, int roomId) {
            try (var connection = dataSource.getConnection();
                 var st = connection.prepareStatement("DELETE FROM RESERVEDROOM WHERE RESERVATIONID = ? AND ROOMID = ?")) {
                st.setLong(1, reservationId);
                st.setInt(2, roomId);
                int rowsDeleted = st.executeUpdate();
                if (rowsDeleted == 0) {
                    System.out.println("ERROR");
                    throw new DataAccessException("Failed to delete non-existing reservationId: " + reservationId);
                }
            } catch (SQLException ex) {
                System.out.println("ERROR");
                throw new DataAccessException("Failed to delete from reservedRoom for reservationId " + reservationId, ex);
            }
        }

        private void createTable() {
            try (var connection = dataSource.getConnection();
                 var st = connection.createStatement()) {

                st.executeUpdate("CREATE TABLE APP.RESERVEDROOM (" +
                        "RESERVATIONID BIGINT REFERENCES Reservation(ID)," +
                        "ROOMID INT REFERENCES Room(ROOMNUMBER)," +
                        "PRICEPERNIGHT VARCHAR(100) NOT NULL," +
                        "PRIMARY KEY (RESERVATIONID, ROOMID))");
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to create RESERVEDROOM table", ex);
            }
        }

        public void dropTable() {
            try (var connection = dataSource.getConnection();
                 var st = connection.createStatement()) {
                st.executeUpdate("DROP TABLE APP.RESERVATION");
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to drop RESERVEDROOM table", ex);
            }
        }
    }
}
