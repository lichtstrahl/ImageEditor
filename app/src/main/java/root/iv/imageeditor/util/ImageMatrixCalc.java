package root.iv.imageeditor.util;

public class ImageMatrixCalc {
    public static double [] interp(double x1, double x2, double y1, double y2, double [] x_arr) {
         double y1d = y1;
         double y2d = y2;
         double k = (y2d - y1d)/(x2 - x1);
         double c = y2d - k*x2;
         double [] y_arr = new double [x_arr.length];
         for (int i = 0; i < x_arr.length; i++) {
             y_arr[i] = (c + k*x_arr[i]);
         }
         return y_arr;
    }
    public static int [] interp(double x1, double x2, double y1, double y2, int [] x_arr) {
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
    public static double interp(double x1, double x2, double y1, double y2, double x_arr) {
        double y1d = y1;
        double y2d = y2;
        double k = (y2d - y1d)/(x2 - x1);
        double c = y2d - k*x2;
        double y_arr = (c + k*x_arr);
        return y_arr;
    }
    public static double find_max(double [][] arr) {
        double max = 0;
        int height = arr[0].length;
        int width = arr.length;
        for (int n = 0; n < height - 1; n++) {
            for (int m = 0; m < width - 1; m++) {
                if (arr[m][n] > max) {
                    max = arr[m][n];
                }
                else {
                }
            }
        }
        return max;
    }
    public static double find_min(double [][] arr) {
        double min = 0;
        int height = arr[0].length;
        int width = arr.length;
        for (int n = 0; n < height - 1; n++) {
            for (int m = 0; m < width - 1; m++) {
                if (arr[m][n] < min) {
                    min = arr[m][n];
                }
                else {
                }
            }
        }
        return min;
    }
}
