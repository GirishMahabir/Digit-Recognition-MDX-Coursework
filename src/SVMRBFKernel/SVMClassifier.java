package SVMRBFKernel;

import java.util.Arrays;

public class SVMClassifier {
    // Parameters
    private String name;
    private double[][] train_dataset;
    private int[] expected_values;
    private double C;
    private double gamma;
    private int positive_output;
    private double[] alphas;
    private double b;
    private Optimizer optimizer;

    // Constructor
    public SVMClassifier(String name, double[][] full_dataset, double C, double gamma, int positive_output) {
        // initialize parameters
        this.name = name;
        this.C = C;
        this.gamma = gamma;
        this.positive_output = positive_output;

        // Split the full_dataset into train_dataset and expected_values
        this.train_dataset = new double[full_dataset.length][64];
        this.expected_values = new int[full_dataset.length];
        this.alphas = new double[full_dataset.length];
        this.b = 0.0;

        for (int i = 0; i < full_dataset.length; i++) {
            this.train_dataset[i] = Arrays.copyOfRange(full_dataset[i], 0, 64);
            this.expected_values[i] = (int) full_dataset[i][64];
        }

        System.out.println("Starting SVM Classifier for " + name + "...");

        // Initialize the optimizer
        this.optimizer = new Optimizer(train_dataset, expected_values, alphas, b, C, gamma);

        System.out.println("SVM Classifier initialized with parameters: C=" + C + ", gamma=" + gamma + ", positive_output=" + positive_output);
    }

    // Methods
    public void trainModel() {
        // Train the SVM, one vs. all, using RBF kernel.
        optimizer.optimize();
        alphas = optimizer.getAlphas();
        b = optimizer.getB();
    }

    public int predict(double[] features) {
        // Predict the label of a given instance
        double sum = 0;
        for (int i = 0; i < train_dataset.length; i++) {
            sum += alphas[i] * expected_values[i] * kernelFunction(train_dataset[i], features);
        }
        sum -= b;
        return (sum > 0) ? positive_output : -positive_output;
    }

    private double kernelFunction(double[] doubles, double[] features) {
        // RBF kernel function
        double sum = 0;
        for (int i = 0; i < doubles.length; i++) {
            sum += Math.pow(doubles[i] - features[i], 2);
        }
        return Math.exp(-gamma * sum);
    }

    public int getTotalInstances() {
        return train_dataset.length;
    }
}