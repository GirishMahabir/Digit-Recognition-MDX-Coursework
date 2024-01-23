package SupportVectorMachine;

public interface Kernel {
    double compute(FeatureVector x1, FeatureVector x2);
}