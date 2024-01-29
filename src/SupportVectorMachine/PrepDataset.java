package SupportVectorMachine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PrepDataset {
    // Prepare Dataset in the DatasetDS format.
    public static DataSetDS[] prepDataset(String datasetPath) throws IOException {
        int[][] dataset = readDataset(datasetPath);
        // Last column is the label; rest are the image.
        DataSetDS[] datasetDS = new DataSetDS[dataset.length];
        for (int i = 0; i < dataset.length; i++) {
            int[] image = new int[dataset[i].length - 1];
            for (int j = 0; j < dataset[i].length - 1; j++) {
                image[j] = dataset[i][j];
            }
            datasetDS[i] = new DataSetDS(image, dataset[i][dataset[i].length - 1]);
        }
        return datasetDS;
    }

    // Read the dataset from the file.
    private static int[][] readDataset(String datasetPath) throws IOException {
        // Read the dataset from the file.
        List<String> lines = Files.readAllLines(Paths.get(datasetPath));
        // Initialize the dataset array.
        int[][] dataset = new int[lines.size()][];
        // Iterate through the lines and add the data to the dataset array.
        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i).split(",");
            dataset[i] = new int[line.length];
            for (int j = 0; j < line.length; j++) {
                dataset[i][j] = Integer.parseInt(line[j]);
            }
        }
        return dataset;
    }

    // Prepare Class Labels in the ClassLabelDS format.
    public static ClassLabelDS[] prepClassLabels(DataSetDS[] datasetDS) {
        // Initialize the class labels array.
        ClassLabelDS[] classLabels = new ClassLabelDS[10];
        // Initialize the class labels array.
        for (int i = 0; i < classLabels.length; i++) {
            classLabels[i] = new ClassLabelDS(i, new int[0][]);
        }
        // Iterate through the dataset and add the data to the class labels array.
        for (DataSetDS dataset : datasetDS) {
            for (ClassLabelDS classLabel : classLabels) {
                if (dataset.getLabel() == classLabel.getLabel()) {
                    ClassLabelDS.appendImage(classLabel, dataset.getImage());
                }
            }
        }
        return classLabels;
    }

    public static ClassLabelDS[][] getTrainingTestingData(ClassLabelDS[] classLabels, int trainingDataPercentage) {
        // The percentage should be processed for each class label.
        // Initialize the training data array.
        ClassLabelDS[] trainingData = new ClassLabelDS[10];
        // Initialize the training data array.
        ClassLabelDS[] testingData = new ClassLabelDS[10];

        // Initialize the training and testing data array.
        for (int i = 0; i < trainingData.length; i++) {
            trainingData[i] = new ClassLabelDS(i, new int[0][]);
            testingData[i] = new ClassLabelDS(i, new int[0][]);
        }
        // Iterate through the class labels and add the data to the training data array.
        for (ClassLabelDS classLabel : classLabels) {
            int[][] images = classLabel.getImages();

            int trainingDataSize = (int) Math.ceil(images.length * (trainingDataPercentage / 100.0));
            int testingDataSize = (int) (images.length * ((100 - trainingDataPercentage) / 100));

            // Add the training data to the training data array.
            for (int i = 0; i < trainingDataSize; i++) {
                ClassLabelDS.appendImage(trainingData[classLabel.getLabel()], images[i]);
            }
            // Add the testing data to the testing data array.
            for (int i = trainingDataSize; i < images.length; i++) {
                ClassLabelDS.appendImage(testingData[classLabel.getLabel()], images[i]);
            }
        }
        return new ClassLabelDS[][]{trainingData, testingData};
    }

    public static void main(String[] args) throws IOException {
        DataSetDS[] datasetDS = prepDataset("/home/girish/Documents/MDX/AI/Digital Recognition/Project Files/cw2DataSet1.csv");
        ClassLabelDS[] classLabels = prepClassLabels(datasetDS);

        // Print the class labels and their images in a readable format.
        System.out.println("--------------------------------------------------");
        System.out.println("Class Labels and their Images");
        for (ClassLabelDS classLabel : classLabels) {
            System.out.println(classLabel.getLabel() + ": " + classLabel.getImages().length);
        }

        // Get the training and testing data.
        System.out.println("--------------------------------------------------");
        ClassLabelDS[][] trainingTestingData = getTrainingTestingData(classLabels, 80);
        ClassLabelDS[] trainingData = trainingTestingData[0];
        ClassLabelDS[] testingData = trainingTestingData[1];

        // Print the training data.
        System.out.println("Training Data");
        for (ClassLabelDS classLabel : trainingData) {
            System.out.println(classLabel.getLabel() + ": " + classLabel.getImages().length);
        }
        System.out.println("--------------------------------------------------");
        // Print the testing data.
        System.out.println("Testing Data");
        for (ClassLabelDS classLabel : testingData) {
            System.out.println(classLabel.getLabel() + ": " + classLabel.getImages().length);
        }
    }
}
