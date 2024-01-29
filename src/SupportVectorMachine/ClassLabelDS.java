package SupportVectorMachine;

final class ClassLabelDS {
    private final int label;
    private int[][] images;

    ClassLabelDS(int label, int[][] images) {
        this.label = label;
        this.images = images;
    }

    int getLabel() {
        return label;
    }

    int[][] getImages() {
        return images;
    }

    static void appendImage(ClassLabelDS classLabel, int[] image) {
        int[][] images = classLabel.getImages();
        int[][] newImages = new int[images.length + 1][];
        for (int i = 0; i < images.length; i++) {
            newImages[i] = images[i];
        }
        newImages[images.length] = image;
        classLabel.images = newImages;
    }

}
