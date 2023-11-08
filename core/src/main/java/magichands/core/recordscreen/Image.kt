package magichands.core.recordscreen

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import magichands.core.OpenApi
import magichands.core.OpenApi.getCon
import java.nio.ByteBuffer

const val REQUEST_MEDIA_PROJECTION = 1

class Image {


    companion object {


        @SuppressLint("WrongConstant")
        fun init(ay: AppCompatActivity) {
            Env.mediaProjectionManager =
                ay.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            Env.handler = Handler(Looper.getMainLooper())
            Env.imageReader = ImageReader.newInstance(
                ScreenUtils.getScreenWidth(),
                ScreenUtils.getScreenHeight(), PixelFormat.RGBA_8888, 1
            )
        }

        fun startScreenCapture(ay: AppCompatActivity) {
            if (Env.mediaProjection == null) {
                Log.d("~~~", "Requesting confirmation")
                // This initiates a prompt dialog for the user to confirm screen projection.
                ay.startActivityForResult(
                    Env.mediaProjectionManager!!.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION
                )


            }

        }

        fun isScreenCapture(): Boolean {
            return Env.getMediaProjections() != null

        }


        fun setUpVirtualDisplay(
            callback: () -> Unit,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            Log.d("~~~", "setUpVirtualDisplay")
            Env.virtualDisplay = Env.mediaProjection!!.createVirtualDisplay(
                "ScreenCapture",
                ScreenUtils.getScreenWidth(),
                ScreenUtils.getScreenHeight(),
                ScreenUtils.getScreenDensityDpi(),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                Env.imageReader!!.surface, null, null
            )
            Env.handler!!.postDelayed({
                val image = Env.imageReader!!.acquireLatestImage()
                if (image != null) {
                    Log.d("~~~", "get image: $image")
                    val cropRect = Rect(left, top, right, bottom)
                    val saveTask = SaveTask(Env.mediaProjection, Env.virtualDisplay)
                    if (cropRect.left != -1 && cropRect.top != -1 && cropRect.width() != -1 && cropRect.height() != -1) {
                        print("范围")
                        Env.bitmap = saveTask.doInBackground(image)?.let {
                            Bitmap.createBitmap(
                                it,
                                cropRect.left,
                                cropRect.top,
                                cropRect.width(),
                                cropRect.height()
                            )
                        }
                    } else {
                        print("全屏")
                        Env.bitmap = saveTask.doInBackground(image)
                    }
//                ExposingApi.Global_Apc.finish()

                } else {
                    Log.d("~~~", "image == null")
                }
//            stopScreenCapture()
            }, 1000)
            callback.invoke()
        }

        @Suppress("DEPRECATION")
        class SaveTask(
            private val mediaProjection: MediaProjection?,
            private val virtualDisplay: VirtualDisplay?
        ) : AsyncTask<Image, Void, Bitmap?>() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            public override fun doInBackground(vararg params: Image): Bitmap? {
                if (params.isEmpty() || params[0] == null) {
                    return null
                }
                val image = params[0]
                val width = image.width
                val height = image.height
                val planes = image.planes
                val buffer: ByteBuffer = planes[0].buffer
                // 每个像素的间距
                val pixelStride = planes[0].pixelStride
                // 总的间距
                val rowStride = planes[0].rowStride
                val rowPadding = rowStride - pixelStride * width
                var bitmap = Bitmap.createBitmap(
                    width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888
                ) // even though ARGB8888 will consume more memory, it has better compatibility on the device.
                bitmap.copyPixelsFromBuffer(buffer)
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
                image.close()
                if (bitmap != null && !bitmap.isRecycled) {
                    return bitmap
                }
//            virtualDisplay?.release()
//            if (mediaProjection != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mediaProjection.stop()
//                }
//            }
                return null
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            public override fun onPostExecute(bitmap: Bitmap?) {
                super.onPostExecute(bitmap)
                // 在这里可以对返回的 bitmap 进行操作
                if (bitmap != null) {
                    // 在这里处理 Bitmap 对象，如显示在 ImageView 中或其他操作
                } else {
                    // bitmap 为 null，说明截图失败
                }
            }
        }

        fun release() {
            if (Env.virtualDisplay != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Env.virtualDisplay!!.release()
                }
            }
            if (Env.mediaProjection != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Env.mediaProjection!!.stop()
                }
            }
        }

        fun stopScreenCapture(): Boolean? {
            Log.d("~~~", "释放截图权限成功")
            Env.virtualDisplay?.release()
            Env.virtualDisplay = null
            Env.mediaProjection?.stop()
            Env.mediaProjection = null;
            return true
        }

    }


}

object ImageUtils {
    fun imageToBitmap(image: Image): Bitmap {
        val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(image.planes[0].buffer)
        image.close()
        return bitmap
    }
}



object ScreenUtils {

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels+OpenApi.device.getNavigationBarHeight(getCon())
    }

    fun getScreenDensityDpi(): Int {
        return Resources.getSystem().displayMetrics.densityDpi
    }
}


