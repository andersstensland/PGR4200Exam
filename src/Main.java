import java.util.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/csv/worldcities.csv";
        List<Map.Entry<String, LatLon>> latitudes = QuickSort.readCSV(filePath);
        Collections.shuffle(latitudes);
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "\n-------------------\nPress 1 for QuickSort by latitude\nPress 2 for QuickSort by longitude\nPress 3 for QuickSort by latitude and longitude\nPress 4 for MergeSort:");
        int choice = scanner.nextInt();

        if (choice == 1) {
            QuickSort.quickSortByLat(latitudes, 0, latitudes.size() - 1);
            System.out.println("\n-------------------\nQuickSort by latitude result: ");
            for (Map.Entry<String, LatLon> entry : latitudes) {
                System.out.println(entry.getKey() + ": " + entry.getValue().latitude);
            }
            System.out.println("Number of comparisons in QuickSort by Latitude: " + QuickSort.comparisons);
            QuickSort.comparisons = 0;
        } else if (choice == 2) {
            QuickSort.quickSortByLon(latitudes, 0, latitudes.size() - 1);
            System.out.println("\n-------------------\nQuickSort by longitude result: ");
            for (Map.Entry<String, LatLon> entry : latitudes) {
                System.out.println(entry.getKey() + ": " + entry.getValue().longitude);
            }
            System.out.println("Number of comparisons in QuickSort by Longitude: " + QuickSort.comparisons);
            QuickSort.comparisons = 0;
        } else if (choice == 3) {
            QuickSort.quickSortByLatLon(latitudes, 0, latitudes.size() - 1);
            System.out.println("\n-------------------\nQuickSort by latitude and longitude result: ");
            for (Map.Entry<String, LatLon> entry : latitudes) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out
                    .println("Number of comparisons in QuickSort by Latitude and Longitude: " + QuickSort.comparisons);
            QuickSort.comparisons = 0;
        } else if (choice == 4) {
            System.out.println("\n-------------------\nMergeSort is not yet implemented");
        } else {
            System.out.println("\n-------------------\nInvalid choice");
        }

        scanner.close();
    }
}