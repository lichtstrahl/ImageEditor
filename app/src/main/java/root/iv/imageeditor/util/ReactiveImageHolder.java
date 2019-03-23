package root.iv.imageeditor.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.util.Locale;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import rapid.decoder.BitmapDecoder;
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

    /**
     * @deprecated - из-за возможных ошибок OutOfMemory
     * @param img - входная картинка
     * @return - экземпляр загрузчика
     */
    @Deprecated
    public static ReactiveImageHolder getInstance(@Nullable Bitmap img) {
        if (img == null) throw new NullPointerException("Передан null как bitmap");
        int w = img.getWidth();
        int h = img.getHeight();

        App.logI(String.format(Locale.ENGLISH, "w,h: %d %d", w, h));
        int[] pxs = new int[h*w];
        img.getPixels(pxs,0, w, 0,0,w, h);
        return new ReactiveImageHolder(pxs, w, h);
    }

    /**
     * Сначала просто узнаем размеры изображения, без выделения памяти.
     * Подсчитываем коэффициент для нужного нам изображения
     * @param path - путь к изображению
     * @param reqW - запрашиваемая ширина
     * @param reqH - запрашиваемая высота
     * @return - экземпляр загрузчика
     */
    public static ReactiveImageHolder getInstance(String path, int reqW, int reqH) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqW, reqH);
        options.inJustDecodeBounds = false;
        Bitmap mini = BitmapFactory.decodeFile(path, options);

        int w = mini.getWidth();
        int h = mini.getHeight();
        int[] pxs = new int[w*h];
        mini.getPixels(pxs, 0, w, 0, 0, w, h);
        return new ReactiveImageHolder(pxs, w, h);
    }

    public Single<Bitmap> brightness(double alpha) {
        return Single.fromCallable(() -> {
                                    ImageHolder.brightness_segm(pixels, width, height, alpha);
                                    return getCurrentBitmap();
                                });
    }

    // Подсчет необходимого коэффициента масштабирования
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqW, int reqH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqH || width > reqW) {
            inSampleSize *= 2;
            final int halfHeight = height / inSampleSize;
            final  int halfWidth = width / inSampleSize;

            while ((halfHeight/inSampleSize) >= reqH && (halfWidth/inSampleSize) >= reqW) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
