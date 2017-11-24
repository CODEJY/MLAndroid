package org.tensorflow.demo.android;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by wujy on 2017/11/23.
 */

public class BitmapHandle {

    //将预处理过的图片传入，Bitmap to array
    public float[] getPixelData(Bitmap newbitmap) {
        if (newbitmap == null) {
            return null;
        }

        int width = newbitmap.getWidth();
        int height = newbitmap.getHeight();

        // Get 28x28 pixel data from bitmap
        int[] pixels = new int[width * height];
        newbitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        float[] retPixels = new float[pixels.length];
        for (int i = 0; i < pixels.length; ++i) {
            // Set 0 for white and 255 for black pixel
            int pix = pixels[i];
            int b = pix & 0xff;
            retPixels[i] = (float) ((0xff - b)/255.);

        }
        return retPixels;
    }
    //图片预处理，先灰度处理，再reshape
    public Bitmap preProcess(Bitmap bitmap) {
        Bitmap greyBitm = convertGreyImg(bitmap);
        Bitmap reshapeBitm = reshape(greyBitm);
        greyBitm.recycle();
        return reshapeBitm;
    }
    private Bitmap reshape(Bitmap newbitmap) {
        int width = newbitmap.getWidth();
        int height = newbitmap.getHeight();
        // 设置想要的大小
        int newWidth = 256;
        int newHeight = 64;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap resizeBitmap = Bitmap.createBitmap(newbitmap, 0, 0, width, height, matrix,
                true);
        return resizeBitmap;
    }
    //转换灰度图
    private Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高

        int []pixels = new int[width * height]; //通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++)  {
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int)((float) red * 0.2989 + (float)green * 0.5870 + (float)blue * 0.1140);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }
}
