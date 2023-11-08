package magichands.example

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import com.getcapacitor.BridgeActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
import magichands.core.Env
import magichands.core.OpenApi

import magichands.core.OpenApi.device.checkAccessibilityPermissions
import magichands.core.OpenApi.device.openAccessibilitySettings
import magichands.core.OpenApi.sleep
import magichands.core.OpenApi.ui.vue.getVueValue
import magichands.core.OpenApi.windows.requestFloatWindowPermission
import magichands.core.debug.Windows
import magichands.core.debug.Windows.wsShow
import magichands.core.ui.vue.InterActive
import magichands.ui.vue
import magichands.ui.xml


class MainActivity :  BridgeActivity() {
    var ui: Handler = magichands.core.Handler.ui
    var toast: Handler = magichands.core.Handler.toast
    var debugWindows: Handler? = magichands.core.Handler.debugWindows
    var ws:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Env.init(this, this::class.java.name,false)
        XXPermissions.with(this)
            // 申请单个权限
            .permission(Permission.RECORD_AUDIO)
            // 申请多个权限
//            .permission(Permission.Group.CALENDAR)
            .permission(Permission.GET_INSTALLED_APPS)
            .permission(Permission.SCHEDULE_EXACT_ALARM)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .permission(Permission.READ_PHONE_STATE)
            .permission(Permission.SYSTEM_ALERT_WINDOW)
            .permission(Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            // 设置权限请求拦截器（局部设置）
            //.interceptor(new PermissionInterceptor())
            // 设置不触发错误检测机制（局部设置）
            //.unchecked()
            .request(object : OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        Toaster.show("获取部分权限成功，但部分权限未正常授予")
                        return
                    }
                    Toaster.show("所需权限正常")
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        Toaster.show("被永久拒绝授权，请手动授予录音和日历权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(OpenApi.getCon(), permissions)
                    } else {
                        Toaster.show("获取所需权限失败")
                    }
                }
            })
        InterActive.onClick { id -> // 在这里处理回调逻辑
            when (id) {
                "1" -> {
                    println("1")
                    val ip = OpenApi.ui.vue.getVueValue()["ip"]
                    if (ip != null)
//                        OpenApi.sleep(2000)
                        ip?.let { Env.start(it) }
                }

                "2" -> {
                    if (!checkAccessibilityPermissions()) {
                        Toaster.show( "无障碍权限未开启")

                        openAccessibilitySettings()
                    }else{
                        Toaster.show( "无障碍权限正常")
                    }
                }

                "3" -> {
                    println("3")
                    if (Settings.canDrawOverlays(this)) {
                        Windows.wsShow()
                    } else {
                        OpenApi.windows.requestFloatWindowPermission(this)
                        println("悬浮窗权限不正常")
                    }

                }

                "4" -> {
                    println("4")


                }

                else -> println("其他值")
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { Env.onActivityResult(requestCode, resultCode, it) }
    }
}