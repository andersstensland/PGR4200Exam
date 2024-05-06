import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class QuickSort {

    private static final double EARTH_RADIUS = 6371;

    /*
    At first, use the unique latitude values of each city only. (30/50 Marks)
        a. Implement a proper quick sort algorithm so that all city latitudes are in an ordered list.
        b. Count the number of comparisons needed to sort the dataset. Does it change if you
           randomly order the list before sorting? Why/why not


    Use the latitude and longitude values for each city. (20/50 Marsk)
        c. Implement a proper quick sort algorithm so that the (latitude, longitude) pairs are in an
        ordered list.


     */


    public static void main(String[] args) {

        String filePath = "src/csv/worldcities.csv";
        String outputFilePath2A = "csv/qs_sorted_bylat_worldcities.csv";
        String outputFilePath2C = "csv/qs_sorted_bylat&lng_worldcities.csv";

        List<City> cities = new ArrayList<>();



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

                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);


                // Legger til i array list
                cities.add(new City(name, latitude, longitude, country));


            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filePath);
            return;
        }


        int[] comparisonCount = new int[1];

        // Problem 2A

        // Shuffling for å simulerer at comparsions endrer seg (Oppgave 2 B)
        Collections.shuffle(cities);


        quickSort2(cities, 0, cities.size() - 1, comparisonCount);
        // Write sorted data to a new CSV file
        try (PrintWriter writer = new PrintWriter(new File(outputFilePath2A))) {
            writer.println("Name,Latitude,Longitude,Country");
            for (City city : cities) {
                writer.println(city.name + "," + city.latitude);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + outputFilePath2A);
        }

        System.out.println("Cities sorted and written to: " + outputFilePath2A);
        System.out.println("Number of comparisons needed: " + comparisonCount[0]);


        /*
        // Problem 2C

        quickSort(cities, 0, cities.size() - 1, comparisonCount);
        // Write sorted data to a new CSV file
        try (PrintWriter writer = new PrintWriter(new File(outputFilePath2C))) {
            writer.println("Name,Latitude,Longitude,Country");
            for (City city : cities) {
                writer.println(city.name + "," + city.latitude + "," + city.longitude + "," + city.country);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + outputFilePath2C);
        }

        System.out.println("Cities sorted and written to: " + outputFilePath2C);
        System.out.println("Number of comparisons needed: " + comparisonCount[0]);

         */

    }

    // Hentet fra https://www.geeksforgeeks.org/quick-sort/

    public static void quickSort(List<City> cities, int low, int high, int[] comparisonCount) {
        if (low < high) {
            int pi = partition(cities, low, high, comparisonCount);
            quickSort(cities, low, pi - 1, comparisonCount);
            quickSort(cities, pi + 1, high, comparisonCount);
        }
    }

    private static int partition(List<City> cities, int low, int high, int[] comparisonCount) {
        City pivot = cities.get(high); // Tar siste element som "pivot"
        int i = (low - 1); // Indexen av de mindre elementene
        for (int j = low; j < high; j++) {
            // Hvis element er mindre enn pivot
            if (cities.get(j).latitude < pivot.latitude) {
                i++;
                // Bytter plass
                City temp = cities.get(i);
                cities.set(i, cities.get(j));
                cities.set(j, temp);
            }
        }
        // Bytter cities.get(i+1) og cities.get(high) (eller pivot)
        City temp = cities.get(i + 1);
        cities.set(i + 1, cities.get(high));
        cities.set(high, temp);


        comparisonCount[0]++;

        return i + 1;


    }

    public static void quickSort2(List<City> cities, int low, int high, int[] comparisonCount) {
        if (low < high) {
            int pi = partition2(cities, low, high, comparisonCount);
            quickSort2(cities, low, pi - 1, comparisonCount);
            quickSort2(cities, pi + 1, high, comparisonCount);
        }
    }

    private static int partition2(List<City> cities, int low, int high, int[] comparisonCount) {
        double pivotDistance = calculateDistance(0, 0, cities.get(high).latitude, cities.get(high).longitude);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            double currentDistance = calculateDistance(0, 0, cities.get(j).latitude, cities.get(j).longitude);
            if (currentDistance < pivotDistance) {
                i++;
                City temp = cities.get(i);
                cities.set(i, cities.get(j));
                cities.set(j, temp);
            }
            comparisonCount[0]++;
        }
        City temp = cities.get(i + 1);
        cities.set(i + 1, cities.get(high));
        cities.set(high, temp);

        return i + 1;
    }


    public static void quickSort3(List<City> cities, int low, int high, int[] comparisonCount) {
        if (low < high) {
            int pi = partition3(cities, low, high, comparisonCount);
            quickSort3(cities, low, pi - 1, comparisonCount);
            quickSort3(cities, pi + 1, high, comparisonCount);
        }
    }

    private static int partition3(List<City> cities, int low, int high, int[] comparisonCount) {
        City pivot = cities.get(high); 
        int i = (low - 1); 

        for (int j = low; j < high; j++) {
l
            if (cities.get(j).latitude < pivot.latitude ||
                    (cities.get(j).latitude == pivot.latitude && cities.get(j).longitude < pivot.longitude)) {
                i++;


                City temp = cities.get(i);
                cities.set(i, cities.get(j));
                cities.set(j, temp);
            }

            comparisonCount[0]++;
        }


        City temp = cities.get(i + 1);
        cities.set(i + 1, cities.get(high));
        cities.set(high, temp);

        return i + 1;
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
