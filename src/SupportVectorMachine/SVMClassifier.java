package SupportVectorMachine;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;
import javax.swing.*;
import java.util.Arrays;


public class SVMClassifier {
    // Parameters
    // Max Iterations: The maximum number of iterations to be performed on the training data.
    private final int MAX_ITERATIONS;
    // Kernel Type: The type of kernel to be used in the SVM (linear, polynomial, radial basis function, etc.)
    private final String kernelType;
    // C: The penalty parameter for the error term.
    private final double C;
    // Epsilon: The tolerance value for the convergence of the SVM. Regularization Parameter.
    private final double epsilon;
    // Weight Threshold: The threshold for the weight of the support vectors. Convergence Criteria.
    private final double weightThreshold;
    private final double alpha;
    // Bias: The bias value for the SVM.
    double bias;
    double[] weight; // Dynamic size based on input features
    private double[] prevWeight;
    private double prevBias;
    final int targetClass;
    private SwingWrapper<XYChart> swingWrapper;
    private JFrame displayFrame;

    public SVMClassifier(int MAX_ITERATIONS, String kernelType, double c, double epsilon, double weightThreshold,
                         double bias, double alpha, int targetClass) {
        this.MAX_ITERATIONS = MAX_ITERATIONS;
        this.kernelType = kernelType;
        this.C = c;
        this.epsilon = epsilon;
        this.weightThreshold = weightThreshold;
        this.bias = bias;
        this.weight = new double[64]; // Initialize based on the feature size
        this.prevWeight = new double[64]; // Initialize based on the feature size
        Arrays.fill(this.prevWeight, Double.MAX_VALUE); // Initialize with large value
        this.prevBias = Double.MAX_VALUE; // Initialize with large value
        this.alpha = alpha;
        this.targetClass = targetClass;
    }

    // Training method
    public void train(ClassLabelDS[] trainingData) {
        for (int epoch = 0; epoch < MAX_ITERATIONS; epoch++) {
            for (ClassLabelDS classLabel : trainingData) {
                int[][] images = classLabel.getImages();
//                int y = classLabel.getLabel();
                int y = (classLabel.getLabel() == targetClass) ? 1 : -1; // Adjust label for OvR

                for (int[] image : images) {
                    double[] x = convertToDouble(image);
                    double prediction = applyKernel(x) - bias;

                    if (y * prediction < 1) {
                        // Misclassified, update weight and bias
                        for (int j = 0; j < weight.length; j++) {
//                            weight[j] += C * (y * x[j] - 2 * 1.0 / MAX_ITERATIONS * weight[j]);
                            weight[j] += alpha * (C * (y * x[j] - 2 * 1.0 / MAX_ITERATIONS * weight[j]));
                        }
//                        bias += C * y;
                        bias += alpha * C * y;
                    } else {
                        // Correct classification, update only weight
                        for (int j = 0; j < weight.length; j++) {
                            weight[j] -= C * 2 * 1.0 / MAX_ITERATIONS * weight[j];
                        }
                    }
                }
            }
            if (hasConverged()) {
                System.out.println("Converged at epoch: " + epoch);
                break;
            }
            System.arraycopy(weight, 0, prevWeight, 0, weight.length);
            prevBias = bias;
        }
//        plotTrainingData(trainingData);
    }

    // Helper method to convert int[] to double[]
    private double[] convertToDouble(int[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    // Kernel application function
    private double applyKernel(double[] x) {
        // Placeholder for kernel computation, currently acts as a linear kernel
        // To add: Implement other types of kernels based on the value of kernelType
        return dotProduct(x);
    }

    // Dot product function
    private double dotProduct(double[] x) {
        double sum = 0;
        for (int i = 0; i < weight.length; i++) {
            sum += weight[i] * x[i];
        }
        return sum;
    }

    // Prediction method
    public int predict(int[] image) {
        double[] x = convertToDouble(image);
        double result = applyKernel(x) - bias;
        return result > 0 ? 1 : -1;
    }

    public double score(int[] image) {
        double[] x = convertToDouble(image);
        return applyKernel(x) - bias; // raw decision function value
    }

    private boolean hasConverged() {
        double maxWeightDiff = 0.0;
        for (int i = 0; i < weight.length; i++) {
            maxWeightDiff = Math.max(maxWeightDiff, Math.abs(weight[i] - prevWeight[i]));
        }
        double biasDiff = Math.abs(bias - prevBias);

        return maxWeightDiff < weightThreshold && biasDiff < epsilon;
    }

    public void plotTrainingData(ClassLabelDS[] trainingData) {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title("SVM Training Visualization").xAxisTitle("Feature 1").yAxisTitle("Feature 2").build();

        // Customize Chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setMarkerSize(6);

        // Collect data for each class label
        for (ClassLabelDS classLabel : trainingData) {
            int[][] images = classLabel.getImages();
            double[] xData = new double[images.length];
            double[] yData = new double[images.length];

            for (int i = 0; i < images.length; i++) {
                // Assuming the first two features are the most significant for visualization
                xData[i] = images[i][0];
                yData[i] = images[i][1];
            }

            // Add series for each class label
            XYSeries series = chart.addSeries("Class " + classLabel.getLabel(), xData, yData);
            series.setMarker(SeriesMarkers.CIRCLE);
        }

        // If there's an existing display, dispose of it.
        if (displayFrame != null) {
            displayFrame.dispose();
        }

        // Show chart
        swingWrapper = new SwingWrapper<>(chart);
        displayFrame = swingWrapper.displayChart();

        // Make the JFrame close when the next one opens
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
