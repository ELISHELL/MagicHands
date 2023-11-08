package magichands.core

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Objects

const val REQUEST_MEDIA_PROJECTION = 1

class Permissions {
    companion object {


        fun ScreenCapture(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
            if (requestCode == REQUEST_MEDIA_PROJECTION) {
                if (resultCode != Activity.RESULT_OK) {
                    Log.d("~~~", "User cancelled")
                    return false
                }
                Log.d("~~~", "Starting screen capture")
                Objects.requireNonNull(magichands.core.recordscreen.Env.mediaProjectionManager)
                    .let {
                        it?.let { it1 ->
                            magichands.core.recordscreen.Env.setMediaProjections(
                                it1.getMediaProjection(resultCode, data!!)
                            )
                        }
                    }
                return true
            }
            return false
        }

        fun requestFloatWindowPermission(a: AppCompatActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(a)) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.data = Uri.parse("package:" + a.packageName)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    a.startActivity(intent)
                }
            }
        }

        fun requestPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // 先判断有没有权限
                if (Environment.isExternalStorageManager()) {
                    println("储存权限正常")
                } else {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data =
                        Uri.parse("package:" + OpenApi.getAct().applicationContext.packageName)
                    OpenApi.getAct().startActivityForResult(intent, 1024)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 先判断有没有权限
                if (ActivityCompat.checkSelfPermission(
                        OpenApi.getAct(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        OpenApi.getAct(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    println("储存权限正常")
                } else {
                    ActivityCompat.requestPermissions(
                        OpenApi.getAct(),
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1024
                    )
                }
            } else {
                println("储存权限其他申请情况！")
            }
        }


    }


}