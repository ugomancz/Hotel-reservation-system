package cz.muni.fi.pv168.hotel_app.rooms;

public class RoomType {
    private int pricePerNight;
    private BedType bedType;
    private int numOfBeds;

    public RoomType(int pricePerNight, BedType bedType, int numOfBeds) {
        this.pricePerNight = pricePerNight;
        this.bedType = bedType;
        this.numOfBeds = numOfBeds;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    public int getNumOfBeds() {
        return numOfBeds;
    }

    public void setNumOfBeds(int numOfBeds) {
        this.numOfBeds = numOfBeds;
    }
}
