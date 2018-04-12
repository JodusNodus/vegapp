package be.tabtabstudio.veganapp.data.entities;

public class Location {
    public double lat;
    public double lng;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location loc = ((Location) obj);
            return loc.lat == lat && loc.lng == lng;
        } else {
            return false;
        }
    }
}
