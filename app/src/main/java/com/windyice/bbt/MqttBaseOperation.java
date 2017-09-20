package com.windyice.bbt;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 32699 on 2017/9/17.
 */

public class MqttBaseOperation {
    private String HOST;
    private String clientId;
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private Handler handler;

    public MqttBaseOperation(String _HOST, String _clientId){
        HOST=_HOST;
        clientId=_clientId;
    }

    public void setCallback(MqttCallback mqttCallback){
        mqttClient.setCallback(mqttCallback);
    }

    public void setHandler(Handler handler){
        this.handler=handler;
    }

    public void connect(){
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

    public void connect(boolean withoutHandler){
        if(withoutHandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqttClient.connect(mqttConnectOptions);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        }
        else  connect();
    }

    public void startReconnect(long reconnectRate) {
        //final long reconnectRate=1*3000;
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(!mqttClient.isConnected()){
                    connect();
                }
            }
        },0,reconnectRate, TimeUnit.MILLISECONDS);
    }

    public void startReconnect(long reconnectRate,boolean withoutHandler){
        if(withoutHandler){
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if(!mqttClient.isConnected()){
                        connect(true);
                    }
                }
            },0,reconnectRate, TimeUnit.MILLISECONDS);
        }
        else startReconnect(reconnectRate);
    }

    public void Setting(boolean willCleanSession,int connectionTimeout,int keepAliveInterval){
        try{
            mqttClient=new MqttClient(HOST,clientId,new MemoryPersistence());
            mqttConnectOptions=new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(willCleanSession);
            mqttConnectOptions.setConnectionTimeout(connectionTimeout);
            mqttConnectOptions.setKeepAliveInterval(keepAliveInterval);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Handler getHandler(){
        return handler;
    }

    public void subscribe(String a){
        try {
            mqttClient.subscribe(a);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void subscribe(List<String> a){
        try{
            for(String b:a){
                mqttClient.subscribe(b);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void publish(String topic,MqttMessage message){
        try{
            mqttClient.publish(topic,message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
