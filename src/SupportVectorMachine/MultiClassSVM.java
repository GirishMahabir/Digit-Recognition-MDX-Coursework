package SupportVectorMachine;

public class MultiClassSVM {
    SVMClassifier[] classifiers;

    public MultiClassSVM(int numClasses, int MAX_ITERATIONS, String kernelType, double C, double epsilon, double weightThreshold, double bias, double alpha) {
        classifiers = new SVMClassifier[numClasses];

        for (int i = 0; i < numClasses; i++) {
            System.out.println("Initializing classifier for class " + i);
            classifiers[i] = new SVMClassifier(MAX_ITERATIONS, kernelType, C, epsilon, weightThreshold, bias, alpha, i);
        }

    }

    public void train(ClassLabelDS[] trainingData) {
        for (SVMClassifier classifier : classifiers) {
            System.out.println("Training classifier for class " + classifier.targetClass);
            classifier.train(trainingData);
        }
    }

    public int predict(int[] image) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestClass = -1;

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
