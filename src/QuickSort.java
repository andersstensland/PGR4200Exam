import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class QuickSort {
    public static void quickSort(List<Map.Entry<String, Double>> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    public static int partition(List<Map.Entry<String, Double>> list, int low, int high) {
        double pivot = list.get(high).getValue();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (list.get(j).getValue() <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static List<Map.Entry<String, Double>> readCSV(String filePath) {
        Map<String, Double> latitudes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String cityName = values[1].replace("\"", "");
                String latitudeStr = values[2].replace("\"", "");
                latitudes.put(cityName, Double.parseDouble(latitudeStr));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map.Entry<String, Double>> list = new ArrayList<>(latitudes.entrySet());
        return list;
    }

    public static void main(String[] args) {
        List<Map.Entry<String, Double>> latitudes = readCSV("src/csv/worldcities.csv");
        quickSort(latitudes, 0, latitudes.size() - 1);
        System.out.println(latitudes);
    }
}