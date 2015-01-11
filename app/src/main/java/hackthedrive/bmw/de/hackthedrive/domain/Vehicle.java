package hackthedrive.bmw.de.hackthedrive.domain;

/**
 * Created by oliverwidder on 10/01/15.
 */
public class Vehicle {
    private Double lat;
    private Double lng;
    private Double heading;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }
}
