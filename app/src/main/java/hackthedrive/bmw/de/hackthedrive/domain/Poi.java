package hackthedrive.bmw.de.hackthedrive.domain;

import android.graphics.Bitmap;
import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dst on 10.01.2015.
 */
public class Poi implements Serializable {

    private String name;
    private String desc;
    private List<Bitmap> images = new ArrayList<>();

    private Location location;
    private long radius;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getRadius() {
        return radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }

    public void addImage(Bitmap image){
        this.images.add(image);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Poi{");
        sb.append("name='").append(name).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", location=").append(location);
        sb.append(", radius=").append(radius);
        sb.append('}');
        return sb.toString();
    }
}
