package root.iv.imageeditor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.PowerManager;
import android.text.format.DateUtils;

import java.io.Serializable;
import java.util.Locale;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.iv.imageeditor.app.App;

public class ReactiveImageHolder implements Serializable {
    private static final String WAKELOCK_TAG = "lock:image-holder";
    private int[] pixels;
    private int width;
    private int height;
    private PowerManager.WakeLock lock;

    private ReactiveImageHolder(Context context, int[] pxs, int w, int h) {
        pixels = pxs;
        width = w;
        height = h;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        lock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
    }

    public static ReactiveImageHolder getInstance(Context context, @Nullable Bitmap scaled) {
        if (scaled == null) throw new NullPointerException("Передан null как bitmap");
        int w = scaled.getWidth();
        int h = scaled.getHeight();

        App.logI(String.format(Locale.ENGLISH, "w,h: %d %d", w, h));
        int[] pxs = new int[h*w];
        scaled.getPixels(pxs,0, w, 0,0,w, h);


        return new ReactiveImageHolder(context, pxs, w, h);
    }

    public Single<Bitmap> brightness(double alpha) {

        Single<Bitmap> work = Single.fromCallable(() -> {
                                    lock.acquire(10* DateUtils.SECOND_IN_MILLIS);
                                    ImageHolder.brightness_segm(pixels, width, height, alpha);
                                    return getCurrentBitmap();
                                });
        return work
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Bitmap getCurrentBitmap() {

        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

}
