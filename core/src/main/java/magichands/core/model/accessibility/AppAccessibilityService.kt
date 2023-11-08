package magichands.core.model.accessibility

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import cn.vove7.andro_accessibility_api.AccessibilityApi
import cn.vove7.auto.core.AppScope
import timber.log.Timber

class AppAccessibilityService(override val enableListenPageUpdate: Boolean = true) :
    AccessibilityApi() {

    //页面更新回调
    override fun onPageUpdate(currentScope: AppScope) {
        Timber.tag("TAG").d("onPageUpdate: %s", currentScope)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        println("开启无障碍")
        acc = this;
//        val hostHandlerClass = Class.forName("magichands.core.env")
//        val hostHandlerField = hostHandlerClass.getDeclaredMethod(
//            "initAccessibility",
//            AppAccessibilityService::class.java
//        )
//        hostHandlerField.isAccessible = true
//        hostHandlerField.invoke(null, acc)
        magichands.core.Env.appAccessibilityService=this

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var acc: AppAccessibilityService
    }

}