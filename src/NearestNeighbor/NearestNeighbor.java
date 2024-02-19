package NearestNeighbor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NearestNeighbor {

    // Path and file configuration
    static String dataDir = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/";
    static String datasetExt = ".csv";
    static String datasetPath = dataDir + "cw2DataSet1" + datasetExt;
    static String datasetPathOut = dataDir + "cw2DataSet1Sorted" + datasetExt;
    static String datasetPathTrain = dataDir + "cw2DataSet1Train" + datasetExt;
    static String datasetPathTest = dataDir + "cw2DataSet1Test" + datasetExt;
    static String datasetPath2 = dataDir + "cw2DataSet2" + datasetExt;
    static String datasetPathOut2 = dataDir + "cw2DataSet2Sorted" + datasetExt;

    // Visualization characters
    static char low = '.';
    static char mid = 'x';
    static char high = 'X';

    /**
     * Main method to initiate the nearest neighbor classification process. It prepares the datasets, performs the
     * classification, and prints out the results.
     *
     * @param args Command line arguments, not used in this implementation.
     * @throws IOException If there is an issue with file reading or writing during dataset preparation.
     */
    public static void main(String[] args) throws IOException {
        prepareDatasets();
        List<List<Integer>> datasetTrain = csvToIntList(datasetPathTrain);
        List<List<Integer>> datasetTest = csvToIntList(datasetPathTest);
        performClassificationAndPrintResults(datasetTrain, datasetTest);
        // Remove the temporary sorted files by passing list of temporary files to CSVSorter.removeTempFiles.
        CSVSorter.removeTempFiles(Arrays.asList(datasetPathOut, datasetPathOut2, datasetPathTrain));
    }

    /**
     * Prepares the datasets for classification by sorting based on labels and dividing into training and testing sets.
     * This function sorts the datasets by their labels to ensure that the division between training and testing data
     * is based on a consistent criterion.
     *
     * @throws IOException If an error occurs during file reading or writing.
     */
    private static void prepareDatasets() throws IOException {
        // Sort the dataset 1 by the labels (assuming labels are in the last column, index 64)
        // and use the sorted dataset for training.
        CSVSorter.sortCsvByColumn(datasetPath, datasetPathOut, 64);
        datasetPathTrain = datasetPathOut; // Use the sorted dataset 1 for training.

        // Directly use dataset 2 for testing without sorting as it's assumed to be prepared.
        datasetPathTest = datasetPath2;

    }

    /**
     * Converts the contents of a CSV file into a list of lists of integers. Each line in the CSV file is converted
     * into a list of integers, representing a single data point in the dataset. This method is used to load the dataset
     * from a file into a data structure that can be used for further processing.
     *
     * @param dataSetPath The file path of the CSV dataset.
     * @return A list of lists of integers, where each inner list represents a row from the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<List<Integer>> csvToIntList(String dataSetPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(dataSetPath));
        return lines.stream().map(line -> Arrays.stream(line.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList())).collect(Collectors.toList());
    }

    /**
     * Performs the nearest neighbor classification on the test dataset against the training dataset and prints the results.
     * This method iterates through each item in the test dataset, classifies it using the nearest neighbor algorithm
     * by finding the closest match in the training dataset, and updates the prediction statistics. Finally, it prints
     * the classification accuracy and precision for each label.
     *
     * @param datasetTrain The training dataset loaded as a list of lists of integers.
     * @param datasetTest The test dataset loaded as a list of lists of integers.
     */
    private static void performClassificationAndPrintResults(List<List<Integer>> datasetTrain, List<List<Integer>> datasetTest) {
        int correctPredictions = 0;
        Map<Integer, Integer> truePositives = new HashMap<>();
        Map<Integer, Integer> falsePositives = new HashMap<>();
        Map<Integer, Integer> actualCounts = new HashMap<>();

        for (List<Integer> testRow : datasetTest) {
            ClassificationResult result = classifyRow(testRow, datasetTrain);
            if (result.isCorrect) {
                correctPredictions++;
                truePositives.put(result.predictedLabel, truePositives.getOrDefault(result.predictedLabel, 0) + 1);
            } else {
                falsePositives.put(result.predictedLabel, falsePositives.getOrDefault(result.predictedLabel, 0) + 1);
            }
            actualCounts.put(testRow.get(64), actualCounts.getOrDefault(testRow.get(64), 0) + 1);
        }

        printResults(correctPredictions, datasetTest.size(), truePositives, falsePositives, actualCounts);
    }

    /**
     * Classifies a single row from the test dataset by finding the closest row in the training dataset using the
     * Euclidean distance metric. This method calculates the distance between the test row and each row in the training
     * dataset, identifies the row with the smallest distance (closest match), and determines if the prediction is correct
     * based on the labels.
     * @param testRow A single row from the test dataset.
     * @param datasetTrain The training dataset.
     * @return A ClassificationResult object containing information about whether the classification was correct,
     * the index of the closest match, and the predicted label.
     */
    private static ClassificationResult classifyRow(List<Integer> testRow, List<List<Integer>> datasetTrain) {
        double smallestDistance = Double.MAX_VALUE;
        int closestMatchIndex = -1;
        int actualLabel = testRow.get(64);

        for (int trainIndex = 0; trainIndex < datasetTrain.size(); trainIndex++) {
            double distance = calculateEuclideanDistance(testRow, datasetTrain.get(trainIndex));
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestMatchIndex = trainIndex;
            }
        }

        int predictedLabel = datasetTrain.get(closestMatchIndex).get(64);
        boolean isCorrect = predictedLabel == actualLabel;
        return new ClassificationResult(isCorrect, closestMatchIndex, predictedLabel);
    }

    /**
     * Calculates the Euclidean distance between two vectors (digit representations). This method is used as part of
     * the nearest neighbor classification to find the closest match for a test digit in the training dataset.
     * @param digit1 The first digit vector.
     * @param digit2 The second digit vector.
     * @return The Euclidean distance between the two digit vectors.
     */
    static double calculateEuclideanDistance(List<Integer> digit1, List<Integer> digit2) {
        double sumSquaredDiffs = 0.0;
        for (int i = 0; i < digit1.size() - 1; i++) {
            sumSquaredDiffs += Math.pow(digit1.get(i) - digit2.get(i), 2);
        }
        return Math.sqrt(sumSquaredDiffs);
    }

    /**
     * Prints the results of the nearest neighbor classification, including the overall accuracy and the precision
     * for each digit label. This method calculates the accuracy by comparing the number of correct predictions to the
     * total predictions and calculates the precision for each label based on true positives and false positives.
     * @param correctPredictions The number of correct predictions made by the classifier.
     * @param totalPredictions The total number of predictions made.
     * @param truePositives A map containing the count of true positive predictions for each label.
     * @param falsePositives A map containing the count of false positive predictions for each label.
     * @param actualCounts A map containing the count of actual occurrences of each label in the test dataset.
     */
    private static void printResults(int correctPredictions, int totalPredictions, Map<Integer, Integer> truePositives, Map<Integer, Integer> falsePositives, Map<Integer, Integer> actualCounts) {
        double accuracy = (double) correctPredictions / totalPredictions * 100;
        System.out.println("Accuracy: " + accuracy + "%");

        truePositives.forEach((key, value) -> {
            int fp = falsePositives.getOrDefault(key, 0);
            double precision = value + fp > 0 ? (double) value / (value + fp) * 100 : 0;
            System.out.println("Precision for digit " + key + ": " + precision + "%");
        });
    }

    /**
     * Represents the result of classifying a single test row. This class contains information about whether the
     * classification was correct, the index of the closest match in the training dataset, and the predicted label.
     */
    private static class ClassificationResult {
        boolean isCorrect;
        int closestMatchIndex;
        int predictedLabel;

        /**
         * Constructs a ClassificationResult instance.
         * @param isCorrect Indicates whether the classification prediction was correct.
         * @param closestMatchIndex The index of the closest match found in the training dataset.
         * @param predictedLabel The label predicted by the classifier.
         */
        ClassificationResult(boolean isCorrect, int closestMatchIndex, int predictedLabel) {
            this.isCorrect = isCorrect;
            this.closestMatchIndex = closestMatchIndex;
            this.predictedLabel = predictedLabel;
        }
    }
}
