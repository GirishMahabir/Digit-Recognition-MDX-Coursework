package SupportVectorMachine;

import java.util.Arrays;

/**
 * A classifier based on the Support Vector Machine (SVM) algorithm.
 * This implementation includes configurable parameters for different types of kernels,
 * regularization, convergence criteria, and more.
 */
public class SVMClassifier {
    // Maximum number of iterations for the training process.
    private final int maxIterations;
    // Type of kernel to be used (linear, polynomial, radial basis function, etc.).
    private final String kernelType;
    // Penalty parameter of the error term.
    private final double penaltyParameter;
    // Tolerance for stopping criterion.
    private final double epsilon;
    // Threshold for weight difference for convergence.
    private final double weightThreshold;
    // Learning rate.
    private final double learningRate;
    // Bias term in the decision function.
    double bias;
    // Weights of the features in the decision function.
    double[] weights;
    // Previous iteration weights, for convergence check.
    private double[] previousWeights;
    // Previous iteration bias, for convergence check.
    private double previousBias;
    // Target class for binary classification in a multi-class setup.
    final int targetClass;

    /**
     * Constructs an SVMClassifier with specified parameters.
     *
     * @param maxIterations    Maximum number of training iterations.
     * @param kernelType       Kernel type for the SVM.
     * @param penaltyParameter Penalty parameter C for the error term.
     * @param epsilon          Tolerance for convergence.
     * @param weightThreshold  Threshold for weight update difference for convergence.
     * @param bias             Initial bias.
     * @param learningRate     Learning rate (alpha) for weight and bias updates.
     * @param targetClass      Target class for One-vs-Rest classification.
     */
    public SVMClassifier(int maxIterations, String kernelType, double penaltyParameter, double epsilon, double weightThreshold,
                         double bias, double learningRate, int targetClass) {
        this.maxIterations = maxIterations;
        this.kernelType = kernelType;
        this.penaltyParameter = penaltyParameter;
        this.epsilon = epsilon;
        this.weightThreshold = weightThreshold;
        this.bias = bias;
        this.weights = new double[64]; // Assumes feature vector size of 64, adjust as necessary.
        this.previousWeights = new double[64];
        Arrays.fill(this.previousWeights, Double.MAX_VALUE); // Initialize with large values for convergence check.
        this.previousBias = Double.MAX_VALUE;
        this.learningRate = learningRate;
        this.targetClass = targetClass;
    }

    /**
     * Trains the SVM classifier with the provided training data.
     *
     * @param trainingData Array of training data instances.
     */
    public void train(ClassLabelDS[] trainingData) {
        // Iterate through each training epoch.
        for (int epoch = 0; epoch < maxIterations; epoch++) {
            // Iterate through each data instance in training data.
            for (ClassLabelDS dataInstance : trainingData) {
                int[][] images = dataInstance.getImages();
                int label = (dataInstance.getLabel() == targetClass) ? 1 : -1; // Adjust label for binary classification.

                // Iterate through each image in the current data instance.
                for (int[] image : images) {
                    double[] features = convertToIntArray(image);
                    double prediction = applyKernel(features) - bias;

                    // Update weights and bias for each feature in the image.
                    if (label * prediction < 1) {
                        for (int featureIndex = 0; featureIndex < weights.length; featureIndex++) {
                            weights[featureIndex] += learningRate * (penaltyParameter * (label * features[featureIndex] - 2 * 1.0 / maxIterations * weights[featureIndex]));
                        }
                        bias += learningRate * penaltyParameter * label;
                    } else {
                        for (int featureIndex = 0; featureIndex < weights.length; featureIndex++) {
                            weights[featureIndex] -= penaltyParameter * 2 * 1.0 / maxIterations * weights[featureIndex];
                        }
                    }
                }
            }
            // if epoch > 10000 and no convergence, break
            if (epoch > 50000) {
                System.out.println("Convergence ends at epoch: " + epoch);
                break;
            }

            if (hasConverged()) {
                System.out.println("Converged at epoch: " + epoch);
                break;
            }
            // Prepare for the next iteration by updating previous weights and bias.
            System.arraycopy(weights, 0, previousWeights, 0, weights.length);
            previousBias = bias;
        }
    }

    /**
     * Converts an array of integers to an array of doubles.
     *
     * @param array The integer array to convert.
     * @return The double array.
     */
    private double[] convertToIntArray(int[] array) {
        // Convert each integer in the array to double.
        double[] doubleArray = new double[array.length];
        for (int index = 0; index < array.length; index++) {
            doubleArray[index] = array[index];
        }
        return doubleArray;
    }

    /**
     * Applies the selected kernel to the input features.
     *
     * @param features The feature vector.
     * @return The result of the kernel function.
     */
    private double applyKernel(double[] features) {
        // Currently, this acts as a linear kernel. Extend to include other kernel types.
        return dotProduct(features);
    }

    /**
     * Calculates the dot product of the weights and input features.
     *
     * @param features The feature vector.
     * @return The dot product.
     */
    private double dotProduct(double[] features) {
        double sum = 0;
        // Calculate the dot product of weights and features.
        for (int featureIndex = 0; featureIndex < weights.length; featureIndex++) {
            sum += weights[featureIndex] * features[featureIndex];
        }
        return sum;
    }

    /**
     * Computes the score for a given image.
     *
     * @param image The image to compute the score for.
     * @return The raw decision function value.
     */
    public double score(int[] image) {
        double[] features = convertToIntArray(image);
        return applyKernel(features) - bias;
    }

    /**
     * Checks if the classifier has converged based on weight and bias changes.
     *
     * @return true if converged, false otherwise.
     */
    private boolean hasConverged() {
        double maxWeightDiff = 0.0;
        // Check the maximum difference in weights and bias from the previous iteration.
        for (int weightIndex = 0; weightIndex < weights.length; weightIndex++) {
            maxWeightDiff = Math.max(maxWeightDiff, Math.abs(weights[weightIndex] - previousWeights[weightIndex]));
        }
        double biasDiff = Math.abs(bias - previousBias);

        return maxWeightDiff < weightThreshold && biasDiff < epsilon;
    }
}