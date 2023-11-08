package com.getcapacitor;

import android.util.Log;

public class Tab3Plugin extends Plugin {

    @PluginMethod
    public void handleButtonClick(PluginCall call) {
        String buttonId = call.getString("buttonId");
        Log.d("Tab3Plugin", "Button with ID '" + buttonId + "' clicked!");

        // 处理您的逻辑

        JSObject result = new JSObject();
        result.put("message", "Success");
        call.resolve(result);
    }
}