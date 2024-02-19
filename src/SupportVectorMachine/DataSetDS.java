package SupportVectorMachine;

final class DataSetDS {
    /*
     * Data Structure Class to standardize how the result is returned.
     * This class encapsulates an image (represented by an array of integers) and its associated label.
     */
    private final int[] image; // Array representing the image data.
    private final int label; // Integer representing the label of the image.

    /**
     * Constructor to initialize the DataSetDS object with an image and its label.
     * This method sets up the image data and label for this data structure.
     * @param image An array of integers representing the image.
     * @param label An integer representing the label associated with the image.
     */
    public DataSetDS(int[] image, int label) {
        this.image = image;
        this.label = label;
    }

    /**
     * Returns the image data stored in this DataSetDS.
     * This method is used to retrieve the array representing the image.
     * @return An array of integers representing the image.
     */
    public int[] getImage() {
        return image;
    }

    /**
     * Returns the label associated with the image in this DataSetDS.
     * This method is used to retrieve the integer label of the image.
     * @return An integer representing the label.
     */
    public int getLabel() {
        return label;
    }
}
