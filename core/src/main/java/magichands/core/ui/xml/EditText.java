package magichands.core.ui.xml;

import java.util.Map;

public class EditText {

    public static String getText(android.widget.EditText editText){
        String text = editText.getText().toString();
        return text;
    }

    public static void setText(android.widget.EditText editText,String text){
        editText.setText(text);
    }


    public static android.widget.EditText getEditText(String id){

        for (Map.Entry<String, android.widget.EditText> entry : Env.getEdittext_m().entrySet()) {
            String mapKey = entry.getKey();
            android.widget.EditText mapValue = entry.getValue();
            if (mapKey.equals(id)){
                return  mapValue;
            }


        }
        return null;
    }
}
