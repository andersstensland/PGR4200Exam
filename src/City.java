public class City {
    String name;
    public double latitude;

    public double longitude;

    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return name + " (" + latitude + ") "+" (" + longitude + ")";
    }
}
