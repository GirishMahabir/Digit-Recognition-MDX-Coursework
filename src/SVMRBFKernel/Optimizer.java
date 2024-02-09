package SVMRBFKernel;

public class Optimizer {
    private double[][] dataset;
    private int[] expected_values;
    private double[] alphas;
    private double b;
    private double C;
    private double gamma;

    public Optimizer(double[][] dataset, int[] expected_values, double[] alphas, double b, double C, double gamma) {
        this.dataset = dataset;
        this.expected_values = expected_values;
        this.alphas = alphas;
        this.b = b;
        this.C = C;
        this.gamma = gamma;
    }

    public void optimize() {
        int n = dataset.length;
        System.out.println("Dataset size: " + n);
        System.out.println("Starting optimization...");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[] x = dataset[i];
                double[] y = dataset[j];
                double kernelValue = kernelFunction(x, y);
                alphas[j] = optimizeAlpha(alphas[j], expected_values[j], kernelValue);
            }
            System.out.println("Iteration " + i + " completed.");
        }
        b = calculateBias();
    }

    private double kernelFunction(double[] x, double[] y) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] - y[i], 2);
        }
        return Math.exp(-gamma * sum);
    }

    private double optimizeAlpha(double alpha, int y, double kernelValue) {
        double E = b + sumAlphaKernelY() - y;
        alpha = alpha + C * E * kernelValue;
        return alpha;
    }

    private double calculateBias() {
        double sum = 0;
        for (int i = 0; i < dataset.length; i++) {
            sum += (alphas[i] * expected_values[i] * kernelFunction(dataset[i], dataset[i]));
        }
        return -sum;
    }

    private double sumAlphaKernelY() {
        double sum = 0;
        for (int i = 0; i < dataset.length; i++) {
            sum += (alphas[i] * expected_values[i] * kernelFunction(dataset[i], dataset[i]));
        }
        return sum;
    }

    public double[] getAlphas() {
        return alphas;
    }


    public double getB() {
        return b;
    }
}