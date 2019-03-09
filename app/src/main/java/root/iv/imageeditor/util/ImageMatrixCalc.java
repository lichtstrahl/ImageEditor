package root.iv.imageeditor.util;

public class ImageMatrixCalc {
    public static int [] front_conversion(int rgb) {
        int [] col = new int [3];
        col[2] = (int)(rgb & 255);
        col[1] = (int)(rgb >> 8 & 255);
        col[0] = (int)(rgb >> 16 & 255);
        return col;
    }
    public static int back_conversion(int r, int g, int b) {
        int rgb = ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
        return rgb;
    }
    public static int [] interp(int x1, int x2, int y1, int y2, double [] x_arr) {
        double y1d = y1;
        double y2d = y2;
        double k = (y2d - y1d)/(x2 - x1);
        double c = y2d - k*x2;
        int [] y_arr = new int [x_arr.length];
        for (int i = 0; i < x_arr.length; i++) {
            y_arr[i] = (int)(c + k*x_arr[i]);
        }
        return y_arr;
    }
    public static int [] interp(int x1, int x2, double y1, double y2, int [] x_arr) {
        double k = (y2 - y1)/(x2 - x1);
        double c = y2 - k*x2;
        int [] y_arr = new int [x_arr.length];
        for (int i = 0; i < x_arr.length; i++) {
            y_arr[i] = (int)(c + k*x_arr[i]);
        }
        return y_arr;
    }
    public static int interp(int x1, int x2, double y1, double y2, int x) {
        double k = (y2 - y1)/(x2 - x1);
        double c = y2 - k*x2;
        int y_arr = (int)(c + k*x);
        return y_arr;
    }
    public static int find_max(int [][] arr) {
        int max = 0;
        int height = arr[0].length;
        int width = arr.length;
        for (int m = 0; m < width - 1; m = m + 20) {
            for (int n = 0; n < height - 1; n = n + 20) {
                if (arr[m][n] > max) {
                    max = arr[m][n];
                }
            }
        }
        return max;
    }
    public static double [] find_max(double [][] arr , int return_coord) {
        double [] max = new double [3];
        max[0] = 0;
        max[1] = 0;
        max[2] = 0;
        int height = arr[0].length;
        int width = arr.length;
        for (int n = 0; n < height - 1; n++) {
            for (int m = 0; m < width - 1; m++) {
                if (arr[m][n] > max[0]) {
                    max[0] = arr[m][n];
                    max[1] = m;
                    max[2] = n;
                }
                else {
                }
            }
        }
        return max;
    }
    public static int find_min(int [][] arr) {
        int min = 0;
        int height = arr[0].length;
        int width = arr.length;
        for (int m = 0; m < width - 1; m = m + 20) {
            for (int n = 0; n < height - 1; n = n + 20) {
                if (arr[m][n] < min) {
                    min = arr[m][n];
                }
            }
        }
        return min;
    }
}


