package SupportVectorMachine;

import java.io.IOException;
import java.util.Arrays;

public class TestSVMClassifier {

    public static void main(String[] args) {
        // Set SVM parameters
        int numClasses = 10; // for digits 0-9
        int MAX_ITERATIONS = 100000;
        String kernelType = "linear";
        double C = 0.5;
        double epsilon = 0.0001;
        double weightThreshold = 0.0001;
        double bias = 0;
        double alpha = 0.00001;
        double gamma = 0.001;

        // Initialize the MultiClassSVM
        MultiClassSVM multiClassSVM = new MultiClassSVM(numClasses, MAX_ITERATIONS, kernelType, C, epsilon, weightThreshold, bias, alpha, gamma);

        // Load and prepare the dataset
        try {
            String datasetPath = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet1.csv";
            DataSetDS[] datasetDS = PrepDataset.prepDataset(datasetPath);
            ClassLabelDS[] classLabels = PrepDataset.prepClassLabels(datasetDS);

            // Split the dataset into training and testing sets
            int trainingDataPercentage = 70;
            ClassLabelDS[][] splitData = PrepDataset.getTrainingTestingData(classLabels, trainingDataPercentage);
            ClassLabelDS[] trainingData = splitData[0];
            System.out.println("Training data size: " + trainingData.length);
            ClassLabelDS[] testingData = splitData[1];
            System.out.println("Testing data size: " + testingData.length);

            // Train the model
            multiClassSVM.train(trainingData);

            for (SVMClassifier classifier : multiClassSVM.classifiers) {
                System.out.println("Classifier for class " + classifier.targetClass);
                System.out.println("Weights: " + Arrays.toString(Arrays.stream(classifier.weight)
                      .filter(weight -> weight > 0)
                      .toArray()));
                System.out.println("Bias: " + classifier.bias);
            }


            // Test the model
            int correctPredictions = 0;
            int totalPredictions = 0;
            for (ClassLabelDS testingClass : testingData) {
                int[][] images = testingClass.getImages();
                for (int[] image : images) {
                    int predictedLabel = multiClassSVM.predict(image);
                    if (predictedLabel == testingClass.getLabel()) {
                        correctPredictions++;
                    }
                    totalPredictions++;
                }
            }

            // Calculate and print the accuracy
            double accuracy = (double) correctPredictions / totalPredictions;
            System.out.println("Accuracy: " + accuracy);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
