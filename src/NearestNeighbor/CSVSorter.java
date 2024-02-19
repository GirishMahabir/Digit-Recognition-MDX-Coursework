package NearestNeighbor;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class CSVSorter {
    /**
     * Sorts a CSV file by a specified column and outputs the sorted content to another CSV file.
     * @param inputFile Path to the input CSV file.
     * @param outputFile Path to the output CSV file where the sorted content is saved.
     * @param columnIndex The index of the column to sort by (0-based).
     * @throws IOException If an I/O error occurs reading from the file or writing to the file.
     */
    public static void sortCsvByColumn(String inputFile, String outputFile, int columnIndex) throws IOException {
        // Read all lines from the CSV file
        List<String> lines = Files.readAllLines(Paths.get(inputFile));

        // Sort the lines based on the value in the specified column
        List<String> sorted = lines.stream()
                .sorted(Comparator.comparingInt(line -> Integer.parseInt(line.split(",")[columnIndex])))
                .collect(Collectors.toList());

        // Write the sorted lines back to another CSV file
        Files.write(Paths.get(outputFile), sorted);
    }

    /**
     * Counts the occurrence of each digit (last value in each line) in a CSV file.
     * Assumes the last value in each line represents a digit (0-9).
     * @param inputFile Path to the input CSV file.
     * @return An array of counts for each digit (0-9).
     * @throws IOException If an I/O error occurs reading from the file.
     */
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

    /**
     * Divides the data from a CSV file into training and test datasets based on a specified percentage for training.
     * @param inputFile Path to the input CSV file.
     * @param trainFile Path to the output CSV file for the training dataset.
     * @param testFile Path to the output CSV file for the test dataset.
     * @param trainPercentage The percentage of data to be used for the training dataset.
     * @throws IOException If an I/O error occurs reading from the file or writing to the files.
     */
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

        // Initialize lists to store the training and test data
        List<String> trainData = new ArrayList<>();
        List<String> testData = new ArrayList<>();

        // Distribute lines between training and test data based on the calculated counts
        int[] trainCountAdded = new int[10];

        for (String line : lines) {
            String[] values = line.split(",");
            int digit = Integer.parseInt(values[values.length - 1]);
            if (trainCountAdded[digit] < trainCount[digit]) {
                trainData.add(line);
                trainCountAdded[digit]++;
            } else {
                testData.add(line);
            }
        }

        // Write the training and test data to their respective files
        Files.write(Paths.get(trainFile), trainData);
        Files.write(Paths.get(testFile), testData);
    }

    /**
     * Attempts to delete a list of temporary files.
     * @param files A list of file paths to attempt to delete.
     */
    public static void removeTempFiles(List<String> files) {
        for (String file : files) {
            try {
                Files.deleteIfExists(Paths.get(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}