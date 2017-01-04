package mountainq.organization.dongguk.hanbokapp.datas;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class BookMark {

    private int primeKey;
    private String locationName;
    private double lat;
    private double lon;


    public BookMark(int primeKey, String locationName, double lat, double lon) {
        this.primeKey = primeKey;
        this.locationName = locationName;
        this.lat = lat;
        this.lon = lon;

    }

    public BookMark(String primeKey, String locationName, String lat, String lon) {
        this.primeKey = Integer.parseInt(primeKey);
        this.locationName = locationName;
        this.lat = Double.parseDouble(lat);
        this.lon = Double.parseDouble(lon);

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
