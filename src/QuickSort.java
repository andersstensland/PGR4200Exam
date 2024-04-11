import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class LatLon {
    double latitude;
    double longitude;

    public LatLon(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return latitude + ", " + longitude;
    }
}

public class QuickSort {
    public static int comparisons = 0;

    public static void quickSortByLat(List<Map.Entry<String, LatLon>> list, int low, int high) {
        if (low < high) {
            int pi = partitionByLat(list, low, high);
            quickSortByLat(list, low, pi - 1);
            quickSortByLat(list, pi + 1, high);
        }
    }

    public static int partitionByLat(List<Map.Entry<String, LatLon>> list, int low, int high) {
        double pivot = list.get(high).getValue().latitude;
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            comparisons++;
            if (list.get(j).getValue().latitude <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static void quickSortByLon(List<Map.Entry<String, LatLon>> list, int low, int high) {
        if (low < high) {
            int pi = partitionByLon(list, low, high);
            quickSortByLon(list, low, pi - 1);
            quickSortByLon(list, pi + 1, high);
        }
    }

    public static int partitionByLon(List<Map.Entry<String, LatLon>> list, int low, int high) {
        double pivot = list.get(high).getValue().longitude;
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            comparisons++;
            if (list.get(j).getValue().longitude <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static void quickSortByLatLon(List<Map.Entry<String, LatLon>> list, int low, int high) {
        if (low < high) {
            int pi = partitionByLatLon(list, low, high);
    
            quickSortByLatLon(list, low, pi - 1);
            quickSortByLatLon(list, pi + 1, high);
        }
    }
    
    private static int partitionByLatLon(List<Map.Entry<String, LatLon>> list, int low, int high) {
        LatLon pivot = list.get(high).getValue();
        int i = (low - 1);
    
        for (int j = low; j <= high - 1; j++) {
            comparisons++;
            // If current element is smaller than or equal to pivot
            if (compareLatLon(list.get(j).getValue(), pivot) <= 0) {
                i++;
    
                // swap arr[i] and arr[j]
                Map.Entry<String, LatLon> temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
    
        // swap arr[i+1] and arr[high] (or pivot)
        Map.Entry<String, LatLon> temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
    
        return (i + 1);
    }
    
    private static int compareLatLon(LatLon a, LatLon b) {
        if (a.latitude < b.latitude) {
            return -1;
        } else if (a.latitude > b.latitude) {
            return 1;
        } else {
            return Double.compare(a.longitude, b.longitude);
        }
    }

    public static List<Map.Entry<String, LatLon>> readCSV(String filePath) {
        Map<String, LatLon> latitudes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String cityName = values[1].replace("\"", "");
                String latitudeStr = values[2].replace("\"", "");
                String longitudeStr = values[3].replace("\"", "");
                latitudes.put(cityName, new LatLon(Double.parseDouble(latitudeStr), Double.parseDouble(longitudeStr)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map.Entry<String, LatLon>> list = new ArrayList<>(latitudes.entrySet());
        return list;
    }

    public static void main(String[] args) {
        List<Map.Entry<String, LatLon>> latitudes = readCSV("src/csv/worldcities.csv");
        Collections.shuffle(latitudes);
    
        quickSortByLat(latitudes, 0, latitudes.size() - 1);
        System.out.println("Number of comparisons in QuickSort by Latitude: " + comparisons);
        comparisons = 0;
    
        quickSortByLon(latitudes, 0, latitudes.size() - 1);
        System.out.println("Number of comparisons in QuickSort by Longitude: " + comparisons);
        comparisons = 0;
    
        quickSortByLatLon(latitudes, 0, latitudes.size() - 1);
        System.out.println("Number of comparisons in QuickSort by Latitude and Longitude: " + comparisons);
    }
}