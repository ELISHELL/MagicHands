package magichands.core.ui.xml;

import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Button {

   public static class button {
        public static l m;
        public  void s(l i){
            m=i;
        }
        public  interface l{
            void onclick(android.widget.Button id);
        }
    }


    public static void onClick(button.l callback) {
        button bu = new button();
        bu.s(new button.l() {
            @Override
            public void onclick(android.widget.Button id) {
                callback.onclick(id);
            }
        });
    }

    public  static void setOnClickListener(android.widget.Button b, AppCompatActivity c){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击的id："+b);
                if (button.m != null){
                    new Thread("事件") {
                        @Override
                        public void run() {
                            button.m.onclick((android.widget.Button)v);
                        }
                    }.start();
                }
            }
        });
    }
    public static android.widget.Button getButton(String id){

        for (Map.Entry<String, android.widget.Button> entry : Env.getButton_m().entrySet()) {
            String mapKey = entry.getKey();
            android.widget.Button mapValue = entry.getValue();
            if (mapKey.equals(id)){
                return  mapValue;
            }

        }
        return null;

    }
}
