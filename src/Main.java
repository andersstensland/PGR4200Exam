import java.util.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/csv/worldcities.csv";

        // Read data from CSV file
        List<Map.Entry<String, Double>> latitudes = QuickSort.readCSV(filePath);

        // Input
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n-------------------\nPress 1 for QuickSort\nPress 2 for MergeSort:");
        int choice = scanner.nextInt();

        if (choice == 1) {
            // Sort latitude
            QuickSort.quickSort(latitudes, 0, latitudes.size() - 1);
            System.out.println("\n-------------------\nQuickSort result: ");
            for (Map.Entry<String, Double> entry : latitudes) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } else if (choice == 2) {
            System.out.println("\n-------------------\nMergeSort is not yet implemented");
        } else {
            System.out.println("\n-------------------\nInvalid choice");
        }

        scanner.close();
    }
}