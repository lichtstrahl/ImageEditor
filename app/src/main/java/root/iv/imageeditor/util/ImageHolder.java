package root.iv.imageeditor.util;

import root.iv.imageeditor.image.Image;

public class ImageHolder {
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

    public ImageHolder reseize(int dx, int dy) {
        // Какая-то работа
        pixels[0][0][0] = dx + dy;

        return this;
    }

}

class Example {

    public static void main(String[] arg) {
        double[][][] result;
        double[][][] bitmap= new double[1][1][1];

        // Получаю обработчик (допустим массив у меня откуда-то будет, сейчас 1 элемент)
        ImageHolder holder =ImageHolder.getInstance(bitmap);

        // Смотрю текущее состояние (автоматом там 0)
        result = holder.getPixels();
        System.out.println(result[0][0][0]);

        // Изменил размер
        holder.reseize(1,1);

        // Хочу узнать результат
        result = holder.getPixels();
        // Поскольку в результате reseize я просто заношу в первый пиксель сумму, то там будет 1+1=2
        System.out.println(result[0][0][0]);
    }
}
