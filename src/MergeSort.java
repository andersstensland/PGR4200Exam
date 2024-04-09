import java.io.File;
import java.io.FileNotFoundException;
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

            Mengden merges kommer alltid til å være den samme uavhengig av hvordan dataset er sortert dette skyldes
            at mergeSort er O(log n).


        Use the latitude and longitude values for each city. (20/50 Marks)
            c. Implement a proper merge sort algorithm so that the (latitude, longitude) pairs are in an
               ordered list. What distance measure is used?

               // Usikker på om dette er løst riktig

    */

    public static void main(String[] args) {
        String filePath = "csv/worldcities.csv";
        List<City> cities = new ArrayList<>();

        List<City> cities1 = new ArrayList<>();


        // Les fil og fyll inn cities list;
        try (Scanner scanner = new Scanner(new File(filePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Hopper over første linjen i CSV filen
            }

            // While loop for resten av CSV Filen
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                // Andre kolonne inneholder navnet på by
                // Tredje kolonne inneholder lat
                // Fjerde kolonne innehold lng

                String name = columns[1].replace("\"", "");
                String latitudeString = columns[2].replace("\"", "");
                String longitudeString = columns[3].replace("\"", "");

                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);




                // add cities1


                cities.add(new City(name, latitude, longitude));

                cities1.add(new City(name, latitude, longitude));

            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filePath);
            return;
        }



        int[] mergeCount = new int[1];


        mergeSort(cities, 0, cities.size() - 1, mergeCount);

        mergeSort2(cities1, 0, cities.size() - 1, mergeCount);

        System.out.println(cities1);

        System.out.println("Number of merges needed: " + mergeCount[0]);
    }

    public static void mergeSort(List<City> cities, int left, int right, int[] mergeCount) {
        if (left < right) {
            // Find the middle point
            int middle = (left + right) / 2;

            // Sort first and second halves
            mergeSort(cities, left, middle, mergeCount);
            mergeSort(cities, middle + 1, right, mergeCount);

            // Merge the sorted halves
            merge(cities, left, middle, right, mergeCount);
        }
    }


    private static void merge(List<City> cities, int left, int middle, int right, int[] mergeCount) {
        // Finner størrelesnen til sub-arrays for merge
        int arr1 = middle - left + 1;
        int arr2 = right - middle;

        // Lager to midlertidig arrays
        List<City> L = new ArrayList<>(arr1);
        List<City> R = new ArrayList<>(arr2);

        // Lager temp arrays for venstre og høyre siden
        for (int i = 0; i < arr1; ++i)
            L.add(i, cities.get(left + i));
        for (int j = 0; j < arr2; ++j)
            R.add(j, cities.get(middle + 1 + j));

        // Merge

        // indexes
        int i = 0;
        int j = 0;

        int k = left;
        while (i < arr1 && j < arr2) {
            if(L.get(i).latitude <= R.get(j).latitude) {
                cities.set(k, L.get(i));
                i++;
            } else {
                cities.set(k, R.get(j));
                j++;
            }
            k++;
        }


        // kopier resterende av elemente i venstre siden av array hvis det er noen
        while (i < arr1) {
            cities.set(k, L.get(i));
            i++;
            k++;
        }

        // kopier resterende av elemente i høyre siden av array hvis det er noen
        while (j < arr2) {
            cities.set(k, R.get(j));
            j++;
            k++;
        }


        mergeCount[0]++; // Increment the number of merges

    }


    public static void mergeSort2(List<City> cities, int left, int right, int[] mergeCount) {
        if (left < right) {
            // Find the middle point
            int middle = (left + right) / 2;

            // Sort first and second halves
            mergeSort2(cities, left, middle, mergeCount);
            mergeSort2(cities, middle + 1, right, mergeCount);

            // Merge the sorted halves
            merge2(cities, left, middle, right, mergeCount);
        }
    }


    private static void merge2(List<City> cities, int left, int middle, int right, int[] mergeCount) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // Temporary arrays
        List<City> L = new ArrayList<>(n1);
        List<City> R = new ArrayList<>(n2);

        // Copy data to temporary arrays
        for (int i = 0; i < n1; ++i)
            L.add(i, cities.get(left + i));
        for (int j = 0; j < n2; ++j)
            R.add(j, cities.get(middle + 1 + j));

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = left;
        double refLat = 0.0; // Reference latitude
        double refLon = 0.0; // Reference longitude

        while (i < n1 && j < n2) {
            City leftCity = L.get(i);
            City rightCity = R.get(j);
            double distanceToLeftCity = calculateDistance(refLat, refLon, leftCity.latitude, leftCity.longitude);
            double distanceToRightCity = calculateDistance(refLat, refLon, rightCity.latitude, rightCity.longitude);

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
        while (i < n1) {
            cities.set(k, L.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            cities.set(k, R.get(j));
            j++;
            k++;
        }

        mergeCount[0]++; // Inkrementerer merge count
    }



    // Hentet fra https://www.baeldung.com/java-find-distance-between-points#equirectangular-distance-approximation

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



}
