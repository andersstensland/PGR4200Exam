import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MergeSort {
    private static final double EARTH_RADIUS = 6371;

    /*
        Problem 1. MergeSort (50 Marks)
        At first, use the unique latitude values of each city only. (30/50 Marks)
            a. Implement a proper merge sort algorithm so that all city latitudes are in an ordered list.
            b. Count the number of merges needed to sort the dataset. Does it change if you randomly
            order the list before sorting? Why/why not?

            SVAR:

            Merge sort er en "divide and conquer"-algoritme som deler datasettet i to halvdeler, sorterer hver halvdel
            rekursivt, og deretter sammenslår de to halvdelene til en sorteret liste.

            Antall sammenslåinger (merges) som trengs for å sortere datasettet er direkte knyttet til hvor mange nivåer
            av rekursjon i algoritmen, ikke til den opprinnelige rekkefølgen på dataene. Dette vil si at det har null
            betydning hvordan datasettet er sortert. Dette skyldes at algoritmen følger ett logaritmisk forhold
            til størrelsen altså O(n log n).



        Use the latitude and longitude values for each city. (20/50 Marks)
            c. Implement a proper merge sort algorithm so that the (latitude, longitude) pairs are in an
               ordered list. What distance measure is used?

               // Usikker på om dette er løst riktig

               Det er flere mulig måter og tolke denne oppgaven på

               - Først sortere etter latitude og deretter longitude eller motsatt

               - Kombinert metrikk for eks. avstand fra equator (som vi gjør nå)


    */

    public static void main(String[] args) {
        String filePath = "src/csv/worldcities.csv";
        String outputFilePath1A = "src/csv/ms_sorted_bylat_worldcities.csv";
        String outputFilePath1C = "src/csv/ms_sorted_bylat&lng_worldcities.csv";

        List<City> cities = new ArrayList<>();

        /* Reading file and adding City objects to the ArrayList */
        try (Scanner scanner = new Scanner(new File(filePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip the first line in the CSV file (headers)
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                String name = columns[1].replace("\"", "");
                String latitudeString = columns[2].replace("\"", "");
                String longitudeString = columns[3].replace("\"", "");
                String country = columns[4].replace("\"", "");

                try {
                    double latitude = Double.parseDouble(latitudeString);
                    double longitude = Double.parseDouble(longitudeString);
                    if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) continue; // Validate coordinates

                    cities.add(new City(name, latitude, longitude, country));
                } catch (NumberFormatException e) {
                    continue; // Handles malformed numeric data
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filePath);
            return;
        }



        int[] mergeCount = new int[1];


        //Problem 1C: Sorting by latitude & longitude distance measure

        long start2 = System.currentTimeMillis();

        mergeSort2(cities, 0, cities.size() - 1, mergeCount);

        // Get memory usage after sorting
        long memoryAfter = getMemoryUsage();

        long end2 = System.currentTimeMillis();

        System.out.println("Elapsed Time in milliseconds: "+ (end2-start2));
        System.out.println("Memory used after sorting: " + (memoryAfter) + " KB");

        // Write sorted data to a new CSV file
        try (PrintWriter writer = new PrintWriter(new File(outputFilePath1C))) {
            writer.println("Name,Latitude,Longitude,Country");
            for (City city : cities) {
                writer.println(city.name + "," + city.latitude + "," + city.longitude + "," + city.country);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + outputFilePath1C);
        }

        System.out.println("Cities sorted and written to: " + outputFilePath1C);
        System.out.println("Number of merges needed: " + mergeCount[0]);


    }

    public static void mergeSort2(List<City> cities, int left, int right, int[] mergeCount) {
        if (left < right) {
            // Find the middle point of the array
            int middle = (left + right) / 2;

            // Sort first and second halves
            mergeSort2(cities, left, middle, mergeCount);
            mergeSort2(cities, middle + 1, right, mergeCount);

            // Merge the sorted halves
            merge2(cities, left, middle, right, mergeCount);
        }
    }


    private static void merge2(List<City> cities, int left, int middle, int right, int[] mergeCount) {
        // Find sizes of two subarrays to be merged
        int arr1 = middle - left + 1;
        int arr2 = right - middle;

        // Create temporary arrays
        List<City> L = new ArrayList<>(arr1);
        List<City> R = new ArrayList<>(arr2);

        // Copy data to temporary arrays
        for (int i = 0; i < arr1; ++i)
            L.add(i, cities.get(left + i));

        for (int j = 0; j < arr2; ++j)
            R.add(j, cities.get(middle + 1 + j));

        // Merge the arrays

        // Initial indexes
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = left;

        while (i < arr1 && j < arr2) {
            City leftCity = L.get(i);
            City rightCity = R.get(j);
            double distanceToLeftCity = calculateDistance(0, 0, leftCity.latitude, leftCity.longitude);
            double distanceToRightCity = calculateDistance(0, 0, rightCity.latitude, rightCity.longitude);

            if (distanceToLeftCity <= distanceToRightCity) {
                cities.set(k, leftCity);
                i++;
            } else {
                cities.set(k, rightCity);
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < arr1) {
            cities.set(k, L.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < arr2) {
            cities.set(k, R.get(j));
            j++;
            k++;
        }

        mergeCount[0]++; // Increment merge count
    }



    // Equirectangular Distance Approximation
    static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }

    private static long getMemoryUsage() {
        // Trigger garbage collection to clear out unused objects
        System.gc();
        // Wait for a short time for garbage collection to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Get memory usage after garbage collection
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
    }


}