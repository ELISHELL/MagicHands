package magichands.core.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Base64;

//import com.example.opencvlibrary.Mat;
//import com.example.opencvlibrary.OpencvOper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageTool {




    public static Bitmap getBitmapFromInputStream(InputStream inputStream) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    public static byte[] bitmapByte(Bitmap bitmap) {
        if (null == bitmap) throw new NullPointerException();
        // if (null == bitmap) return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //把bitmap100%高质量压缩 到 output对象里
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }


    public static String byteBase64(byte[] imageByte) {
        if(null == imageByte) return null;
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }


    public static Bitmap base64Bitmap(String base64String) {
        if (null == base64String) throw new NullPointerException();
        byte[] decode = Base64.decode(base64String.split(",")[1], Base64.DEFAULT);
        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return mBitmap;
    }





}
