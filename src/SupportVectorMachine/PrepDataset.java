package SupportVectorMachine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class for preparing datasets for SVM training and testing.
 */
public class PrepDataset {

    /**
     * Prepares the dataset from a given path, splitting features and labels.
     *
     * @param datasetPath Path to the dataset file.
     * @return Array of DataSetDS instances.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public static DataSetDS[] prepDataset(String datasetPath) throws IOException {
        int[][] dataset = readDataset(datasetPath);
        DataSetDS[] datasetDSArray = new DataSetDS[dataset.length];

        // Extract features and label for each data point.
        for (int dataIndex = 0; dataIndex < dataset.length; dataIndex++) {
            int[] features = new int[dataset[dataIndex].length - 1];
            // Copy features from dataset.
            for (int featureIndex = 0; featureIndex < dataset[dataIndex].length - 1; featureIndex++) {
                features[featureIndex] = dataset[dataIndex][featureIndex];
            }
            datasetDSArray[dataIndex] = new DataSetDS(features, dataset[dataIndex][dataset[dataIndex].length - 1]);
        }
        return datasetDSArray;
    }

    /**
     * Reads dataset from a file and converts it into a 2D integer array.
     *
     * @param datasetPath Path to the dataset file.
     * @return 2D integer array representing the dataset.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    private static int[][] readDataset(String datasetPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(datasetPath));
        int[][] dataset = new int[lines.size()][];

        // Parse each line into an integer array.
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String[] lineParts = lines.get(lineIndex).split(",");
            dataset[lineIndex] = new int[lineParts.length];
            for (int partIndex = 0; partIndex < lineParts.length; partIndex++) {
                dataset[lineIndex][partIndex] = Integer.parseInt(lineParts[partIndex]);
            }
        }
        return dataset;
    }

    /**
     * Prepares class labels from the dataset for SVM training.
     *
     * @param datasetDSArray Array of DataSetDS instances.
     * @return Array of ClassLabelDS instances.
     */
    public static ClassLabelDS[] prepClassLabels(DataSetDS[] datasetDSArray) {
        ClassLabelDS[] classLabels = new ClassLabelDS[10];

        // Initialize class labels.
        for (int classIndex = 0; classIndex < classLabels.length; classIndex++) {
            classLabels[classIndex] = new ClassLabelDS(classIndex, new int[0][]);
        }

        // Assign images to the corresponding class label.
        for (DataSetDS dataSetDS : datasetDSArray) {
            ClassLabelDS.appendImage(classLabels[dataSetDS.getLabel()], dataSetDS.getImage());
        }
        return classLabels;
    }

    /**
     * Splits class labels into training and testing datasets based on a given percentage.
     *
     * @param classLabels            Array of ClassLabelDS instances.
     * @param trainingDataPercentage Percentage of data to be used for training.
     * @return 2D array containing training and testing datasets.
     */
    public static ClassLabelDS[][] getTrainingTestingData(ClassLabelDS[] classLabels, int trainingDataPercentage) {
        ClassLabelDS[] trainingData = new ClassLabelDS[10];
        ClassLabelDS[] testingData = new ClassLabelDS[10];

        // Initialize training and testing datasets.
        for (int classIndex = 0; classIndex < 10; classIndex++) {
            trainingData[classIndex] = new ClassLabelDS(classIndex, new int[0][]);
            testingData[classIndex] = new ClassLabelDS(classIndex, new int[0][]);
        }

        // Split each class label's images into training and testing sets.
        for (ClassLabelDS classLabel : classLabels) {
            int[][] images = classLabel.getImages();
            int trainingSize = (int) Math.ceil(images.length * (trainingDataPercentage / 100.0));
            int testingSize = images.length - trainingSize;

            // Assign images to training dataset.
            for (int i = 0; i < trainingSize; i++) {
                ClassLabelDS.appendImage(trainingData[classLabel.getLabel()], images[i]);
            }
            // Assign images to testing dataset.
            for (int i = trainingSize; i < images.length; i++) {
                ClassLabelDS.appendImage(testingData[classLabel.getLabel()], images[i]);
            }
        }
        return new ClassLabelDS[][]{trainingData, testingData};
    }
}