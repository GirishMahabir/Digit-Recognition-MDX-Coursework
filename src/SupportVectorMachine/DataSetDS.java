package SupportVectorMachine;

final class DataSetDS {
    /*
      * Data Structure Class to standardize how the result is returned.
    */
    private final int[] image;
    private final int label;

    public DataSetDS(int[] image, int label) {
        this.image = image;
        this.label = label;
    }

    public int[] getImage() {
        return image;
    }

    public int getLabel() {
        return label;
    }

}
