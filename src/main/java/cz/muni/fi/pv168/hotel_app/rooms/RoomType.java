package cz.muni.fi.pv168.hotel_app.rooms;

public class RoomType {
    private final int pricePerNight;
    private final BedType bedType;
    private final int numOfBeds;

    public RoomType(int pricePerNight, BedType bedType, int numOfBeds) {
        this.pricePerNight = pricePerNight;
        this.bedType = bedType;
        this.numOfBeds = numOfBeds;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public BedType getBedType() {
        return bedType;
    }

    public int getNumOfBeds() {
        return numOfBeds;
    }
}
