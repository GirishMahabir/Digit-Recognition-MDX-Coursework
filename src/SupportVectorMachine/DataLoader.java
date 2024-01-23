package SupportVectorMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    // Method to load data from a file and parse it into a list of FeatureVectors
    public static List<FeatureVector> loadData(String filePath) {
        List<FeatureVector> dataSet = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Parse each line into a FeatureVector and add it to the dataSet
                dataSet.add(parseLineToFeatureVector(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    // Helper method to parse a line from the file into a FeatureVector
    private static FeatureVector parseLineToFeatureVector(String line) {
        String[] tokens = line.split(",");
        double[] features = new double[tokens.length - 1]; // Last token is the label
        for (int i = 0; i < tokens.length - 1; i++) {
            features[i] = Double.parseDouble(tokens[i]);
        }
        int label = Integer.parseInt(tokens[tokens.length - 1]);
        return new FeatureVector(features, label);
    }
}
