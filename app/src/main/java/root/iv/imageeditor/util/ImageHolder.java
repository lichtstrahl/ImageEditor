package root.iv.imageeditor.util;

import java.io.Serializable;
import java.util.concurrent.ThreadFactory;

import root.iv.imageeditor.app.App;

public class ImageHolder implements Serializable {
    public int [] pixels;

    private ImageHolder(int [] pixels) {
        this.pixels = pixels;
    }

    public static ImageHolder getInstance(int [] pixels) {
        return new ImageHolder(pixels);
    }

    public int [] getPixels() {
        return pixels;
    }

    public ImageHolder brightness_segm(int width, int height, double alpha) {
        App.logI("Thread: " + Thread.currentThread().getName());
        int step = height+1;

        int[][][] pixels1  = new int [step][step][3];
        for (int i0 = 0; i0 < width; i0 += step - 1) {
          for (int j0 = 0; j0 < height; j0 += step - 1) {

              int lim_width = (i0 + step < width) ? i0+step : width;
              int lim_height = (j0 + step < height) ? j0 + step : height;

              for (int i = i0; i < lim_width; i++) {
                  for (int j = j0; j < lim_height; j++) {
                      int [] rgb = ImageMatrixCalc.front_conversion(pixels[width * j + i]);
                      pixels1[i-i0][j-j0][0] = rgb[0];
                      pixels1[i-i0][j-j0][1] = rgb[1];
                      pixels1[i-i0][j-j0][2] = rgb[2];
                  }
              }
              pixels1  = MainFunctions.brightness(pixels1, alpha);


              for (int i1 = 0; i1 + i0 < lim_width; i1++) {
                  for (int j1 = 0; j1 + j0 < lim_height; j1++) {
                      int rgb = ImageMatrixCalc.back_conversion(pixels1[i1][j1][0], pixels1[i1][j1][1], pixels1[i1][j1][2]);
                      pixels[(j1 + j0)*width + (i1 + i0)] = rgb;
                  }
              }
          }
        }
        return this;
    }

    public ImageHolder contrast_segm(int [] pixels, int width, int height, double alpha) {
        int step = height/100;
        int i0 = 0;
        int j0 = 0;
        int lim_width = 0;
        int lim_height = 0;
        int i = 0;
        int j = 0;
        int i1 = 0;
        int j1 = 0;
        int k = 0;
        int kk = 0;
        int [][] inten = new int [width][height];

        while (k < width){
            while (kk < height) {
                int [] rgb = ImageMatrixCalc.front_conversion(pixels[width * k + kk]);
                inten[k][kk] = rgb[0] + rgb[1] + rgb[2];
                kk = kk + 1;
            }
            k = k + 1;
            kk = 0;
        }

        int imax = ImageMatrixCalc.find_max(inten);
        int imin = ImageMatrixCalc.find_min(inten);
        int range = imax - imin;

        int pixels1 [][][] = new int [step][step][3];
        while (i0 < width) {
            while (j0 < height) {
                if (i0 + step < width) {
                    lim_width = i0 + step;
                }
                else {
                    lim_width = width;
                }
                if (j0 + step < height) {
                    lim_height = j0 + step;
                }
                else {
                    lim_height = height;
                }
                i = i0;
                while (i < lim_width) {
                    j = j0;
                    while (j < lim_height) {
                        int [] rgb = ImageMatrixCalc.front_conversion(pixels[width * i + j]);
                        pixels1[i-i0][j-j0][0] = rgb[0];
                        pixels1[i-i0][j-j0][1] = rgb[1];
                        pixels1[i-i0][j-j0][2] = rgb[2];
                        j = j + 1;
                    }
                    i = i + 1;
                }
                pixels1  = MainFunctions.contrast(pixels1, imax, imin, range, alpha);
                i1 = 0;
                while (i1 + i0 < lim_width) {
                    j1 = 0;
                    while (j1 + j0 < lim_height) {
                        int rgb = ImageMatrixCalc.back_conversion(pixels1[i1][j1][0], pixels1[i1][j1][1], pixels1[i1][j1][2]);
                        pixels[(i1 + i0)*width + (j1 + j0)] = rgb;
                        j1 = j1 + 1;
                    }
                    i1 = i1 + 1;
                }
                j0 = j0 + step - 1;
            }
            j0 = 0;
            i0 = i0 + step - 1;
        }
        return this;
    }

