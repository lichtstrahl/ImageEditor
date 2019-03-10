//package root.iv.imageeditor.util;

//import java.io.Serializable;
package root.iv.imageeditor.util;


import android.graphics.Color;

public class ImageHolder {

    public static int [] brightness_segm(int [] pixels, int width, int height, double alpha) {
        int step = height/100;
        int i0 = 0;
        int j0 = 0;
        int lim_width = 0;
        int lim_height = 0;
        int i = 0;
        int j = 0;

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
                      int [] rgb = ImageMatrixCalc.front_conversion(pixels[width * j + i]);
                      pixels1[i-i0][j-j0][0] = rgb[0];
                      pixels1[i-i0][j-j0][1] = rgb[1];
                      pixels1[i-i0][j-j0][2] = rgb[2];
                      j = j + 1;
                  }
                  i = i + 1;
              }
              pixels1  = MainFunctions.brightness(pixels1, alpha);

              for(int i1 = 0; i1 + i0 < lim_width; i1++) {
                  for (int j1 = 0; j1 + j0 < lim_height; j1++) {
                      pixels[(j1 + j0)*width + (i1 + i0)] = Color.argb(0xFF, pixels1[i1][j1][0], pixels1[i1][j1][1], pixels1[i1][j1][2]);
                  }
              }

              j0 = j0 + step - 1;
          }
          j0 = 0;
          i0 = i0 + step - 1;
        }
        return pixels;
    }
}