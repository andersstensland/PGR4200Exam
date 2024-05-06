import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class QuickSort {
    private static final double EARTH_RADIUS = 6371.0;


    /*
    At first, use the unique latitude values of each city only. (30/50 Marks)
        a. Implement a proper quick sort algorithm so that all city latitudes are in an ordered list.
        b. Count the number of comparisons needed to sort the dataset. Does it change if you
           randomly order the list before sorting? Why/why not?

           Antall sammenligninger kan endre seg i QuickSort dette skyldes pivot element.



    Use the latitude and longitude values for each city. (20/50 Marsk)
        c. Implement a proper quick sort algorithm so that the (latitude, longitude) pairs are in an
        ordered list.




     */


    public QuickSort() {
    }

    public static void main(String[] args) {
        String filePath = "src/csv/worldcities.csv";
        String outputFilePath2A = "src/csv/qs_sorted_bylat_worldcities.csv";
        String outputFilePath2C = "src/csv/qs_sorted_bylat&lng_worldcities.csv";
        List<City> cities = new ArrayList();

                /*

        - Leser fil og legger inn City objekter i Array Listen


         */

        try {
            Scanner scanner = new Scanner(new File(filePath));

            try {
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] columns = line.split(",");
                    String name = columns[1].replace("\"", "");
                    String latitudeString = columns[2].replace("\"", "");
                    String longitudeString = columns[3].replace("\"", "");
                    String country = columns[4].replace("\"", "");
                    double latitude = Double.parseDouble(latitudeString);
                    double longitude = Double.parseDouble(longitudeString);
                    cities.add(new City(name, latitude, longitude, country));
                }
            } catch (Throwable var21) {
                try {
                    scanner.close();
                } catch (Throwable var17) {
                    var21.addSuppressed(var17);
                }

                throw var21;
            }

            scanner.close();
        } catch (FileNotFoundException var22) {
            System.out.println("File not found " + filePath);
            return;
        }

        int[] comparisonCount = new int[1];
        Collections.shuffle(cities);
        long startTime = System.currentTimeMillis();
        quickSort2(cities, 0, cities.size() - 1, comparisonCount);
        long memoryAfter = getMemoryUsage();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        try {
            PrintWriter writer = new PrintWriter(new File(outputFilePath2A));

            try {
                writer.println("Name,Latitude,Longitude,Country");
                Iterator var15 = cities.iterator();

                while(var15.hasNext()) {
                    City city = (City)var15.next();
                    writer.println(city.name + "," + city.latitude);
                }
            } catch (Throwable var19) {
                try {
                    writer.close();
                } catch (Throwable var18) {
                    var19.addSuppressed(var18);
                }

                throw var19;
            }

            writer.close();
        } catch (FileNotFoundException var20) {
            System.out.println("Error writing to file: " + outputFilePath2A);
        }

        System.out.println("Cities sorted and written to: " + outputFilePath2A);
        System.out.println("Number of comparisons needed: " + comparisonCount[0]);
        System.out.println("Time taken for sorting: " + elapsedTime + " milliseconds");
        System.out.println("Memory used after sorting: " + memoryAfter + " KB");
    }

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

    public static void quickSort2(List<City> cities, int low, int high, int[] comparisonCount) {
        if (low < high) {
            int pi = partition2(cities, low, high, comparisonCount);
            quickSort2(cities, low, pi - 1, comparisonCount);
            quickSort2(cities, pi + 1, high, comparisonCount);
        }

    }

    private static int partition2(List<City> cities, int low, int high, int[] comparisonCount) {
        double pivotDistance = calculateDistance(0.0, 0.0, ((City)cities.get(high)).latitude, ((City)cities.get(high)).longitude);
        int i = low - 1;

        for(int j = low; j < high; ++j) {
            double currentDistance = calculateDistance(0.0, 0.0, ((City)cities.get(j)).latitude, ((City)cities.get(j)).longitude);
            if (currentDistance < pivotDistance) {
                ++i;
                City temp = (City)cities.get(i);
                cities.set(i, (City)cities.get(j));
                cities.set(j, temp);
            }

            int var10002 = comparisonCount[0]++;
        }

        City temp = (City)cities.get(i + 1);
        cities.set(i + 1, (City)cities.get(high));
        cities.set(high, temp);
        return i + 1;
    }

    static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);
        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2.0);
        double y = lat2Rad - lat1Rad;
        double distance = Math.sqrt(x * x + y * y) * 6371.0;
        return distance;
    }

    private static long getMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024L;
    }

    static class City {
        String name;
        double latitude;
        double longitude;
        String country;

        public City(String name, double latitude, double longitude, String country) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.country = country;
        }
    }
}