package com.windyice.bbt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Subscribe extends AppCompatActivity {

    private String topicChosen="";
    private List<String> topicsChosen=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);


        final Spinner spinner_subscribe=(Spinner) findViewById(R.id.subscribe_spinner);
        final Button button_subscribe=(Button) findViewById(R.id.subscribe_button);
        final Button button_delete=(Button) findViewById(R.id.delete_button);
        final Button button_return=(Button) findViewById(R.id.subscribe2main_button);
        final TextView textView_subscribed=(TextView) findViewById(R.id.subscribe_textview);

        textView_subscribed.setText("");
        for(String a:MainActivity.topicsChosen){
            textView_subscribed.append(""+a);
        }

        final String[] topics=getResources().getStringArray(R.array.topics);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,topics);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_subscribe.setAdapter(adapter);
        spinner_subscribe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                topicChosen=topics[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                topicChosen="";
            }
        });



        //Add topics logic!!!
        button_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topicChosen.equals("")){
                    Toast.makeText(Subscribe.this,"Nothing selected!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(MainActivity.topicsChosen.contains(topicChosen)){
                        Toast.makeText(Subscribe.this,"Selected topic is chosen already!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        MainActivity.topicsChosen.add(topicChosen);
                        textView_subscribed.setText("");
                        for(String a:MainActivity.topicsChosen){
                            textView_subscribed.append("\n"+a);
                        }
                    }
                }
            }
        });


        //Delete topics logic!!!
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topicChosen.equals("")){
                    Toast.makeText(Subscribe.this,"Nothing selected!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!MainActivity.topicsChosen.contains(topicChosen)){
                        Toast.makeText(Subscribe.this,"Selected topic has not been chosen!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        MainActivity.topicsChosen.remove(topicChosen);
                        textView_subscribed.setText("");
                        for(String a:MainActivity.topicsChosen){
                            textView_subscribed.append("\n"+a);
                        }
                    }
                }
            }
        });


        //Return logic!
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String toPass[]=(String[])topicsChosen.toArray();
                Intent intent=new Intent(Subscribe.this,MainActivity.class);
                //intent.putExtra("topicsChosen",toPass);
                startActivity(intent);
            }
        });


    }
}
