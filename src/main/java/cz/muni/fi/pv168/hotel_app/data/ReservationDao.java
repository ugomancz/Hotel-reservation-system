package cz.muni.fi.pv168.hotel_app.data;


import javax.sql.DataSource;


/**
 * @author Denis Kollar
 */
public final class ReservationDao {
    private final DataSource dataSource;

    public ReservationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
