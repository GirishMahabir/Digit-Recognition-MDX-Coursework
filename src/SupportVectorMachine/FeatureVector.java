package SupportVectorMachine;

public class FeatureVector {
    private double[] features; // Array to store feature values
    private int label; // The label of the vector (digit)

    // Constructor to create a FeatureVector with features and label
    public FeatureVector(double[] features, int label) {
        this.features = features;
        this.label = label;
    }

    // Getter for features
    public double[] getFeatures() {
        return features;
    }

    // Getter for label
    public int getLabel() {
        return label;
    }

    // Setter for features (optional)
    public void setFeatures(double[] features) {
        this.features = features;
    }

    // Setter for label (optional)
    public void setLabel(int label) {
        this.label = label;
    }
}
