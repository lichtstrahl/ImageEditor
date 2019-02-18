package root.iv.imageeditor.util;

public class Pixel {
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public Pixel(int pixel) {
        this.alpha  = pixel & 0xFF000000;
        this.red    = pixel & 0x00FF0000;
        this.green  = pixel & 0x0000FF00;
        this.blue   = pixel & 0x000000FF;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }
}
