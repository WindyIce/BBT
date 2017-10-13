package com.windyice.bbt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static List<String> topicsChosen=new ArrayList<>();
    public static boolean hasLogin=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_main2subscribe=(Button) findViewById(R.id.button_main2subscribe);
        final Button button_main2dashboard=(Button) findViewById(R.id.button_main2dashboard);
        final Button button_main2publish=(Button) findViewById(R.id.button_main2publish);
        final Button button_main2control=(Button) findViewById(R.id.button_main2control);
        final Button button_main2audiocontrol=(Button) findViewById(R.id.button_main2audiocontrol);
        final Button button_main2LDrawer=(Button) findViewById(R.id.button_main2LDrawer);

        Intent intent=getIntent();
        //String[] topicChosen=intent.getStringArrayExtra("topicsChosen");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_login = (FloatingActionButton) findViewById(R.id.fab_login);
        fab_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasLogin) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    //after login
                }
            }
        });

        button_main2subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Subscribe.class);
                startActivity(intent);
            }
        });

        button_main2dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Dashboard.class);
                startActivity(intent);
            }
        });

        button_main2publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Publish.class);
                startActivity(intent);
            }
        });

        button_main2control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Control.class);
                startActivity(intent);
            }
        });


        button_main2LDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NavigationMain.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_subscribe) {
            //Intent intent=new Intent(MainActivity.this,Subscribe.class);
            //startActivity(intent);
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
