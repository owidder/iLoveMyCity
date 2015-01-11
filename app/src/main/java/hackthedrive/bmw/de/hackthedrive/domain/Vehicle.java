package hackthedrive.bmw.de.hackthedrive.domain;

/**
 * Created by oliverwidder on 10/01/15.
 */
public class Vehicle {
    private Double lat;
    private Double lng;
    private Double heading;

    public Boolean getIgnitionOn() {
        return ignitionOn;
    }

    public void setIgnitionOn(Boolean ignitionOn) {
        this.ignitionOn = ignitionOn;
    }

    private Boolean ignitionOn;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    private String vin;

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private Double batteryLevel;
    private int speed;

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
