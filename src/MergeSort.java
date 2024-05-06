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
            rekursivt, og deretter sammenslår de to halvdelene til en sorterert liste.

            Antall sammenslåinger (merges) som trengs for å sortere datasettet er direkte knyttet til hvor mange nivåer
            av rekursjon i algoritmen, ikke til den opprinnelige rekkefølgen på dataene. Dette vil si at det har null
            betydning hvordan datasettet er sortert. Dette skyldes at algoritmen følger ett logaritmisk forhold
            til størrelsen altså O(n log n).



        Use the latitude and longitude values for each city. (20/50 Marks)
            c. Implement a proper merge sort algorithm so that the (latitude, longitude) pairs are in an
               ordered list. What distance measure is used?
*/
    public static void main(String[] args) {
        String filePath = "csv/worldcities.csv";
        String outputFilePath1A = "csv/ms_sorted_bylat_worldcities.csv";
        String outputFilePath1C = "csv/ms_sorted_bylat&lng_worldcities.csv";

        List<City> cities = new ArrayList<>();



        List<City> cities1 = new ArrayList<>();


        /*

        - Leser fil og legger inn City objekter i Array Listen


         */
        try (Scanner scanner = new Scanner(new File(filePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Hopper over første linjen i CSV filen (overskrifter)
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
                String country = columns[4].replace("\"", "");

                try {
                    double latitude = Double.parseDouble(latitudeString);
                    double longitude = Double.parseDouble(longitudeString);
                    if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) continue; // Validerer kordinatene

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


        //mergeSort(cities, 0, cities.size() - 1, mergeCount);


        /*

        Problem 1B: Sorting by latitude

        long start2 = System.currentTimeMillis();

        mergeSort(cities, 0, cities.size() - 1, mergeCount);

        long end2 = System.currentTimeMillis();

        System.out.println("Elapsed Time in milli seconds: "+ (end2-start2));

        // Write sorted data to a new CSV file
        try (PrintWriter writer = new PrintWriter(new File(outputFilePath1A))) {
            writer.println("Name,Latitude,Longitude,Country");
            for (City city : cities) {
                writer.println(city.name + "," + city.latitude);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + outputFilePath1A);
        }

        System.out.println("Cities sorted and written to: " + outputFilePath1A);
        System.out.println("Number of merges needed: " + mergeCount[0]);


         */



        //Problem 1C: Sorting by latitude & longitude distance measure

        long start2 = System.currentTimeMillis();

        mergeSort2(cities, 0, cities.size() - 1, mergeCount);

        long end2 = System.currentTimeMillis();

        System.out.println("Elapsed Time in milli seconds: "+ (end2-start2));

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

    public static void mergeSort(List<City> cities, int left, int right, int[] mergeCount) {
        // Base case
        if (left < right) {
            // Finner mellompunktet til arrayet
            int middle = (left + right) / 2;

            // Sorter første og andre halvdelene
            mergeSort(cities, left, middle, mergeCount);
            mergeSort(cities, middle + 1, right, mergeCount);

            // Merger de sorter halvdelene
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


        mergeCount[0]++;

    }


    public static void mergeSort2(List<City> cities, int left, int right, int[] mergeCount) {
        if (left < right) {
            // Finner mellompunktet til arrayet
            int middle = (left + right) / 2;

            // Sorter første og andre halvdelene
            mergeSort2(cities, left, middle, mergeCount);
            mergeSort2(cities, middle + 1, right, mergeCount);

           // Merger de
            merge2(cities, left, middle, right, mergeCount);
        }
    }


    private static void merge2(List<City> cities, int left, int middle, int right, int[] mergeCount) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
    
        List<City> L = new ArrayList<>(n1);
        List<City> R = new ArrayList<>(n2);
    
        for (int i = 0; i < n1; ++i)
            L.add(i, cities.get(left + i));
        for (int j = 0; j < n2; ++j)
            R.add(j, cities.get(middle + 1 + j));
    
        int i = 0;
        int j = 0;
    
        int k = left;
        while (i < n1 && j < n2) {
            if (L.get(i).latitude < R.get(j).latitude ||
                    (L.get(i).latitude == R.get(j).latitude && L.get(i).longitude <= R.get(j).longitude)) {
                cities.set(k, L.get(i));
                i++;
            } else {
                cities.set(k, R.get(j));
                j++;
            }
            k++;
        }
    
        while (i < n1) {
            cities.set(k, L.get(i));
            i++;
            k++;
        }
    
        while (j < n2) {
            cities.set(k, R.get(j));
            j++;
            k++;
        }
    
        mergeCount[0]++;
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
