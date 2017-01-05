package mountainq.organization.dongguk.hanbokapp.datas;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class LocationItem extends HanbokMapPoint {

    /**
     * X좌표 : Longitude
     * Y좌표 : Latitude
     */
    private String firstImgUrl;
    private String phoneNumber;
    private String address;

    public LocationItem() {
        setLongitude(0.0f);
        setLatitude(0.0f);
        setTitle("");
        firstImgUrl = "";
        phoneNumber = "";
        address = "";
    }

    public LocationItem(double longitude, double latitude, String title, String firstImgUrl) {
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);
        this.firstImgUrl = firstImgUrl;
    }

    public LocationItem(String longitude, String latitude, String title, String firstImgUrl) {
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);
        this.firstImgUrl = firstImgUrl;
    }

    public LocationItem(double longitude, double latitude, String title, String firstImgUrl, String phoneNumber) {
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);
        this.firstImgUrl = firstImgUrl;
        this.phoneNumber = phoneNumber;
    }

    public LocationItem(String longitude, String latitude, String title, String firstImgUrl, String phoneNumber) {
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);
        this.firstImgUrl = firstImgUrl;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 파싱 로그용
     * @return
     */
    @Override
    public String toString() {
        return "LocationItem{" +
                "mapX=" + getLongitude() +
                ", mapY=" + getLatitude() +
                ", locationName='" + getTitle() + '\'' +
                ", firstImgUrl='" + firstImgUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
