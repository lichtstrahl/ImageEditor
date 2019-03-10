//package root.iv.imageeditor.util;

//import java.io.Serializable;
package root.iv.imageeditor.util;


import android.graphics.Color;

public class ImageHolder {

    public static int [] brightness_segm(int [] pixels, int width, int height, double alpha) {
        int step = height/100;

        int[][][] pixels1  = new int [step][step][3];
        for (int i0 = 0; i0 < width; i0 += step-1) {
            for (int j0 = 0; j0 < height; j0 += step-1) {
                int iEnd = (i0+step < width) ? i0+step : width;
                int jEnd = (j0+step < height) ? j0+step : height;

                for (int i = i0; i < iEnd; i++) {
                    for (int j = j0; j < jEnd; j++) {
                        pixels1[i-i0][j-j0][0] = Color.red(pixels[width * j + i]);
                        pixels1[i-i0][j-j0][1] = Color.green(pixels[width * j + i]);
                        pixels1[i-i0][j-j0][2] = Color.blue(pixels[width * j + i]);
                    }
                }

                MainFunctions.brightness(pixels1, alpha);

                for(int i1 = 0; i1 + i0 < iEnd; i1++) {
                    for (int j1 = 0; j1 + j0 < jEnd; j1++) {
                        pixels[(j1 + j0)*width + (i1 + i0)] = Color.argb(0xFF, pixels1[i1][j1][0], pixels1[i1][j1][1], pixels1[i1][j1][2]);
                    }
                }
            }

        }
        return pixels;
    }
}