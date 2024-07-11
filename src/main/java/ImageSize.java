// package in.gov.uidai.packetcompressor.utils.headeraddition;

public class ImageSize {
    private static ImageSize ins = new ImageSize();

    int width;

    int height;

    public static ImageSize getIns() {
        return ins;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
