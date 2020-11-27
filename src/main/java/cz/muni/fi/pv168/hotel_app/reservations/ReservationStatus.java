package cz.muni.fi.pv168.hotel_app.reservations;

import java.awt.*;

public enum ReservationStatus {
    PLANNED {
        @Override
        public Color getColor() {
            return Color.green;
        }
    },
    ONGOING {
        @Override
        public Color getColor() {
            return Color.orange;
        }
    },
    PAST {
        @Override
        public Color getColor() {
            return Color.lightGray;
        }
    };

    public abstract Color getColor();
}
