import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuickSort {
    public static void main(String[] args) {
        String filePath = "csv/worldcities.csv";
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

                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);


                // Legger til i to ulike array lister en for oppgave A og en for oppgave B
                cities.add(new City(name, latitude, longitude));

                cities1.add(new City(name, latitude, longitude));

            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filePath);
            return;
        }

        quickSort(cities, 0, cities.size() - 1);
        System.out.println(cities);
    }

    // Hentet fra https://www.geeksforgeeks.org/quick-sort/

    public static void quickSort(List<City> cities, int low, int high) {
        if (low < high) {
            int pi = partition(cities, low, high);
            quickSort(cities, low, pi - 1);
            quickSort(cities, pi + 1, high);
        }
    }

    private static int partition(List<City> cities, int low, int high) {
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
        // Bytter cities.get(i+1) og cities.get(high) (or pivot)
        City temp = cities.get(i + 1);
        cities.set(i + 1, cities.get(high));
        cities.set(high, temp);

        return i + 1;
    }
}
