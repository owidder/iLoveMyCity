package hackthedrive.bmw.de.hackthedrive.domain;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dst on 10.01.2015.
 */
public class Route {

    private String name;
    private String description;

    private Location start;
    private Location end;
    private List<Location> viaPoints = new ArrayList<Location>();

    private List<Image> images = new ArrayList<Image>();

    public Route() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public List<Location> getViaPoints() {
        return viaPoints;
    }

    public void setViaPoints(List<Location> viaPoints) {
        this.viaPoints = viaPoints;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
