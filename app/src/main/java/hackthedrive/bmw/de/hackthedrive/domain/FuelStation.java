package hackthedrive.bmw.de.hackthedrive.domain;

import android.location.Location;

/**
 * Created by dst on 11.01.2015.
 */
public class FuelStation {
    private Location location;
    private boolean electro = false;

    public FuelStation(Location location, boolean electro) {
        this.location = location;
        this.electro = electro;
    }
    public FuelStation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isElectro() {
        return electro;
    }
}
