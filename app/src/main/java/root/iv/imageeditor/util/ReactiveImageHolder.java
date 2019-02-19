package root.iv.imageeditor.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class ReactiveImageHolder implements Serializable {
    private static final int POS_RED    = 0;
    private static final int POS_GREEN  = 1;
    private static final int POS_BLUE   = 2;
    private static final int POS_ALPHA  = 3;

    private ImageHolder holder;
    private int width;
    private int height;

    private ReactiveImageHolder(double[][][] pxs, int w, int h) {
        holder = ImageHolder.getInstance(pxs);
        width = w;
        height = h;
    }

    public static ReactiveImageHolder getInstance(@Nullable Bitmap bitmap) {
        if (bitmap == null) throw new NullPointerException("Передан null как bitmap");
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        double[][][] pxs = new double[h][w][4];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int color = bitmap.getPixel(x,y);
                pxs[y][x][POS_RED] = Color.red(color);
                pxs[y][x][POS_GREEN] = Color.green(color);
                pxs[y][x][POS_BLUE] = Color.blue(color);
                pxs[y][x][POS_ALPHA] = Color.alpha(color);
            }
        }

        return new ReactiveImageHolder(pxs, w, h);
    }

    public Bitmap brightness(double alpha) {
        holder.brightness(alpha);
        return getCurrentBitmap();
    }

    public Bitmap getCurrentBitmap() {
        double[][][] pxs = holder.getPixels();

        int[] colors = new int[width*height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colors[y*width + x] = Color.argb((int)pxs[y][x][POS_ALPHA], (int)pxs[y][x][POS_RED], (int)pxs[y][x][POS_GREEN], (int)pxs[y][x][POS_BLUE]);
            }
        }

        return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
    }
}
