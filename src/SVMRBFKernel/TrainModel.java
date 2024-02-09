package SVMRBFKernel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainModel {
    private SVMClassifier svmClassifier;
    private String csvFilePath;

    public TrainModel(String csvFilePath, String name, double C, double gamma, int positive_output) {
        this.csvFilePath = csvFilePath;
        System.out.println("Reading CSV file...");
        double[][] dataset = readCSV();
        System.out.println("CSV file read successfully. Dataset size: " + dataset.length);
        this.svmClassifier = new SVMClassifier(name, dataset, C, gamma, positive_output);
    }

    private double[][] readCSV() {
        List<double[]> dataset = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double[] row = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = Double.parseDouble(values[i]);
                }
                dataset.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataset.toArray(new double[0][]);
    }

    public void trainModel() {
        System.out.println("Starting model training...");
        long startTime = System.currentTimeMillis();
        svmClassifier.trainModel();
        long endTime = System.currentTimeMillis();
        System.out.println("Model training completed. Training time: " + (endTime - startTime) + " ms");
    }

    public void evaluateModel(double[][] test_dataset) {
        System.out.println("Starting model evaluation...");
        int correctPredictions = 0;
        for (double[] instance : test_dataset) {
            double[] features = Arrays.copyOfRange(instance, 0, 64);
            int actualLabel = (int) instance[64];
            int predictedLabel = svmClassifier.predict(features);
            if (actualLabel == predictedLabel) {
                correctPredictions++;
            }
        }
        double accuracy = (double) correctPredictions / test_dataset.length;
        System.out.println("Model evaluation completed. Model accuracy: " + accuracy);
    }

    public static void main(String[] args) {
        String csvFilePath = "/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet1.csv";
        double C = 0.01;
        double gamma = 0.5;
        int positive_output = 1;
        TrainModel trainModel = new TrainModel(csvFilePath, "SVM", C, gamma, positive_output);
        trainModel.trainModel();
        double[][] test_dataset = trainModel.readCSV();
        trainModel.evaluateModel(test_dataset);
    }
}