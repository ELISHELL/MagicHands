package magichands.core.ui.vue

import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class Env {

    companion object {
        @JvmStatic
        var callmap: Map<String, String> = java.util.HashMap()
        @JvmStatic
        var ip: String? = null
    }

}