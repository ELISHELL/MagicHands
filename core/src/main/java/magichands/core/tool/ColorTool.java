package magichands.core.tool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

//import com.example.opencvlibrary.Mat;
//import com.example.opencvlibrary.OpencvOper;

public class ColorTool {

    /**
     * 获取指定坐标的 RGB 颜色值
     *
     * @param x 指定坐标的 x 值
     * @param y 指定坐标的 y 值
     * @return 返回一个 int 类型的 RGB 颜色值，其中低 8 位是蓝色分量，接下来的 8 位是绿色分量，最高的 8 位是红色分量
     */
    public static int getRGBColorAtPoint(Bitmap b, int x, int y) {
        // 创建一个 ARGB 8888 格式的 Bitmap 对象，宽和高都是 1
//        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        // 创建一个 Canvas 对象并将其绑定到 Bitmap 上
        Canvas canvas = new Canvas(b);
        // 在 Canvas 上绘制一个矩形，矩形的左上角和右下角坐标都是指定的 x 和 y 值
        canvas.drawRect(x, y, x + 1, y + 1, new Paint());
        // 从 Bitmap 对象中获取像素值，并返回 RGB 颜色值
        int color = b.getPixel(0, 0);
        // 释放资源
        b.recycle();
        return color;
    }





}
