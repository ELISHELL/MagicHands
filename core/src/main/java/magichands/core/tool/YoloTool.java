package magichands.core.tool;

import static magichands.core.tool.StringTool.isStartWithStorage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.tencent.yolov8ncnn.Yolov8Ncnn;

import magichands.core.Env;

public class YoloTool {
    @SuppressLint("StaticFieldLeak")
    static Context context = Env.Companion.getContext();

    public static Yolov8Ncnn yolov8ncnn = new Yolov8Ncnn();

    public static int current_model = -1;
    public static int current_cpugpu = -1;

    public static Yolov8Ncnn.Obj[] Detect(Bitmap bitmap) {
        Bitmap yourSelectedImage = null;
        yourSelectedImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        if (yourSelectedImage == null)
            return new Yolov8Ncnn.Obj[0];

        Yolov8Ncnn.Obj[] objects = yolov8ncnn.Detect(yourSelectedImage);

        return objects;

    }

    public static Boolean YoloV8Load(String name, String[] list, int model, int cpugpu) {
        boolean ret_init;
        if (!isStartWithStorage(name)) {
            ret_init = yolov8ncnn.loadModel(context.getAssets(), name, list, model, cpugpu);
        } else {
            ret_init = yolov8ncnn.loadModel1(name, list, model, cpugpu);
        }

        if (!ret_init) {
            return false;
        }
        return true;


    }


    //    public static Boolean YoloV8Load(String name ,int model,int cpugpu){
//
//        if (model==-2){
//
//        }else{
//            current_model=model;
//        }
//        if (cpugpu==-2){
//
//        }else{
//            current_cpugpu=cpugpu;
//        }
//
//
//        if (current_model!=-1&&current_cpugpu!=-1){
//            boolean ret_init = yolov8ncnn.loadModel(Global.getAssets(),name, current_model, current_cpugpu);
//            if (!ret_init)
//            {
//                return false;
//            }
//            return true;
//        }
//        return true;
//
//
//    }
//    public static Boolean YoloV8Load(String path,String name ,int model,int cpugpu){
//
//        if (model==-2){
//
//        }else{
//            current_model=model;
//        }
//        if (cpugpu==-2){
//
//        }else{
//            current_cpugpu=cpugpu;
//        }
//
//
//        if (current_model!=-1&&current_cpugpu!=-1){
//            boolean ret_init = yolov8ncnn.loadModel1(path,name, current_model, current_cpugpu);
//            if (!ret_init)
//            {
//                return false;
//            }
//            return true;
//        }
//        return true;
//
//
//    }
    public static Bitmap draw(Yolov8Ncnn.Obj[] objects, Bitmap b) {
        if (objects == null) {

            return b;
        }

        // draw objects on bitmap
        Bitmap rgba = b.copy(Bitmap.Config.ARGB_8888, true);

        final int[] colors = new int[]{
                Color.rgb(54, 67, 244),
                Color.rgb(99, 30, 233),
                Color.rgb(176, 39, 156),
                Color.rgb(183, 58, 103),
                Color.rgb(181, 81, 63),
                Color.rgb(243, 150, 33),
                Color.rgb(244, 169, 3),
                Color.rgb(212, 188, 0),
                Color.rgb(136, 150, 0),
                Color.rgb(80, 175, 76),
                Color.rgb(74, 195, 139),
                Color.rgb(57, 220, 205),
                Color.rgb(59, 235, 255),
                Color.rgb(7, 193, 255),
                Color.rgb(0, 152, 255),
                Color.rgb(34, 87, 255),
                Color.rgb(72, 85, 121),
                Color.rgb(158, 158, 158),
                Color.rgb(139, 125, 96)
        };

        Canvas canvas = new Canvas(rgba);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        Paint textbgpaint = new Paint();
        //textbgpaint.setColor(Color.WHITE);
        textbgpaint.setStyle(Paint.Style.FILL);

        Paint textpaint = new Paint();
        //textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(28);
        textpaint.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < objects.length; i++) {
            paint.setColor(colors[i % 19]);
            textbgpaint.setColor(colors[i % 19]);
            textpaint.setColor(Color.WHITE);

            canvas.drawRect(objects[i].x, objects[i].y, objects[i].x + objects[i].w, objects[i].y + objects[i].h, paint);

            // draw filled text inside image
            {
                String text = objects[i].label + "  " + String.format("%.1f", objects[i].prob * 100) + "%";

                float text_width = textpaint.measureText(text);
                float text_height = -textpaint.ascent() + textpaint.descent();

                float x = objects[i].x;
                float y = objects[i].y - text_height;
                if (y < 0)
                    y = 0;
                if (x + text_width > rgba.getWidth())
                    x = rgba.getWidth() - text_width;

                canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);

                canvas.drawText(text, x, y - textpaint.ascent(), textpaint);
            }
        }

        return rgba;
    }
}
