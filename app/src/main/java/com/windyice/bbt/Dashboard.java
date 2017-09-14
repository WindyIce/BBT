package com.windyice.bbt;

import android.content.Intent;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;



public class Dashboard extends AppCompatActivity {

    private final String HOST="tcp://39.108.118.166:1883";
    private final String clientId="2233";
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private ScheduledExecutorService scheduledExecutorService;
    private static List<String> toShow=new ArrayList<String>();
    private android.os.Handler handler;

    private void connect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqttClient.connect(mqttConnectOptions);
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Message message=new Message();
                    message.what=3;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final ListView listView_dashboard=(ListView) findViewById(R.id.dashboard_listview);
        final Button button_dashboard2main=(Button) findViewById(R.id.dashboard2main_button);
        final Button button_clearDashBoard=(Button) findViewById(R.id.clearDashBoard_button);

        /*final String HOST="tcp://39.108.118.166:23";
        final String clientId="2233";
        MqttClient mqttClient;
        MqttConnectOptions mqttConnectOptions;
        ScheduledExecutorService scheduledExecutorService;
        List<String> toShow=new ArrayList<String>();*/

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(Dashboard.this,android.R.layout.simple_list_item_1,toShow);
        listView_dashboard.setAdapter(arrayAdapter);


        try {
            mqttClient = new MqttClient(HOST, clientId, new MemoryPersistence());
            mqttConnectOptions=new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setConnectionTimeout(10);
            mqttConnectOptions.setKeepAliveInterval(20);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //reconnection can be here.
                    Toast.makeText(Dashboard.this,cause.getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("messageArrived----------");
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = topic+"---"+message.toString();
                    handler.sendMessage(msg);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //after publish
                    Toast.makeText(Dashboard.this,"DeliverComplete-------"+token.isComplete(),Toast.LENGTH_SHORT).show();
                }
            });
            //mqttClient.connect(mqttConnectOptions);
            connect();

        }
        catch (Exception e){
            Toast.makeText(Dashboard.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        handler=new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        toShow.add((String)msg.obj);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        Toast.makeText(Dashboard.this, "Connection successful", Toast.LENGTH_SHORT).show();
                        try {
                            for(String a:MainActivity.topicsChosen){
                                mqttClient.subscribe(a);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        Toast.makeText(Dashboard.this, "Connection fail          Reconnecting......", Toast.LENGTH_SHORT).show();
                        startReconnect();
                        break;
                    default:
                        break;
                }
            }
        };



        button_clearDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toShow.clear();
                arrayAdapter.notifyDataSetChanged();
            }
        });

        button_dashboard2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startReconnect() {
        scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(!mqttClient.isConnected()){
                    connect();
                }
            }
        },0*1000,10*1000, TimeUnit.MILLISECONDS);
    }
}
