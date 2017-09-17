package com.windyice.bbt;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Control extends AppCompatActivity {

    private final String HOST="tcp://39.108.118.166:1883";
    private final String clientId="2233";
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private ScheduledExecutorService scheduledExecutorService;
    //private static List<String> toShow=new ArrayList<String>();
    //private android.os.Handler handler;

    private class ControlThread implements Runnable {
        private byte[] toPass;

        public ControlThread(byte[] a){
            toPass=a;
        }

        @Override
        public void run() {
            try {
                MqttMessage mqttMessage=new MqttMessage(toPass);
                mqttClient.publish("wuzeen", mqttMessage);
            }
            catch (Exception e){
                //Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }



    private void connect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqttClient.connect(mqttConnectOptions);
                    //Message message=new Message();
                    //message.what=2;
                    //Toast.makeText(Control.this, "Connection successful", Toast.LENGTH_SHORT).show();
                    Log.d("connection","connection successful");
                    //handler.sendMessage(message);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d("connection","connection failed");
                    //Message message=new Message();
                    //message.what=3;
                    //Toast.makeText(Control.this, "Connection fail          Reconnecting......", Toast.LENGTH_SHORT).show();
                    startReconnect();
                    //handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void control(String signal){
        /*final MqttMessage message=new MqttMessage(signal.getBytes());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqttClient.publish("wuzeen", message);
                }
                catch (Exception e){
                    //Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).start();*/
        Control.ControlThread controlThread=new Control.ControlThread(signal.getBytes());
        new Thread(controlThread).start();
        startReconnect();
    }

    private void startReconnect() {
        final long reconnectRate=1*3000;
        scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(!mqttClient.isConnected()){
                    connect();
                }
            }
        },0,reconnectRate, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        final Button button_control_0=(Button) findViewById(R.id.control0_button);
        final Button button_control_1=(Button) findViewById(R.id.control1_button);
        final Button button_control_2=(Button) findViewById(R.id.control2_button);
        final Button button_control_3=(Button) findViewById(R.id.control3_button);
        final Button button_control_4=(Button) findViewById(R.id.control4_button);
        final Button button_control_5=(Button) findViewById(R.id.control5_button);
        final Button button_control_6=(Button) findViewById(R.id.control6_button);
        final Button button_control_7=(Button) findViewById(R.id.control7_button);
        final Button button_control_8=(Button) findViewById(R.id.control8_button);
        final Button button_control_9=(Button) findViewById(R.id.control9_button);
        final Button button_control_10=(Button) findViewById(R.id.control10_button);
        final Button button_control_11=(Button) findViewById(R.id.control11_button);

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
                    Toast.makeText(Control.this,cause.getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if(MainActivity.topicsChosen.contains(topic)) {
                        System.out.println("messageArrived----------");
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = topic + "---" + message.toString();
                        //handler.sendMessage(msg);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //after publish
                    Toast.makeText(Control.this,"DeliverComplete-------"+token.isComplete(),Toast.LENGTH_SHORT).show();
                }
            });
            //mqttClient.connect(mqttConnectOptions);
            connect();

        }
        catch (Exception e){
            Toast.makeText(Control.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }




        button_control_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("0");
            }
        });

        button_control_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("1");
            }
        });

        button_control_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("2");
            }
        });

        button_control_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("3");
            }
        });

        button_control_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("4");
            }
        });

        button_control_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("5");
            }
        });

        button_control_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("6");
            }
        });

        button_control_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("7");
            }
        });

        button_control_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("8");
            }
        });

        button_control_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("9");
            }
        });

        button_control_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("10");
            }
        });

        button_control_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("11");
            }
        });

    }
}
