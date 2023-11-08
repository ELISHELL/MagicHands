package magichands.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
import magichands.core.Env
import magichands.core.OpenApi
import magichands.core.debug.Windows
import magichands.core.ui.vue.InterActive

open class xml : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OpenApi.ui.xml.showViewOnScreen(OpenApi.ui.xml.parseLayout(this, "ui/xml/layout.xml"), this)
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


        OpenApi.start(false,true)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { Env.onActivityResult(requestCode, resultCode, it) }
    }
}