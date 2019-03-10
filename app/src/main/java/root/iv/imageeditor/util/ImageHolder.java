//package root.iv.imageeditor.util;

//import java.io.Serializable;
package root.iv.imageeditor.util;


import android.graphics.Color;

public class ImageHolder {

    public static int [] brightness_segm(int [] pixels, int width, int height, double alpha) {
        int step = height/100;

        int[][][] pixels1  = new int [step][step][3];
        int[] pxs = new int[step*step];
        for (int i0 = 0; i0 < height; i0 += step-1) {
            for (int j0 = 0; j0 < width; j0 += step-1) {
                int iEnd = (i0+step < height) ? i0+step : height;
                int jEnd = (j0+step < width) ? j0+step : width;

                for (int i = i0; i < iEnd; i++) {
                    for (int j = j0; j < jEnd; j++) {
//                        pxs[j-j0*width + i-i0] = pixels[width*j+i];
                        pixels1[i-i0][j-j0][0] = Color.red(pixels[width * i + j]);
                        pixels1[i-i0][j-j0][1] = Color.green(pixels[width * i + j]);
                        pixels1[i-i0][j-j0][2] = Color.blue(pixels[width * i + j]);
                    }
                }

                MainFunctions.brightness(pixels1, pxs, alpha);

                for(int i1 = 0; i1 + i0 < iEnd; i1++) {
                    for (int j1 = 0; j1 + j0 < jEnd; j1++) {
                        pixels[(i1 + i0)*width + (j1 + j0)] = Color.argb(0xFF, pixels1[i1][j1][0], pixels1[i1][j1][1], pixels1[i1][j1][2]);
                    }
                }
            }

        }
        return pixels;
    }
}