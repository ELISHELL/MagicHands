package magichands.core.ui.xml

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class Env {

    companion object {
        @JvmStatic
        var button_m: Map<String, Button> = HashMap()
        @JvmStatic
        var spinner_m: Map<String, Spinner> = HashMap()
        @JvmStatic
        var edittext_m: Map<String, EditText> = HashMap()
        @JvmStatic
        var spinnerName: String? = null
    }

}