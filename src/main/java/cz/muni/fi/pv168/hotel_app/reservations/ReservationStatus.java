package cz.muni.fi.pv168.hotel_app.reservations;

import cz.muni.fi.pv168.hotel_app.Constants;

import java.awt.*;

public enum ReservationStatus {
    PLANNED {
        @Override
        public Color getColor() {
            return Constants.PLANNED_RESERVATION;
        }
    },
    ONGOING {
        @Override
        public Color getColor() {
            return Constants.ONGOING_RESERVATION;
        }
    },
    PAST {
        @Override
        public Color getColor() {
            return Constants.PAST_RESERVATION;
        }
    };

    public abstract Color getColor();
}
