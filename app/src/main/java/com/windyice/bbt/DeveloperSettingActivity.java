package com.windyice.bbt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeveloperSettingActivity extends AppCompatActivity {

    private Button button_develop;
    private EditText editText_develop;
    private Button button_develop_direct;
    private TextView textView_develop;
    public static int addedTopics=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button_develop=(Button) findViewById(R.id.developer_buttonf);
        editText_develop=(EditText) findViewById(R.id.developer_subscribe_edittext);
        button_develop_direct=(Button) findViewById(R.id.developer_direct_buttonf);
        textView_develop=(TextView) findViewById(R.id.developer_textview);

        for(int i=MainActivity.topicsChosen.size()-addedTopics;i<MainActivity.topicsChosen.size();++i){
            textView_develop.append("\n"+MainActivity.topicsChosen.get(i));
        }

        button_develop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubscribeFragment.topics.add(editText_develop.getText().toString());
                editText_develop.setText("");
            }
        });

        button_develop_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.topicsChosen.add(editText_develop.getText().toString());
                textView_develop.append("\n"+editText_develop.getText().toString());
                ++addedTopics;
                editText_develop.setText("");
            }
        });
    }

}
