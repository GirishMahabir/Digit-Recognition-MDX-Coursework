package SupportVectorMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Path to your dataset file
//        String filePath = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/digits.csv";
        String filePath = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet1.csv";

        // Load the dataset
        List<FeatureVector> dataSet = DataLoader.loadData(filePath);

        // Shuffle the dataset to ensure a good mix of digits
        Collections.shuffle(dataSet);

        // SHUFFLE SECTION
        // List<FeatureVector> shuffledDataSet = new ArrayList<>();
        // for (int i = 0; i < 10; i++) {
        //     for (int j = i; j < dataSet.size(); j += 10) {
        //         shuffledDataSet.add(dataSet.get(j));
        //     }
        // }
        // dataSet = shuffledDataSet;

        // Splitting the dataset into training and testing sets
        // Let's say 80% for training and 20% for testing
        int splitIndex = (int) (dataSet.size() * 0.8);
        List<FeatureVector> trainingSet = dataSet.subList(0, splitIndex);
        List<FeatureVector> testingSet = dataSet.subList(splitIndex, dataSet.size());

        // Initialize the SVM Classifier
        SVMClassifier svmClassifier = new SVMClassifier(10000, "linear", 1.0, 0.001, 0.0, 0.0);

        // Train the classifier
        System.out.println("Training the SVM classifier...");
        svmClassifier.train(trainingSet);
        System.out.println("Training complete.");

        // Test the classifier
        System.out.println("Testing the SVM classifier...");
        int correctPredictions = 0;
        for (FeatureVector vector : testingSet) {
            int predictedLabel = svmClassifier.predict(vector);
            if (predictedLabel == vector.getLabel()) {
                correctPredictions++;
            }
        }

        // Calculate and print the accuracy
        double accuracy = 100.0 * correctPredictions / testingSet.size();
        System.out.printf("Accuracy: %.2f%%\n", accuracy);
    }
}
