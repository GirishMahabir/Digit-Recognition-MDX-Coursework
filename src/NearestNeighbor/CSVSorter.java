package NearestNeighbor;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class CSVSorter {

    public static void sortCsvByColumn(String inputFile, String outputFile, int columnIndex) throws IOException {
        // Read all lines from the CSV file
        List<String> lines = Files.readAllLines(Paths.get(inputFile));

        // Skip the header if your CSV has one
        // String header = lines.remove(0);

        // Sort the lines based on the value in the specified column
        List<String> sorted = lines.stream()
                .sorted(Comparator.comparingInt(line -> Integer.parseInt(line.split(",")[columnIndex])))
                .collect(Collectors.toList());

        // Write the sorted lines back to another CSV file
        Files.write(Paths.get(outputFile), sorted);
    }

    // Function to count the number of lines corresponding to each digit in the file.
    public static int[] countLines(String inputFile) throws IOException {
        // Read all lines from the CSV file
        List<String> lines = Files.readAllLines(Paths.get(inputFile));

        // Initialize an array to store the number of lines corresponding to each digit
        int[] count = new int[10];

        // Loop through each line in the file
        for (String line : lines) {
            // Split the line into an array of values
            String[] values = line.split(",");
            // Get the last value in the array, which is the digit
            int digit = Integer.parseInt(values[values.length - 1]);
            // Increment the count for this digit
            count[digit]++;
        }

        // Return the array of counts
        return count;
    }

    public static void divideTrainTestData(String inputFile, String trainFile, String testFile, int trainPercentage) throws IOException {
        // Read all lines from the CSV file
        List<String> lines = Files.readAllLines(Paths.get(inputFile));

        // Get the number of lines corresponding to each digit
        int[] count = countLines(inputFile);
        int[] trainCount = new int[10];

        // For each line in the file, calculate the percentage of lines to be used for training each digit.
        for (int i = 0; i < count.length; i++) {
            trainCount[i] = (int) (count[i] * trainPercentage / 100.0);
        }

        // Initialize a list to store the training data
        List<String> trainData = new ArrayList<>();
        // Initialize a list to store the test data
        List<String> testData = new ArrayList<>();

        // Loop through each line in the file,
        // The file starts with rows of 0s, then 1s, then 2s, etc.
        // trainCount[0] is the number of 0s to be used for training.
        // Once trainCount[0] 0s have been added to the training data, the rest will be added to the test data.
        // Then we move to the next digit.

        int[] trainCountAdded = new int[10];

        for (String line : lines) {
            // Split the line into an array of values
            String[] values = line.split(",");
            // Get the last value in the array, which is the digit
            int digit = Integer.parseInt(values[values.length - 1]);
            // Check if we have added enough lines for this digit to the training data
            if (trainCountAdded[digit] < trainCount[digit]) {
                // Add this line to the training data
                trainData.add(line);
                // Increment the count of lines added for this digit
                trainCountAdded[digit]++;
            } else {
                // Add this line to the test data
                testData.add(line);
            }
        }

        // Write the training data to a file
        Files.write(Paths.get(trainFile), trainData);
        // Write the test data to a file
        Files.write(Paths.get(testFile), testData);
    }
}
