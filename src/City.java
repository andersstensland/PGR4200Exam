public class City {
    String name;
    public double latitude;

    public double longitude;

    public String country;

    public City(String name, double latitude, double longitude, String country) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }

    @Override
    public String toString() {
        return name + " (" + latitude + ") "+" (" + longitude + ") "+" (" + country + ")";
    }
}
