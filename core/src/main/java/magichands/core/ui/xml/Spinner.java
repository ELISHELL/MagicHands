package magichands.core.ui.xml;

import android.view.View;
import android.widget.AdapterView;

import java.util.Map;

public class Spinner {

    public  static void setOnItemSelectedListener(android.widget.Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                Env.setSpinnerName(selectedOption);
                // 处理选中的选项
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 在这里处理下拉框没有选中任何选项的情况
                Env.setSpinnerName("1")  ;
            }
        });

    }

    public static android.widget.Spinner getSpinner(String id){

        for (Map.Entry<String, android.widget.Spinner> entry : Env.getSpinner_m().entrySet()) {
            String mapKey = entry.getKey();
            android.widget.Spinner mapValue = entry.getValue();
            if (mapKey.equals(id)){
                return  mapValue;
            }

        }
        return null;

    }

    public static String getSpinnerName() {

        return Env.getSpinnerName();
    }


}
