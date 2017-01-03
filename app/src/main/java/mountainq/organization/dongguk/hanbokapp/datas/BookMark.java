package mountainq.organization.dongguk.hanbokapp.datas;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class BookMark {

    private int primeKey;
    private String locationName;
    private double lon;
    private double lat;

    public BookMark(int primeKey, String locationName, double lon, double lat) {
        this.primeKey = primeKey;
        this.locationName = locationName;
        this.lon = lon;
        this.lat = lat;
    }

    public BookMark(String primeKey, String locationName, String lon, String lat) {
        this.primeKey = Integer.parseInt(primeKey);
        this.locationName = locationName;
        this.lon = Double.parseDouble(lon);
        this.lat = Double.parseDouble(lat);
    }

    public int getPrimeKey() {
        return primeKey;
    }

    public void setPrimeKey(int primeKey) {
        this.primeKey = primeKey;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
