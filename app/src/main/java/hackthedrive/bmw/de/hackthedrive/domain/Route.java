package hackthedrive.bmw.de.hackthedrive.domain;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dst on 10.01.2015.
 */
public class Route implements Serializable {

    private Long id;

    private String name;
    private String description;

    private Location start;
    private String startAddress;

    private Location end;
    private String endAddress;

    private List<Location> viaPoints = new ArrayList<Location>();

    private List<Poi> pois = new ArrayList<>();

    private int distanceInMi;
    private int costInDollar;

    private float rating;

    private List<Bitmap> images = new ArrayList<Bitmap>();

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

    public List<Bitmap> getImages() {
        return images;
    }

    public void addImage(Bitmap image){
        this.images.add(image);
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistanceInMi() {
        return distanceInMi;
    }

    public void setDistanceInMi(int distanceInMi) {
        this.distanceInMi = distanceInMi;
    }

    public int getCostInDollar() {
        return costInDollar;
    }

    public void setCostInDollar(int costInDollar) {
        this.costInDollar = costInDollar;
    }

    public void addPoi(Poi poi){
        this.pois.add(poi);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addViaPoint(Location location){
        this.viaPoints.add(location);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Route{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", viaPoints=").append(viaPoints);
        sb.append(", images=").append(images);
        sb.append('}');
        return sb.toString();
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
