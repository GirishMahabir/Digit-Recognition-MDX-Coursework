package SupportVectorMachine;

final class ClassLabelDS {
    private final int label; // Holds the label for the class
    private int[][] images; // 2D array storing images associated with the label

    /**
     * Constructor to initialize the ClassLabelDS object with a label and its associated images.
     * @param label The class label.
     * @param images The 2D array of images associated with the label.
     */
    ClassLabelDS(int label, int[][] images) {
        this.label = label;
        this.images = images;
    }

    /**
     * Initializes ClassLabelDS with a specific label and images.
     * Returns the label of this ClassLabelDS.
     * @return The label.
     */
    int getLabel() {
        return label;
    }

    /**
     * Retrieves the label of the class.
     * Returns the images associated with this ClassLabelDS.
     * @return The 2D array of images.
     */
    int[][] getImages() {
        return images;
    }

    /**
     * Retrieves the images associated with the class label.
     * Appends a single image to the array of images in the specified ClassLabelDS instance.
     * @param classLabel The ClassLabelDS instance to which the image is to be appended.
     * @param image The image to append.
     */
    static void appendImage(ClassLabelDS classLabel, int[] image) {
        int[][] images = classLabel.getImages(); // Retrieve current images
        int[][] newImages = new int[images.length + 1][]; // Create a new array with an additional slot for the new image
        for (int i = 0; i < images.length; i++) {
            newImages[i] = images[i]; // Copy existing images to the new array
        }
        newImages[images.length] = image; // Append the new image
        classLabel.images = newImages; // Update the images array in the ClassLabelDS instance
    }
}
