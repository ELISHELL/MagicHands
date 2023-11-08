package magichands.core

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hjq.toast.Toaster
import magichands.core.Permissions.Companion.ScreenCapture
import magichands.core.debug.Interaction.LOG_SERVER_URL
import magichands.core.debug.communication.Link
import magichands.core.model.accessibility.AppAccessibilityService
import magichands.service.MyForegroundService
import java.net.URL
import java.sql.DriverManager


class Env {

    companion object {
        @SuppressLint("StaticFieldLeak")

        var on: Boolean = false

        //        lateinit var context: Context
        var context: Context? = null
            get() = field
            set(value) {
                field = value
            }

        @SuppressLint("StaticFieldLeak")
        var appCompatActivity: AppCompatActivity? = null
            get() = field
            set(value) {
                field = value
            }

        @SuppressLint("StaticFieldLeak")
        var activity: Activity? = null
            get() = field
            set(value) {
                field = value
            }

        @SuppressLint("StaticFieldLeak")
        var application: Application? = null
            get() = field
            set(value) {
                field = value
            }

        @SuppressLint("StaticFieldLeak")
        var appAccessibilityService: AppAccessibilityService? = null
            get() = field
            set(value) {
                field = value
            }

        @SuppressLint("StaticFieldLeak")
        var currentInputConnection: InputMethodService? = null
            get() = field
            set(value) {
                field = value
            }

        @SuppressLint("StaticFieldLeak")
        var mainActivity: String? = null
            get() = field
            set(value) {
                field = value
            }

        var run = true

        fun init(context: Context, mainActivity: String, run: Boolean) {
            this.context = context
            this.activity = context as Activity
            this.appCompatActivity = context as AppCompatActivity
            this.mainActivity = mainActivity
            this.run = run
            magichands.core.model.accessibility.obj.privateApi.e(appCompatActivity!!)
            val serviceIntent = Intent(context, MyForegroundService::class.java)
            context.startService(serviceIntent)
//            requestPermission()
        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
            DriverManager.println("requestCode:" + requestCode + "resultCode:" + resultCode + "Intent:" + data)
            if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                } else {
                    Toast.makeText(context, "储存权限申请失败", Toast.LENGTH_SHORT).show()
                }
            }
            if (requestCode == 100) {
                if (!Settings.canDrawOverlays(context)) {
                    DriverManager.println("未授权")
                }
            } else {
                ScreenCapture(requestCode, resultCode, data)
            }
        }

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            if (requestCode == 1024) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                } else {

                }
            }


        }

        fun start(ip: String) {

            if (run) {
                OpenApi.start(false, true)
            } else {
                if (!on) {
                    Toaster.show("尝试链接Vscode")
                    LOG_SERVER_URL = "http://$ip/"
                    System.out.println("地址：$LOG_SERVER_URL")

                    if (checkUrl(LOG_SERVER_URL)) {
                        Toaster.show("成功链接")
                        Link()
                        on=true
                    } else {
                        Toaster.show("无法链接")
                        on=false
                    }

                }else{
                    Toaster.show("请勿重复链接")
                }


            }
        }

        fun checkUrl(urlStr: String?): Boolean {
            return try {
                val url = URL(urlStr)
                val conn = url.openConnection()
                conn.connectTimeout = 5000 // 设置连接超时时间为5秒
                conn.connect() // 尝试连接
                true // 如果连接成功，则返回true
            } catch (e: Exception) {
                e.printStackTrace() // 如果连接失败，则打印异常信息
                false // 返回false
            }
        }
    }


}