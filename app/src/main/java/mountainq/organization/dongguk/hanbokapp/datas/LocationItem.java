package mountainq.organization.dongguk.hanbokapp.datas;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class LocationItem {

    /**
     * X좌표 : Longitude
     * Y좌표 : Latitude
     */
    private double mapLon;
    private double mapLat;
    private String locationName;
    private String firstImgUrl;
    private String phoneNumber;

    public LocationItem() {
        mapLon = 0;
        mapLat = 0;
        locationName ="";
        firstImgUrl = "";
        phoneNumber = "";
    }

    public LocationItem(double mapLon, double mapLat, String locationName, String firstImgUrl) {
        this.mapLon = mapLon;
        this.mapLat = mapLat;
        this.locationName = locationName;
        this.firstImgUrl = firstImgUrl;
    }

    public LocationItem(String mapLon, String mapLat, String locationName, String firstImgUrl) {
        this.mapLon = Double.parseDouble(mapLon);
        this.mapLat = Double.parseDouble(mapLat);
        this.locationName = locationName;
        this.firstImgUrl = firstImgUrl;
    }

    public LocationItem(double mapLon, double mapLat, String locationName, String firstImgUrl, String phoneNumber) {
        this.mapLon = mapLon;
        this.mapLat = mapLat;
        this.locationName = locationName;
        this.firstImgUrl = firstImgUrl;
        this.phoneNumber = phoneNumber;
    }

    public LocationItem(String mapLon, String mapLat, String locationName, String firstImgUrl, String phoneNumber) {
        this.mapLon = Double.parseDouble(mapLon);
        this.mapLat = Double.parseDouble(mapLat);
        this.locationName = locationName;
        this.firstImgUrl = firstImgUrl;
    }

    public double getMapLon() {
        return mapLon;
    }

    public void setMapLon(double mapLon) {
        this.mapLon = mapLon;
    }

    public void setMapLon(String mapLon) {
        this.mapLon = Double.parseDouble(mapLon);
    }

    public double getMapLat() {
        return mapLat;
    }

    public void setMapLat(double mapLat) {
        this.mapLat = mapLat;
    }

    public void setMapLat(String mapLat) {
        this.mapLat = Double.parseDouble(mapLat);
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getFirstImgUrl() {
        return firstImgUrl;
    }

    public void setFirstImgUrl(String firstImgUrl) {
        this.firstImgUrl = firstImgUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 파싱 로그용
     * @return
     */
    @Override
    public String toString() {
        return "LocationItem{" +
                "mapX=" + mapLon +
                ", mapY=" + mapLat +
                ", locationName='" + locationName + '\'' +
                ", firstImgUrl='" + firstImgUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
