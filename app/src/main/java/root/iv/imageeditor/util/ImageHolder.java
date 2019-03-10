//package root.iv.imageeditor.util;

//import java.io.Serializable;
package root.iv.imageeditor.util;


import android.graphics.Color;

public class ImageHolder {

    public static int [] brightness_segm(int [] pixels, int width, int height, double alpha) {
        int step = height/100;

        int[] pxs = new int[step*step];
        for (int i0 = 0; i0 < height; i0 += step-1) {
            for (int j0 = 0; j0 < width; j0 += step-1) {
                int iEnd = (i0+step < height) ? i0+step : height;
                int jEnd = (j0+step < width) ? j0+step : width;

                for (int i = i0; i < iEnd; i++) {
                    for (int j = j0; j < jEnd; j++) {
                        pxs[(i-i0)*step + j-j0] = pixels[i*width + j];
                    }
                }

                MainFunctions.brightness(pxs, step, step, alpha);

                for(int i1 = 0; i1 + i0 < iEnd; i1++) {
                    for (int j1 = 0; j1 + j0 < jEnd; j1++) {
                        pixels[(i1 + i0)*width + (j1 + j0)] = Color.argb(
                                0xFF,
                                Color.red(pxs[i1*step + j1]),
                                Color.green(pxs[i1*step + j1]),
                                Color.blue(pxs[i1*step + j1])
                        );
                    }
                }

            }

        }
        return pixels;
    }
}