package magichands.core.ui.vue;



import static magichands.core.ui.vue.InterActive.button_vue.m;

import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import magichands.core.ui.xml.Env;
@CapacitorPlugin(name = "InterActive")
public class InterActive extends Plugin {


   public static class button_vue {
        public static button_vue.l m;
        public  void s(button_vue.l i){
            m=i;
        }
        public  interface l{
            void onclick(String id);
        }
    }
    @PluginMethod
    public  void canOpenUrl(PluginCall call){

        if (call.getData() == null) {
            call.reject("Must supply a Data");
            return;
        }
        String id = call.getString("id");
        Map<String, String> dataMap = jsonToMap(call.getData().toString());
        boolean containsOnlyId = containsOnlyId(dataMap);
        if (!containsOnlyId){
            // 要写入的目标Map

            // 使用putOrReplaceValue方法将键值对写入Map
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                putOrReplaceValue(magichands.core.ui.vue.Env.getCallmap(), entry.getKey(), entry.getValue());
            }
        }
        JSObject ret = new JSObject();
        try {
            if (m != null){
                m.onclick(id);
            }
            ret.put("value", true);
            call.resolve(ret);
            return;
        } catch (Exception e) {
            Logger.error(getLogTag(), "Package id '" + id + "' not found!", null);
        }

        ret.put("value", false);
        call.resolve(ret);
    }

    public static void onClick(button_vue.l callback) {
       button_vue bu = new button_vue();
        bu.s(new button_vue.l() {
            @Override
            public void onclick(String id) {
                callback.onclick(id);
            }
        });
    }


    public static <K, V> void putOrReplaceValue(Map<K, V> map, K key, V value) {
        if (map == null) {
            return;
        }

        V existingValue = map.get(key);
        if (existingValue == null || !existingValue.equals(value)) {
            // 如果键名不存在或键值不同，则追加新的键值对或替换原有的值
            map.put(key, value);
        }
    }
    public static Map<String, String> jsonToMap(String jsonString) {
        Gson gson = new Gson();

        // 使用TypeToken获取Map<String, String>类型的Type
        Type type = new TypeToken<Map<String, String>>() {}.getType();

        // 将JSON字符串解析为Map
        return gson.fromJson(jsonString, type);
    }

    public static boolean containsOnlyId(Map<String, String> map) {
        return map != null && map.size() == 1 && map.containsKey("id");
    }



}
