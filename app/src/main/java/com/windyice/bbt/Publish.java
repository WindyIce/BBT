package com.windyice.bbt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Publish extends AppCompatActivity {
    private final String HOST="tcp://39.108.118.166:1883";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Button button_control_1=(Button) findViewById(R.id.control1_button);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        /*button_control_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }
}
