package root.iv.imageeditor.util;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Locale;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import root.iv.imageeditor.app.App;

public class ReactiveImageHolder implements Serializable {
    private int[] pixels;   //
    private int width;      //
    private int height;     //

    private ReactiveImageHolder(int[] pxs, int w, int h) {
        pixels = pxs;
        width = w;
        height = h;
    }

    public static ReactiveImageHolder getInstance(@Nullable Bitmap scaled) {
        if (scaled == null) throw new NullPointerException("Передан null как bitmap");
        int w = scaled.getWidth();
        int h = scaled.getHeight();

        App.logI(String.format(Locale.ENGLISH, "w,h: %d %d", w, h));
        int[] pxs = new int[h*w];
        scaled.getPixels(pxs,0, w, 0,0,w, h);


        return new ReactiveImageHolder(pxs, w, h);
    }

    public Single<Bitmap> brightness(double alpha) {
        return Single.fromCallable(() -> {
                                    ImageHolder.brightness_segm(pixels, width, height, alpha);
                                    return getCurrentBitmap();
                                });
    }

    public Single<Bitmap> contrast(double alpha) {
        return Single.fromCallable(() -> {
           ImageHolder.contrast_segm(pixels, width, height, alpha);
           return getCurrentBitmap();
        });
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap getCurrentBitmap() {
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

}
