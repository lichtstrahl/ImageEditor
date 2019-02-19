package root.iv.imageeditor.util;

import java.io.Serializable;

public class ImageHolder implements Serializable {
    private double[][][] pixels;

    private ImageHolder(double[][][] pixels) {
        this.pixels = pixels;
    }

    public static ImageHolder getInstance(double[][][] pixels) {
        return new ImageHolder(pixels);
    }

    public double[][][] getPixels() {
        return pixels;
    }

    // alpha = {1;2}
    public ImageHolder brightness(double alpha) {

        int height = pixels[1].length;
        int width = pixels.length;

        // brightness/contrast block
        double inten[][] = new double[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                inten[ii][jj] = pixels[ii][jj][0] + pixels[ii][jj][1] + pixels[ii][jj][2];
            }
        }

        double imax = ImageMatrixCalc.find_max(inten);
        double imin = ImageMatrixCalc.find_min(inten);


        double fr_r[][] = new double[width][height];
        double fr_g[][] = new double[width][height];
        double fr_b[][] = new double[width][height];

        for (int kk = 0; kk < width - 1; kk++) {
            for (int kkk = 0; kkk < height - 1; kkk++) {
                double interp = ImageMatrixCalc.interp(imin, imax, imin, alpha * imax, inten[kk][kkk]);

                if (inten[kk][kkk] > 0) {
                    fr_r[kk][kkk] = (pixels[kk][kkk][0]) / (inten[kk][kkk]);
                    fr_g[kk][kkk] = pixels[kk][kkk][1] / inten[kk][kkk];
                    fr_b[kk][kkk] = pixels[kk][kkk][2] / inten[kk][kkk];
                } else {
                    fr_r[kk][kkk] = 1;
                    fr_g[kk][kkk] = 1;
                    fr_b[kk][kkk] = 1;
                }

                pixels[kk][kkk][0] = (int) Math.round(fr_r[kk][kkk] * interp);
                if (pixels[kk][kkk][0] > 255) {
                    pixels[kk][kkk][0] = 255;
                }
                pixels[kk][kkk][1] = (int) Math.round(fr_g[kk][kkk] * interp);
                if (pixels[kk][kkk][1] > 255) {
                    pixels[kk][kkk][1] = 255;
                }
                pixels[kk][kkk][2] = (int) Math.round(fr_b[kk][kkk] * interp);
                if (pixels[kk][kkk][2] > 255) {
                    pixels[kk][kkk][2] = 255;
                }
            }
        }

        return this;
    }

    // alpha = {0;1}
    public ImageHolder contrast(double alpha) {
        int height = pixels[1].length;
        int width = pixels.length;

        // brightness/contrast block
        double inten[][] = new double[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                inten[ii][jj] = pixels[ii][jj][0] + pixels[ii][jj][1] + pixels[ii][jj][2];
            }
        }

        double imax = ImageMatrixCalc.find_max(inten);
        double imin = ImageMatrixCalc.find_min(inten);
        double range = imax - imin;


        double [][]fr_r = new double[width][height];
        double [][]fr_g = new double[width][height];
        double [][]fr_b = new double[width][height];
        double [][]inten_new = new double[width][height];

        for (int k = 0; k < width - 1; k++) {
            for (int a = 0; a < height - 1; a++) {
                double int_k_a = inten[k][a];
                inten_new[k][a] = ImageMatrixCalc.interp(imin, imax, imin + range*alpha/2, imax - range*alpha/2, int_k_a);
            }
        }

        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                if (inten[i][j] > 0) {
                    fr_r[i][j] = (pixels[i][j][0]) / (inten[i][j]);
                    fr_g[i][j] = pixels[i][j][1] / inten[i][j];
                    fr_b[i][j] = pixels[i][j][2] / inten[i][j];
                } else {
                    fr_r[i][j] = 1;
                    fr_g[i][j] = 1;
                    fr_b[i][j] = 1;
                }

                pixels[i][j][0] = (int) Math.round(fr_r[i][j] * inten_new[i][j]);
                if (pixels[i][j][0] > 255) {
                    pixels[i][j][0] = 255;
                }
                if (pixels[i][j][0] < 0) {
                    pixels[i][j][0] = 0;
                }
                pixels[i][j][1] = (int) Math.round(fr_g[i][j] * inten_new[i][j]);
                if (pixels[i][j][1] > 255) {
                    pixels[i][j][1] = 255;
                }
                if (pixels[i][j][1] < 0) {
                    pixels[i][j][1] = 0;
                }
                pixels[i][j][2] = (int) Math.round(fr_b[i][j] * inten_new[i][j]);
                if (pixels[i][j][2] > 255) {
                    pixels[i][j][2] = 255;
                }
                if (pixels[i][j][2] < 0) {
                    pixels[i][j][2] = 0;
                }
            }
        }

        return this;
    }

    // i is a resized image width, approx. 500 pix.
    public ImageHolder resize(int i) {
        // resize block
        int height = pixels[1].length;
        int width = pixels.length;
        double [][][] pixels_copy = pixels;
        pixels = null;
        int j = i*height/width;
        int[] i_num = new int [i];
        int[] j_num = new int [j];

        for (int ii = 0; ii < i ; ii++) {
            i_num[ii] = ii;
        }

        for (int jj = 0; jj < j ; jj++) {
            j_num[jj] = jj;
        }

        int [] seq_width = ImageMatrixCalc.interp(0, i - 1, 0, width - 1, i_num);
        int [] seq_height = ImageMatrixCalc.interp(0, j - 1, 0, height - 1, j_num);


        for (int m = 0; m < i ; m++) {
            for (int n = 0; n < j ; n++) {
                pixels[m][n][0] = (int) pixels_copy[seq_width[m]][seq_height[n]][0];
                pixels[m][n][1] = (int) pixels_copy[seq_width[m]][seq_height[n]][1];
                pixels[m][n][2] = (int) pixels_copy[seq_width[m]][seq_height[n]][2];
            }
        }
        return this;
    }
}


