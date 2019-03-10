package root.iv.imageeditor.util;

public class MainFunctions {
    public static int [][][] brightness(int [][][] pixels, double alpha) {

        int height = pixels[1].length;
        int width = pixels.length;

        // brightness/contrast block
        int inten[][] = new int [width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                inten[ii][jj] = pixels[ii][jj][0] + pixels[ii][jj][1] + pixels[ii][jj][2];
            }
        }

        int imax = ImageMatrixCalc.find_max(inten);
        int imin = ImageMatrixCalc.find_min(inten);

        double fr_r = 0;
        double fr_g = 0;
        double fr_b = 0;

        double imax1 = alpha * imax;
        for (int k = 0; k < width - 1; k++) {
            for (int a = 0; a < height - 1; a++) {
                int inten_new = ImageMatrixCalc.interp(imin, imax, imin, imax1, inten[k][a]);
                try {
                    fr_r = (1.0 * pixels[k][a][0]) / inten[k][a];
                    fr_g = (1.0 * pixels[k][a][1]) / inten[k][a];
                    fr_b = (1.0 * pixels[k][a][2]) / inten[k][a];
                } catch (Exception e) {
                    fr_r = 1;
                    fr_g = 1;
                    fr_b = 1;
                }
                pixels[k][a][0] = (int) (fr_r * inten_new);
                pixels[k][a][1] = (int) (fr_g * inten_new);
                pixels[k][a][2] = (int) (fr_b * inten_new);
                if (pixels[k][a][0] > 255) {
                    pixels[k][a][0] = 255;
                }
                if (pixels[k][a][1] > 255) {
                    pixels[k][a][1] = 255;
                }
                if (pixels[k][a][2] > 255) {
                    pixels[k][a][2] = 255;
                }
            }
        }
        return pixels;
    }

    // alpha = {-0.5;0.5}
    public static int [][][] contrast(int [][][] pixels, int imax, int imin, int range, double alpha) {
        int height = pixels[1].length;
        int width = pixels.length;

        // brightness/contrast block
        int inten[][] = new int [width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                inten[ii][jj] = pixels[ii][jj][0] + pixels[ii][jj][1] + pixels[ii][jj][2];
            }
        }

        int inten_new = 0;
        double fr_r = 0;
        double fr_g = 0;
        double fr_b = 0;

        double imin1 = imin - range * alpha / 2;
        double imax1 = imax + range * alpha / 2;
        for (int k = 0; k < width - 1; k++) {
            for (int a = 0; a < height - 1; a++) {
                inten_new = ImageMatrixCalc.interp(imin, imax, imin1, imax1, inten[k][a]);
                try {
                    fr_r = (1.0 * pixels[k][a][0]) / inten[k][a];
                    fr_g = (1.0 * pixels[k][a][1]) / inten[k][a];
                    fr_b = (1.0 * pixels[k][a][2]) / inten[k][a];
                } catch (Exception e) {
                    fr_r = 1;
                    fr_g = 1;
                    fr_b = 1;
                }
                pixels[k][a][0] = (int) (fr_r * inten_new);
                pixels[k][a][1] = (int) (fr_g * inten_new);
                pixels[k][a][2] = (int) (fr_b * inten_new);
                if (pixels[k][a][0] > 255) {
                    pixels[k][a][0] = 255;
                }
                else {
                    if (pixels[k][a][0] < 0) {
                        pixels[k][a][0] = 0;
                    }
                }
                if (pixels[k][a][1] > 255) {
                    pixels[k][a][1] = 255;
                }
                else {
                    if (pixels[k][a][1] < 0) {
                        pixels[k][a][1] = 0;
                    }
                }
                if (pixels[k][a][2] > 255) {
                    pixels[k][a][2] = 255;
                }
                else {
                    if (pixels[k][a][2] < 0) {
                        pixels[k][a][2] = 0;
                    }
                }
            }
        }
        return pixels;
    }

    // white balance: cloudy <--> fluorescent <--> sun <--> incandescent; alpha = {-1; +2}
    public static int [][][] wbalance(int [][][] pixels, int r_comp, int g_comp, int b_comp, double alpha) {
        int height = pixels[1].length;
        int width = pixels.length;
        int pixels_new [][][] = pixels;
        for (int i = 0; i < width; i++) {
            for (int ii = 0; ii < height; ii++) {
                pixels_new[i][ii][0] = pixels[i][ii][0] + r_comp;
                pixels_new[i][ii][1] = pixels[i][ii][1] + g_comp;
                pixels_new[i][ii][2] = pixels[i][ii][2] + b_comp;
                double k = (pixels[i][ii][0] + pixels[i][ii][1] + pixels[i][ii][2]) / (pixels_new[i][ii][0] + pixels_new[i][ii][1] + pixels_new[i][ii][2]);
                pixels_new[i][ii][0] = (int) k * pixels_new[i][ii][0];
                pixels_new[i][ii][1] = (int) k * pixels_new[i][ii][1];
                pixels_new[i][ii][2] = (int) k * pixels_new[i][ii][2];
                if (pixels_new[i][ii][0] > 255) {
                    pixels_new[i][ii][0] = 255;
                }
                if (pixels_new[i][ii][1] > 255) {
                    pixels_new[i][ii][1] = 255;
                }
                if (pixels_new[i][ii][2]> 255) {
                    pixels_new[i][ii][2] = 255;
                }
            }
        }
        return pixels_new;
    }
}
