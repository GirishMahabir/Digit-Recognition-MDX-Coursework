package NearestNeighbor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class NearestNeighbor {

    static String dataDir = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/";
    static String datasetExt = ".csv";

    static String datasetPath = dataDir + "cw2DataSet1" + datasetExt;
    static String datasetPathOut = dataDir + "cw2DataSet1Sorted" + datasetExt;
    static String datasetPathTrain = dataDir + "cw2DataSet1Train" + datasetExt;
    static String datasetPathTest = dataDir + "cw2DataSet1Test" + datasetExt;
    static String datasetPath2 = dataDir + "cw2DataSet2" + datasetExt;
    static String datasetPathOut2 = dataDir + "cw2DataSet2Sorted" + datasetExt;

    static char low = '.';
    static char mid = 'x';
    static char high = 'X';
    public static void main(String[] args) throws IOException {
        // Step 1: Load the dataset
        // Sort the dataset by the last column
        CSVSorter.sortCsvByColumn(datasetPath, datasetPathOut, 64);
        datasetPath = datasetPathOut;

        CSVSorter.divideTrainTestData(datasetPath, datasetPathTrain, datasetPathTest,80);

        // UNCOMMENT THIS TO USE THE SECOND DATASET
//        CSVSorter.sortCsvByColumn(datasetPath2, datasetPathOut2, 64);
//        datasetPath2 = datasetPathOut2;
//        datasetPathTest = datasetPath2;

        List<List<Integer>> datasetTrain = csvToIntList(datasetPathTrain);
        List<List<Integer>> datasetTest = csvToIntList(datasetPathTest);

        float highestValue = getHighestValue(datasetTrain);
        float lowestValue = getLowestValue(datasetTrain);
        float midValue = (highestValue + lowestValue) / 2;

//        float firstThreshold = lowestValue + (int) ((midValue - lowestValue) / 2);
//        float secondThreshold = midValue + (int) ((highestValue - midValue) / 2);
//        for (int i = 0; i < dataset.size()/2; i++) {
//            printRowToConsole(dataset.get(i), firstThreshold, secondThreshold);
//        }


        int totalPredictions = datasetTest.size();
        int correctPredictions = 0;
        Map<Integer, Integer> truePositives = new HashMap<>();
        Map<Integer, Integer> falsePositives = new HashMap<>();
        Map<Integer, Integer> actualCounts = new HashMap<>();



        for (int i = 0; i < datasetTest.size(); i++) {
            // Initialize variables to keep track of the smallest distance and index of the closest match
            double smallestDistance = Double.MAX_VALUE;
            int closestMatchIndex = -1;

            // Get the current digit from the second half
            List<Integer> currentDigit = datasetTest.get(i);

            // Compare the current digit to each digit in the first half
            for (int j = 0; j < datasetTrain.size(); j++) {
                // Get the digit from the first half
                List<Integer> comparisonDigit = datasetTrain.get(j);

                // Calculate Euclidean distance
                double distance = calculateEuclideanDistance(currentDigit, comparisonDigit);
                // Check if this is the smallest distance so far
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    closestMatchIndex = j;
                }
            }

            int predictedLabel = datasetTrain.get(closestMatchIndex).get(64);
            int actualLabel = datasetTest.get(i).get(64);

            if (predictedLabel == actualLabel) {
                correctPredictions++;
                truePositives.put(predictedLabel, truePositives.getOrDefault(predictedLabel, 0) + 1);
            } else {
                falsePositives.put(predictedLabel, falsePositives.getOrDefault(predictedLabel, 0) + 1);
            }
            actualCounts.put(actualLabel, actualCounts.getOrDefault(actualLabel, 0) + 1);

            // Step 4: Print the results
//            System.out.println("Test Digit " + i + " predicted as: " + predictedLabel + ", actual: " + actualLabel);
        }

        double accuracy = (double) correctPredictions / totalPredictions;
        System.out.println("Accuracy: " + (accuracy * 100) + "%");

        for (int digit = 0; digit <= 9; digit++) {
            int tp = truePositives.getOrDefault(digit, 0);
            int fp = falsePositives.getOrDefault(digit, 0);
            double precision = tp + fp > 0 ? (double) tp / (tp + fp) : 0;
            System.out.println("Precision for digit " + digit + ": " + (precision * 100) + "%");
        }
    }

    public static List<List<Integer>> csvToIntList(String dataSetPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(dataSetPath));
        return lines.stream().map(line -> {
            String[] values = line.split(",");
            return Arrays.stream(values).map(Integer::parseInt).collect(Collectors.toList());
        }).collect(Collectors.toList());
    }

    public static int getHighestValue(List<List<Integer>> dataset) {
        return dataset.stream().flatMap(List::stream).max(Integer::compareTo).get();
    }

    public static int getLowestValue(List<List<Integer>> dataset) {
        return dataset.stream().flatMap(List::stream).min(Integer::compareTo).get();
    }


    public static void printRowToConsole(List<Integer> row, float firstThreshold, float secondThreshold) {
        // 64 Elements in the row.
        // Build 8x8 matrix and return it.
        // Use the chars low, mid, high to represent the values.
        // Use the lowestValue, midValue, highestValue and the thresholds to determine the chars.
        // Print the matrix to the console.
        for (int i = 0; i < row.size() - 1; i++) {
            if (i % 8 == 0) {
                System.out.println();
            }
            if (row.get(i) < firstThreshold) {
                System.out.print(low + " ");
            } else if (row.get(i) < secondThreshold) {
                System.out.print(mid + " ");
            } else {
                System.out.print(high + " ");
            }
        }
        System.out.println();
    }

    static double calculateEuclideanDistance(List<Integer> digit1, List<Integer> digit2) {
        /*
         * The Euclidean distance between two vectors is the square root of the sum of the squared differences between
         * the elements in the two vectors.
         * @param List digit1 The first digit vector
         * @param List digit2 The second digit vector
         */
        // Initialize sum of squared differences
        double sumSquaredDiffs = 0.0;
        // Loop through each element in the digit vectors
        for (int i = 0; i < digit1.size() - 1; i++) {
            // Calculate the difference between the elements, square it, and add it to the sum
            sumSquaredDiffs += Math.pow(digit1.get(i) - digit2.get(i), 2);
        }
        // Take the square root of the sum to get the Euclidean distance
        return Math.sqrt(sumSquaredDiffs);
    }
}