package SupportVectorMachine;

public class LinearKernel implements Kernel {
    @Override
    public double compute(FeatureVector x1, FeatureVector x2) {
        // Implement the linear kernel computation
        double sum = 0.0;
        for (int i = 0; i < x1.getFeatures().length; i++) {
            sum += x1.getFeatures()[i] * x2.getFeatures()[i];
        }
        return sum;
    }
}