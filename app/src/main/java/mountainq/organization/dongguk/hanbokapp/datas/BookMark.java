package mountainq.organization.dongguk.hanbokapp.datas;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class BookMark extends HanbokMapPoint {

    private int primeKey;

    public BookMark(int primeKey, String title, double latitude, double longitude) {
        this.primeKey = primeKey;
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);
    }

    public BookMark(String primeKey, String title, String latitude, String longitude) {
        this.primeKey = Integer.parseInt(primeKey);
        setLongitude(longitude);
        setLatitude(latitude);
        setTitle(title);

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
