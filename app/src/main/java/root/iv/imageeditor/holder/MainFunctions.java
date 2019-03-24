package root.iv.imageeditor.holder;


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

    public static int [][][] noise_red_weak(int [][][] pixels) {
        int height = pixels[1].length;
        int width = pixels.length;

        int inten[][] = new int[width][height];
        double fr_r[][] = new double[width][height];
        double fr_g[][] = new double[width][height];
        double fr_b[][] = new double[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                inten[ii][jj] = pixels[ii][jj][0] + pixels[ii][jj][1] + pixels[ii][jj][2];
            }
        }

        int[][] pattern = MainFunctions.identify_curves(inten, width, height);

        int inten_avg = 0;
        int i = 2;
        int res = 0;
        while (i < (width - 2)) {
            int j = 2;
            while (j < (height - 2)) {
                inten_avg = (inten[i + 1][j + 1] + inten[i - 1][j - 1] + inten[i + 1][j - 1] + inten[i - 1][j + 1] + inten[i][j + 1] + inten[i + 1][j] + inten[i - 1][j] + inten[i][j - 1]) / 12;
                inten_avg = inten_avg + (inten[i + 2][j] + inten[i][j - 2] + inten[i][j + 2] + inten[i - 2][j]) / 12;
                res = MainFunctions.check_curve(i, j, width, height, pattern, 1);
                if (res == 0) {
                    if(inten[i][j] > 0) {
                        pixels[i][j][0] = (int) (1.0 * pixels[i][j][0] * inten_avg) / (inten[i][j]);
                        pixels[i][j][1] = (int) (1.0 * pixels[i][j][1] * inten_avg) / (inten[i][j]);
                        pixels[i][j][2] = (int) (1.0 * pixels[i][j][2] * inten_avg) / (inten[i][j]);
                        if (pixels[i][j][0] > 255) {
                            pixels[i][j][0] = 255;
                        }
                        if (pixels[i][j][1] > 255) {
                            pixels[i][j][1] = 255;
                        }
                        if (pixels[i][j][2] > 255) {
                            pixels[i][j][2] = 255;
                        }
                    }
                    else {
                        pixels[i][j][0] = (int) (0.3 * inten_avg);
                        pixels[i][j][1] = (int) (0.3 * inten_avg);
                        pixels[i][j][2] = (int) (0.3 * inten_avg);
                    }
                } else {
                }
                j = j + 1;
            }
            i = i + 1;
        }
        return pixels;
    }

    public static int [][][] noise_red_strong(int [][][] pixels) {
        int height = pixels[1].length;
        int width = pixels.length;

        int inten[][] = new int[width][height];
        double fr_r[][] = new double[width][height];
        double fr_g[][] = new double[width][height];
        double fr_b[][] = new double[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                inten[ii][jj] = pixels[ii][jj][0] + pixels[ii][jj][1] + pixels[ii][jj][2];
                if (inten[ii][jj] > 0) {
                    fr_r[ii][jj] = 1.0 * pixels[ii][jj][0] / inten[ii][jj];
                    fr_g[ii][jj] = 1.0 * pixels[ii][jj][1] / inten[ii][jj];
                    fr_b[ii][jj] = 1.0 * pixels[ii][jj][2] / inten[ii][jj];
                }
                else {
                    fr_r[ii][jj] = 0.3;
                    fr_g[ii][jj] = 0.3;
                    fr_b[ii][jj] = 0.3;
                }
            }
        }
        // High-freq. jumps removal
        int i = 0;
        int j = 0;
        while(i < width) {
            j = 0;
            while (j < height - 2) {
                if ((inten[i][j + 1] - inten[i][j]) > 0) {
                    if ((inten[i][j + 1] - inten[i][j + 2]) > 0) {
                        inten[i][j + 1] = inten[i][j];
                    }
                }
                if ((fr_r[i][j + 1] - fr_r[i][j]) > 0) {
                    if ((fr_r[i][j + 1] - fr_r[i][j + 2]) > 0) {
                        fr_r[i][j + 1] = fr_r[i][j];
                    }
                }
                if ((fr_g[i][j + 1] - fr_g[i][j]) > 0) {
                    if ((fr_g[i][j + 1] - fr_g[i][j + 2]) > 0) {
                        fr_g[i][j + 1] = fr_g[i][j];
                    }
                }
                if ((fr_b[i][j + 1] - fr_b[i][j]) > 0) {
                    if ((fr_b[i][j + 1] - fr_b[i][j + 2]) > 0) {
                        fr_b[i][j + 1] = fr_b[i][j];
                    }
                }
                j = j + 1;
            }
            i = i + 1;
        }
        i = 0;
        j = 0;
        while(j < height) {
            i = 0;
            while (i < width - 2) {
                if ((inten[i + 1][j] - inten[i][j]) > 0) {
                    if ((inten[i + 1][j] - inten[i + 2][j]) > 0) {
                        inten[i + 1][j] = inten[i][j];
                    }
                }
                if ((fr_r[i + 1][j] - fr_r[i][j]) > 0) {
                    if (fr_r[i + 1][j] - fr_r[i + 2][j] > 0) {
                        fr_r[i + 1][j] = fr_r[i][j];
                    }
                }
                if ((fr_g[i + 1][j] - fr_g[i][j]) > 0) {
                    if (fr_g[i + 1][j] - fr_g[i + 2][j] > 0) {
                        fr_g[i + 1][j] = fr_g[i][j];
                    }
                }
                if ((fr_b[i + 1][j] - fr_b[i][j]) > 0) {
                    if (fr_b[i + 1][j] - fr_b[i + 2][j] > 0) {
                        fr_b[i + 1][j] = fr_b[i][j];
                    }
                }
                i = i + 1;
            }
            j = j + 1;
        }
        int [][] pattern = MainFunctions.identify_curves(inten, width, height);
        // Intensity and polychrome noises removal
        i = 2;
        double fr_r_avg = 0;
        double fr_g_avg = 0;
        double fr_b_avg = 0;
        int inten_avg = 0;
        while(i<width-2) {
            j = 2;
            while (j < height - 2) {
                inten_avg = (int)(1.0*(inten[i + 1][j + 1] + inten[i - 1][j - 1] + inten[i + 1][j - 1] + inten[i - 1][j + 1] + inten[i][j + 1] + inten[i + 1][j] + inten[i - 1][j] + inten[i][j - 1])/12);
                inten_avg = inten_avg + (int)(1.0*(inten[i + 2][j] + inten[i][j - 2] + inten[i][j + 2] + inten[i - 2][j])/12);
                fr_r_avg = (fr_r[i][j] + fr_r[i + 1][j + 1] + fr_r[i - 1][j - 1] + fr_r[i + 1][j - 1] + fr_r[i - 1][j + 1] + fr_r[i][j + 1] + fr_r[i + 1][j] + fr_r[i - 1][j] + fr_r[i][j - 1] + fr_r[i + 2][j] + fr_r[i][j - 2] + fr_r[i][j + 2] + fr_r[i - 2][j])/13;
                fr_g_avg = (fr_g[i][j] + fr_g[i + 1][j + 1] + fr_g[i - 1][j - 1] + fr_g[i + 1][j - 1] + fr_g[i - 1][j + 1] + fr_g[i][j + 1] + fr_g[i + 1][j] + fr_g[i - 1][j] + fr_g[i][j - 1] + fr_g[i + 2][j] + fr_g[i][j - 2] + fr_g[i][j + 2] + fr_g[i - 2][j])/13;
                fr_b_avg = (fr_b[i][j] + fr_b[i + 1][j + 1] + fr_b[i - 1][j - 1] + fr_b[i + 1][j - 1] + fr_b[i - 1][j + 1] + fr_b[i][j + 1] + fr_b[i + 1][j] + fr_b[i - 1][j] + fr_b[i][j - 1] + fr_b[i + 2][j] + fr_b[i][j - 2] + fr_b[i][j + 2] + fr_b[i - 2][j])/13;
                // setting an admissible threshold for noise? if abs(Int(ii, jj) - Int_avg) > Int_avg/100; not needed at all?
                int res = MainFunctions.check_curve(i, j, width, height, pattern, 2);
                if (res == 0) {
                    // here we overwrite data in colors and intensity matrices for the following steps
                    inten[i][j] = inten_avg;
                    fr_r[i][j] = fr_r_avg;
                    pixels[i][j][0] = (int)(inten[i][j]*fr_r_avg);
                    fr_g[i][j] = fr_g_avg;
                    pixels[i][j][1] = (int)(inten[i][j]*fr_g_avg);
                    fr_b[i][j] = fr_b_avg;
                    pixels[i][j][2] = (int)(inten[i][j]*fr_b_avg);
                    if (pixels[i][j][0] > 255) {
                        pixels[i][j][0] = 255;
                    }
                    if (pixels[i][j][1] > 255) {
                        pixels[i][j][1] = 255;
                    }
                    if (pixels[i][j][2] > 255) {
                        pixels[i][j][2] = 255;
                    }
                }
                else {
                    pixels[i][j][0] = (int)(inten[i][j]*fr_r[i][j]);
                    pixels[i][j][1] = (int)(inten[i][j]*fr_g[i][j]);
                    pixels[i][j][2] = (int)(inten[i][j]*fr_b[i][j]);
                    if (pixels[i][j][0] > 255) {
                        pixels[i][j][0] = 255;
                    }
                    if (pixels[i][j][1] > 255) {
                        pixels[i][j][1] = 255;
                    }
                    if (pixels[i][j][2] > 255) {
                        pixels[i][j][2] = 255;
                    }
                }
                j = j + 1;
            }
            i = i + 1;
        }
        return pixels;
    }

    public static int [][]  identify_curves(int [][] inten, int width, int height) {
//        double [][][] F_img = ImageMatrixCalc.dft2(inten);
//
//        int n = 0;
//        int m = width - 1;//(int)(0.02*width);
 //       while(m < width) {
//            n = 0;
//            while(n < height) {
//                F_img[n][m][0] = 0;
//                F_img[n][m][1] = 0;
//                n = n + 1;
//            }
//            m = m + 1;
//        }
//        m = 0;
//        while(m < width) {
//            n = height - 1;//(int)(0.02*height);
//            while(n < height) {
//                F_img[n][m][0] = 0;
//                F_img[n][m][1] = 0;
//                n = n + 1;
//            }
//            m = m + 1;
//        }

//        inten = ImageMatrixCalc.idft2(F_img);

        int [][] pattern = new int [width][height];
        int l = 0;
        int ll = 0;
        int inten_curr = 0;
        while(l<width) {
            ll = 0;
            inten_curr = inten[l][ll];
            while(ll<height) {
                if(Math.abs(inten[l][ll] - inten_curr) > inten_curr/2) {
                    inten_curr = inten[l][ll];
                    pattern[l][ll] = 1;
                }
                else {
                    pattern[l][ll] = 0;
                }
                ll = ll + 1;
            }
            l = l + 1;
        }
        ll = 0;
        while(ll<height) {
            l = 0;
            inten_curr = inten[1][ll];
            while(l<width) {
                if(Math.abs(inten[l][ll] - inten_curr) > inten_curr/2) {
                    inten_curr = inten[l][ll];
                    pattern[l][ll] = 1;
                }
                else {
                    //I_curr = real(Int_new(i, j));
                    //do not overwrite data in pattern() here !
                }
                l = l + 1;
            }
            ll = ll + 1;
        }
        l = 1;
        ll = 1;
        int sum = 0;
        while(l<width-2) {
            ll = 1;
            while(ll<height-2) {
                sum = pattern[l + 1][ll] + pattern[l - 1][ll] + pattern[l][ll + 1] + pattern[l][ll - 1] + pattern[l + 1][ll - 1] + pattern[l + 1][ll + 1] + pattern[l - 1][ll + 1] + pattern[l - 1][ll - 1];
                if(sum == 0) {
                    pattern[l][ll] = 0;
                }
                ll = ll + 1;
            }
            l = l + 1;
        }
        return pattern;
    }

    public static int       check_curve(int i, int j, int width, int height, int [][] pattern, int toler) {
        int sum  = 0;
        int ii = i - toler;
        int jj = 0;
        while(ii<i+toler) {
            jj = j - toler;
            while (jj < j + toler) {
                if(ii >= 0 && ii < width && jj >= 0 && jj < height) {
                    sum = sum + pattern[ii][jj];
                }
                jj = jj + 1;
            }
            ii = ii + 1;
        }
        if(sum > 0) {
            int res = 1;
            return res;
        }
        else {
            int res = 0;
            return res;
        }
    }
}
