package magichands

import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.toyobayashi.nodeexample.App
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
import magichands.core.Env.Companion.application
import magichands.core.OpenApi.getCon

class init : App() {


    override fun onCreate() {
        super.onCreate()
        Toaster.init(this);
        DeviceIdentifier.register(this)
        application = this
    }
}