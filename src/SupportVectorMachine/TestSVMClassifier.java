package SupportVectorMachine;

import java.io.IOException;

/**
 * Demonstrates the setup, training, and testing of a multi-class SVM classifier.
 * This class is designed to classify digits (0-9) using a dataset split into training and testing sets.
 */
public class TestSVMClassifier {

    /**
     * Main method to execute the SVM classification process.
     * It initializes the SVM with specific parameters, loads the training and testing datasets,
     * trains the SVM model, evaluates its performance on the testing set, and prints the accuracy.
     *
     * @param args Command line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        // Define SVM parameters for digit recognition (0-9)
        MultiClassSVM multiClassSVM = getMultiClassSVM();

        // Specify paths to the training and testing datasets
        String datasetPathTrain = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet1.csv";
        String datasetPathTest = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet2.csv";

        try {
            // Load and prepare the training dataset
            DataSetDS[] trainDataset = PrepDataset.prepDataset(datasetPathTrain);
            ClassLabelDS[] trainClassLabels = PrepDataset.prepClassLabels(trainDataset);

            // Load and prepare the testing dataset
            DataSetDS[] testDataset = PrepDataset.prepDataset(datasetPathTest);
            ClassLabelDS[] testClassLabels = PrepDataset.prepClassLabels(testDataset);

            // Train the SVM model with the prepared training data
            multiClassSVM.train(trainClassLabels);

            // Evaluate the trained model with the testing data
            int correctPredictions = 0;
            int totalPredictions = 0;
            for (ClassLabelDS testLabel : testClassLabels) {
                int[][] testImages = testLabel.getImages();
                for (int[] image : testImages) {
                    int predictedLabel = multiClassSVM.predict(image);
                    if (predictedLabel == testLabel.getLabel()) {
                        correctPredictions++;
                    }
                    totalPredictions++;
                }
            }

            // Calculate and display the accuracy of the model
            double accuracy = (double) correctPredictions / totalPredictions;
            System.out.println("Model accuracy: " + (accuracy * 100) + "%");

        } catch (IOException e) {
            System.err.println("Failed to load datasets.");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the multi-class SVM classifier with specific parameters.
     *
     * @return MultiClassSVM instance with specified parameters.
     */
    private static MultiClassSVM getMultiClassSVM() {
        int numClasses = 10;
        int maxIterations = 10000000;
        String kernelType = "linear";
        double penaltyParameter = 0.85;
        double epsilon = 0.00001;
        double weightThreshold = 0.00001;
        double initialBias = 0;
        double learningRate = 0.000001;

        // Initialize the multi-class SVM classifier with the specified parameters
        MultiClassSVM multiClassSVM = new MultiClassSVM(numClasses, maxIterations, kernelType, penaltyParameter, epsilon, weightThreshold, initialBias, learningRate);
        return multiClassSVM;
    }
}