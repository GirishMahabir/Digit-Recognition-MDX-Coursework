# Digits Recognition Coursework
## Coursework Overview
- **Subject:** Machine Learning
- **Due Date:** Week 19 (February 22nd, 2024)
- **Contribution to Overall Course Mark:** 40%

## Task Description
- Develop a machine learning system from scratch.
- Categorize one of the UCI digit tasks using the Optical Recognition of Handwritten Digits Data Set.
- Use converted data sets: cw2 data set 1 and cw2 data set 2.
- Perform a two-fold test and report the results.

## Development Requirements
- Code should be written in Java.
- Must run in Eclipse.
- If using existing algorithms, reference them in both the code and the report.

## Marking Scheme (Total: 100 Points)
1. **Report (20 Points)**
2. **Running Code (20 Points)**
3. **Quality of Code (20 Points)**
4. **Quality of Algorithm (20 Points)**
5. **Quality of Results (20 Points)**

## Report Guidelines
- Length: 1-2 pages.
- Include a description of the chosen algorithm and the rationale behind this choice.
- Show results of the two-fold test.
- Discuss data usage briefly.

## Coding and Algorithm Tips
- Ensure the code is well-commented and structured.
- A balance between simple and complex algorithms is crucial.
- Aim for surpassing the baseline performance reported on the UCI website.
- Starting with a nearest neighbour algorithm using Euclidean distance is suggested.

## Submission Requirements
- Submit code, a mark sheet, and analysis to the coursework folder of CST 3170 on myunihub.
- Optionally, email a copy to the tutor.

## Additional Notes
- This is not a competition; discussing with colleagues can be beneficial.
- The baseline for passing includes implementing a nearest neighbour algorithm with Euclidean distance.


# Resources:
- SVM: https://scikit-learn.org/stable/auto_examples/classification/plot_digits_classification.html
- https://www.kaggle.com/code/jnikhilsai/digit-classification-using-svm-on-mnist-dataset/notebook


# SVM Notes
- The primary objective of SVM is to find a hyperplane that maximally separates the different classes in the dataset.
- The "weight(w)" vector determines the orientation of the hyperplane, while the "bias(b)" term determines the offset of the hyperplane from the origin.

# RBF Notes
- K(x,x') = exp(-gamma||x-x'||^2)
- Gamma is a hyperparameter that determines the spread of the Gaussian around the support vectors.
  - A small gamma value will result in a large variance and a small bias, while a large gamma value will result in a small variance and a large bias.
  - The gamma parameter can be thought of as the inverse of the radius of influence of samples selected by the model as support vectors.
  - Recommended: 0.5
- C is the regularization parameter, which controls the trade-off between achieving a low training error and a low testing error that is the ability to generalize your classifier to unseen data.
  - A large C value will result in a small margin and a small number of support vectors, while a small C value will result in a large margin and a large number of support vectors.
  - Recommended: 0.01
- Optimizer: 

0.8424821002386634
- Max_Itterations: 100000
- Kernel: linear
- C: 0.5
- epsilon: 0.0001
- weightThreshold: 0.0001
- bias: 0.0
- alpha: 0.00001
- gamma: 0.001

0.9594272076372315
- Max_Itterations: 100000000
- Kernel: linear
- C: 0.85
- epsilon: 0.00001
- weightThreshold: 0.00001
- bias: 0
- alpha: 0.000001
- gamma: 0.5








# All code commented, All classes with docstrings, no 1 letter vars.
# Function Length < 50 lines.
# 2Fold test, use dataset 1 for test and dataset 2 for train and vice versa.


        int numClasses = 10; // for digits 0-9
        int MAX_ITERATIONS = 100000000;
        String kernelType = "linear";
        double C = 0.85;
        double epsilon = 0.00001;
        double weightThreshold = 0.00001;
        double bias = 0;
        double alpha = 0.000001;
        double gamma = 0.5;

Accuracy: 95.62277580071175%



















