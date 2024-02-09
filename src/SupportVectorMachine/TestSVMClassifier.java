//package SupportVectorMachine;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//public class TestSVMClassifier {
//
//    public static void main(String[] args) {
//        // Set SVM parameters
//        int numClasses = 10; // for digits 0-9
//        int MAX_ITERATIONS = 100000000;
//        String kernelType = "linear";
//        double C = 0.85;
//        double epsilon = 0.00001;
//        double weightThreshold = 0.00001;
//        double bias = 0;
//        double alpha = 0.000001;
//        double gamma = 0.5;
//
//        // Initialize the MultiClassSVM
//        MultiClassSVM multiClassSVM = new MultiClassSVM(numClasses, MAX_ITERATIONS, kernelType, C, epsilon, weightThreshold, bias, alpha, gamma);
//
//        // Load and prepare the dataset
//        try {
//            String datasetPathTrain = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet1.csv";
//            String datasetPathTest = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet2.csv";
//
//            // Load and prepare training dataset
//            DataSetDS[] datasetDSTrain = PrepDataset.prepDataset(datasetPathTrain);
//            ClassLabelDS[] trainingData = PrepDataset.prepClassLabels(datasetDSTrain);
//            System.out.println("Training data size: " + trainingData.length);
//
//            // Load and prepare testing dataset
//            DataSetDS[] datasetDSTest = PrepDataset.prepDataset(datasetPathTest);
//            ClassLabelDS[] testingData = PrepDataset.prepClassLabels(datasetDSTest);
//            System.out.println("Testing data size: " + testingData.length);
//
//            // Train the model
//            multiClassSVM.train(trainingData);
//
//            // Print classifier details (weights and bias)
//            // ... [Same as before]
//
//            // Test the model
//            int correctPredictions = 0;
//            int totalPredictions = 0;
//            for (ClassLabelDS testingClass : testingData) {
//                int[][] images = testingClass.getImages();
//                for (int[] image : images) {
//                    int predictedLabel = multiClassSVM.predict(image);
//                    if (predictedLabel == testingClass.getLabel()) {
//                        correctPredictions++;
//                    }
//                    totalPredictions++;
//                }
//            }
//
//            // Calculate and print the accuracy
//            double accuracy = (double) correctPredictions / totalPredictions;
//            System.out.println("Accuracy: " + accuracy);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
