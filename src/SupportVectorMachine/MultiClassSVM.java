package SupportVectorMachine;

/**
 * Implements a multi-class SVM classifier by utilizing multiple binary SVM classifiers,
 * one for each class, in a one-vs-all (OvA) approach.
 */
public class MultiClassSVM {
    // Array of SVMClassifier, one for each class.
    SVMClassifier[] classifiers;

    /**
     * Initializes a MultiClassSVM with specified parameters for each binary SVM classifier.
     *
     * @param numClasses      Number of classes for multi-class classification.
     * @param maxIterations   Maximum number of iterations for the SVM training process.
     * @param kernelType      Type of kernel used by the SVM (e.g., linear, polynomial).
     * @param penaltyParameter The penalty parameter C of the error term.
     * @param epsilon         Tolerance for stopping criterion.
     * @param weightThreshold Threshold for weight difference for checking convergence.
     * @param bias            Initial bias for each SVM classifier.
     * @param learningRate    Learning rate (alpha) for adjustments in training.
     */
    public MultiClassSVM(int numClasses, int maxIterations, String kernelType, double penaltyParameter, double epsilon,
                         double weightThreshold, double bias, double learningRate) {
        classifiers = new SVMClassifier[numClasses];

        // Initialize an SVMClassifier for each class.
        for (int classIndex = 0; classIndex < numClasses; classIndex++) {
//            System.out.println("Initializing classifier for class " + classIndex);
            classifiers[classIndex] = new SVMClassifier(maxIterations, kernelType, penaltyParameter, epsilon, weightThreshold, bias, learningRate, classIndex);
        }
    }

    /**
     * Trains each binary classifier on the provided dataset.
     *
     * @param trainingData The dataset used for training, containing examples for all classes.
     */
    public void train(ClassLabelDS[] trainingData) {
        // Train each classifier with the training data.
        for (SVMClassifier classifier : classifiers) {
            System.out.println("Training classifier for class " + classifier.targetClass);
            classifier.train(trainingData);
        }
    }

    /**
     * Predicts the class of a given image using the trained classifiers.
     *
     * @param image The image to classify.
     * @return The class label predicted by the multi-class SVM.
     */
    public int predict(int[] image) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestClass = -1;

        // Evaluate each classifier and select the one with the highest score.
        for (SVMClassifier classifier : classifiers) {
            double score = classifier.score(image);
            if (score > bestScore) {
                bestScore = score;
                bestClass = classifier.targetClass;
            }
        }

        return bestClass;
    }
}