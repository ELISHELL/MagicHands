package magichands.service;



import static magichands.core.ui.xml.Parse.parseLayout1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import magichands.core.OpenApi;


public class XmlService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(parseLayout1(this, this.getExternalFilesDir("debug") + "/ui/xml/layout.xml"));
//        OpenApi.ui.xml.showViewOnScreen(, this);



    }


}
