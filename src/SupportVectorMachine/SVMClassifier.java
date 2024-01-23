package SupportVectorMachine;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.Math;
import java.util.List;
import java.util.Random;

public class SVMClassifier {
    // Parameters
    // Max Iterations: The maximum number of iterations to be performed on the training data.
    private static int MAX_ITERATIONS = 10000;
    // Kernel Type: The type of kernel to be used in the SVM (linear, polynomial, radial basis function, etc.)
    private static String kernelType = "linear";
    // C: The penalty parameter for the error term.
    private static double C = 1.0;
    // Epsilon: The tolerance value for the convergence of the SVM. Regularization Parameter.
    private static double epsilon = 0.001;
    // Weight Threshold: The threshold for the weight of the support vectors. Convergence Criteria.
    private static double weightThreshold;
    // Bias: The bias value for the SVM.
    private static double bias = 0.0;
    private static final double[] weight = new double[64]; // Assuming 64 features (pixels in 8x8 image)

    // Setters and getter Functions
    public static int getMaxIterations() {
        return MAX_ITERATIONS;
    }

    public static void setMaxIterations(int maxIterations) {
        MAX_ITERATIONS = maxIterations;
    }

    public static String getKernelType() {
        return kernelType;
    }

    public static void setKernelType(String kernelType) {
        SVMClassifier.kernelType = kernelType;
    }

    public static double getC() {
        return C;
    }

    public static void setC(double c) {
        C = c;
    }

    public static double getEpsilon() {
        return epsilon;
    }

    public static void setEpsilon(double epsilon) {
        SVMClassifier.epsilon = epsilon;
    }

    public static double getWeightThreshold() {
        return weightThreshold;
    }

    public static void setWeightThreshold(double weightThreshold) {
        SVMClassifier.weightThreshold = weightThreshold;
    }

    public static double getBias() {
        return bias;
    }

    public static void setBias(double bias) {
        SVMClassifier.bias = bias;
    }

    public SVMClassifier(int maxIterations, String kernelType, double c, double epsilon, double weightThreshold, double bias) {
        MAX_ITERATIONS = maxIterations;
        SVMClassifier.kernelType = kernelType;
        C = c;
        SVMClassifier.epsilon = epsilon;
        SVMClassifier.weightThreshold = weightThreshold;
        SVMClassifier.bias = bias;
    }

    // Initialize the weight and bias values
    public static void initialize() {
        /*
          Function to adjust the weight and bias values of the SVM.
          - This is done by solving an optimization problem that maximizes the margin between the different support vectors.
          - The margin is defined as the distance between the hyperplane and the nearest data points from each support vectors.
          - Initial values of the weight and bias are set randomly or if the user has provided them, they are used.
          - However, the weight and bias values are adjusted in the training phase.
        */
        if (weightThreshold == 0.0) {
            Random random = new Random();
            for (int i = 0; i < weight.length; i++) {
                weight[i] = random.nextDouble();
            }
        }
    }

    // Train the SVM model
    public void train(List<FeatureVector> trainingData) {
        // Initialize weights and bias
        for (int i = 0; i < weight.length; i++) {
            weight[i] = 0.0;
        }
        bias = 0.0;

        // Implement the training logic (e.g., using SMO)
        // Simplified version of the training process.
        // Real implementation should use an optimization algorithm like SMO
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            for (FeatureVector vector : trainingData) {
                double[] features = vector.getFeatures();
                int label = vector.getLabel();

                if (label * (dotProduct(weight, features) + bias) <= 0) {
                    for (int j = 0; j < weight.length; j++) {
                        weight[j] += epsilon * label * features[j];
                    }
                    bias += epsilon * label;
                }
            }
        }
    }

    // Predict the label of a new data point
    public int predict(FeatureVector inputData) {
        double sum = dotProduct(weight, inputData.getFeatures()) + bias;
        return sum >= 0 ? 1 : -1;
    }

    // Calculate the kernel between two vectors
    private double kernel(FeatureVector x1, FeatureVector x2) {
        switch (kernelType) {
            case "linear":
                return linearKernel(x1, x2);
            // Add other cases for different kernel types
            default:
                return linearKernel(x1, x2);
        }
    }

    // Linear kernel function
    private double linearKernel(FeatureVector x1, FeatureVector x2) {
        return dotProduct(x1.getFeatures(), x2.getFeatures());
    }

    // Utility function for dot product
    private double dotProduct(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

}



