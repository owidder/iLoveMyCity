package hackthedrive.bmw.de.hackthedrive.domain;

/**
 * Created by oliverwidder on 10/01/15.
 */
public class Vehicle {

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    private Double lat;

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    private Double lng;
}
