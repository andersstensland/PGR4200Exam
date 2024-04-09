public class City {
    String name;
    public double latitude;

    public double longitude;

    public City(String name, double latitude) {
        this.name = name;
        this.latitude = latitude;
    }

    public int compareTo(City other) {
        return Double.compare(this.latitude, other.latitude);
    }


    @Override
    public String toString() {
        return name + " (" + latitude + ")";
    }
}
