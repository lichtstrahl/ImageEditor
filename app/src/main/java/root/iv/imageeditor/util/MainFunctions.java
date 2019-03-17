package root.iv.imageeditor.util;

import android.graphics.Color;



public class MainFunctions {
    public static int sumRGB(int color) {
        return Color.red(color) + Color.green(color) + Color.blue(color);
    }

    public static void brightness(int[] pxs, int width, int height, double alpha) {
        int imax = sumRGB(pxs[0]);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int sum = sumRGB(pxs[i*width+j]);
                if (imax < sum) imax = sum;
            }
        }

        double imax1 = alpha * imax;
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                int inten = sumRGB(pxs[i*width+j]);
                int inten_new = ImageMatrixCalc.interp(0, imax, 0, imax1, inten);

                double frR = inten != 0 ? (1.0 * Color.red(pxs[i*width + j]))     / inten : 1;
                double frG = inten != 0 ? (1.0 * Color.green(pxs[i*width + j]))   / inten : 1;
                double frB = inten != 0 ? (1.0 * Color.blue(pxs[i*width + j]))    / inten : 1;

                pxs[i*width + j] = Color.argb(0xFF, (int) (frR * inten_new), (int) (frG * inten_new), (int) (frB * inten_new) );
            }
        }
    }
}
