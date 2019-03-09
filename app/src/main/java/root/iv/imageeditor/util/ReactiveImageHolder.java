package root.iv.imageeditor.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Locale;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import root.iv.imageeditor.app.App;

public class ReactiveImageHolder implements Serializable {
    private ImageHolder holder;
    private int width;
    private int height;

    private ReactiveImageHolder(int[] pxs, int w, int h) {

        holder = ImageHolder.getInstance(pxs);
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
        App.logI("Thread: " + Thread.currentThread().getName());
        holder.brightness_segm(width, height, alpha);
        return getCurrentBitmap();
    }

    public Single<Bitmap> getCurrentBitmap() {
        int[] pixls = holder.getPixels();
        return Single.just(Bitmap.createBitmap(pixls, width, height, Bitmap.Config.ARGB_8888));
    }
}
