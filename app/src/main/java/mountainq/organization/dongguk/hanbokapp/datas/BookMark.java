package mountainq.organization.dongguk.hanbokapp.datas;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class BookMark extends HanbokMapPoint {

    private int primeKey;
    private String address;
    private String phone;

    public BookMark(int primeKey, String title, double latitude, double longitude, String address, String phone) {
        this.primeKey = primeKey;
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);
        this.address = address;
        this.phone = phone;
    }

    public BookMark(String primeKey, String title, String latitude, String longitude, String address, String phone) {
        this.primeKey = Integer.parseInt(primeKey);
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPrimeKey() {
        return primeKey;
    }

    public void setPrimeKey(int primeKey) {
        this.primeKey = primeKey;
    }

    @Override
    public String toString() {
        return "BookMark{" +
                "primeKey=" + primeKey +
                ", locationName='" + getTitle() + '\'' +
                '}';
    }
}
