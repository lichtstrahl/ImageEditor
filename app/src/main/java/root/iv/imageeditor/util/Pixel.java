package root.iv.imageeditor.util;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Pixel {
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public Pixel(int pixel) {
        this.alpha  = Color.alpha(pixel);
        this.red    = (pixel & 0x00FF0000) >> 16;
        this.green  = (pixel & 0x0000FF00) >> 8;
        this.blue   = (pixel & 0x000000FF);

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
