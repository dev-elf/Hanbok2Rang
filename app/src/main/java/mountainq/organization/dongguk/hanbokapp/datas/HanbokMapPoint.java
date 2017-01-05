package mountainq.organization.dongguk.hanbokapp.datas;

/**
 * Created by dnay2 on 2017-01-05.
 */

public class HanbokMapPoint {

    private double longitude;
    private double latitude;
    private String title;




    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String locationName) {
        this.title = locationName;
    }
}