    // new_width is a resized image width, approx. 500 pix.
    public ImageHolder resize(int [] pixels, int width, int height, int new_width) {
        int new_height = new_width*height/width;

        int[] i_num = new int [new_width];
        for (int m = 0; m < new_width ; m++) {
            i_num[m] = m;
        }
        int [] seq_width = ImageMatrixCalc.interp(0, new_width - 1, 0, width - 1, i_num);

        int[] j_num = new int [new_height];
        for (int n = 0; n < new_height ; n++) {
            j_num[n] = n;
        }
        int [] seq_height = ImageMatrixCalc.interp(0, new_height - 1, 0, height - 1, j_num);

        int pixels1 [] = new int [new_width*new_width + new_height];

        int i = 0;
        int j = 0;
        while (i < new_width) {
            while (j < new_height) {
                pixels1[i*new_width + j] = pixels[width * seq_width[i] + seq_height[j]];
                j = j + 1;
            }
            i = i + 1;
            j = 0;
        }
        pixels = null;
        pixels = pixels1;
        return this;
    }

    public ImageHolder wbalance_segm(int [] pixels, int width, int height, double alpha) {
        int r_comp = 0;
        int g_comp = 0;
        int b_comp = 0;
        if (alpha <= 0) {
            int b = 255;
            int g = (int) (255 - 29 * (-alpha));
            int r = (int) (255 - 54 * (-alpha));
            b_comp = 255 - b;
            g_comp = 255 - g;
            r_comp = 255 - r;
        }
        else {
            int b = (int) (251 - 54 * alpha);
            //    G = 255 - 29*alpha;
            int g = (int) (255 - 39 * alpha);
            int r = 255;
            b_comp = 251 - b;
            g_comp = 255 - g;
            r_comp = 255 - r;
        }
        int step = height/100;
        int i0 = 0;
        int j0 = 0;
        int lim_width = 0;
        int lim_height = 0;
        int i = 0;
        int j = 0;
        int i1 = 0;
        int j1 = 0;
        int pixels1 [][][] = new int [step][step][3];
        while (i0 < width) {
            while (j0 < height) {
                if (i0 + step < width) {
                    lim_width = i0 + step;
                }
                else {
                    lim_width = width;
                }
                if (j0 + step < height) {
                    lim_height = j0 + step;
                }
                else {
                    lim_height = height;
                }
                i = i0;
                while (i < lim_width) {
                    j = j0;
                    while (j < lim_height) {
                        int [] rgb = ImageMatrixCalc.front_conversion(pixels[width * i + j]);
                        pixels1[i-i0][j-j0][0] = rgb[0];
                        pixels1[i-i0][j-j0][1] = rgb[1];
                        pixels1[i-i0][j-j0][2] = rgb[2];
                        j = j + 1;
                    }
                    i = i + 1;
                }
                pixels1  = MainFunctions.wbalance(pixels1, r_comp, g_comp, b_comp, alpha);
                i1 = 0;
                while (i1 + i0 < lim_width) {
                    j1 = 0;
                    while (j1 + j0 < lim_height) {
                        int rgb = ImageMatrixCalc.back_conversion(pixels1[i1][j1][0], pixels1[i1][j1][1], pixels1[i1][j1][2]);
                        pixels[(i1 + i0)*width + (j1 + j0)] = rgb;
                        j1 = j1 + 1;
                    }
                    i1 = i1 + 1;
                }
                j0 = j0 + step - 1;
            }
            j0 = 0;
            i0 = i0 + step - 1;
        }
        return this;
    }
}