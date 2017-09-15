#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <SimpleDHT.h>
#include <ArduinoJson.h>
int pinDHT11 = D2;
SimpleDHT11 dht11;
unsigned int mac=0;
//连线：D2连dht11或dht22中间的DATA，ACC接nodemcu上的VIN接口（其实就是5V），GND接GND（地线）

const char* ssid = "WUZEEN";//连接的路由器的名字
const char* password = "wuzeen1996";//连接的路由器的密码

//const char* ssid = "yzzn";//连接的路由器的名字
//const char* password = "8888888888";//连接的路由器的密码

//const char* ssid = "YJS";//连接的路由器的名字
//const char* password = "YJS88888888";//连接的路由器的密码

//const char* mqtt_server = "47.93.196.49";//服务器的地址 iot.eclipse.org是开源服务器,实验室服务器：123.206.127.199

const char* mqtt_server = "39.108.118.166";//吴泽恩的服务器

long lastMsg = 0;//存放时间的变量 

WiFiClient espClient;
PubSubClient client(espClient);


char msg[100];//存放要发的数据
int Humidity=0;
int Temperature=0;

void setup_wifi() {//自动连WIFI接入网络
  delay(10);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
}

void callback(char* topic, byte* payload, unsigned int length) {//用于接收数据

  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);//串口打印接收到的数据
  }
  Serial.println();//换行

}

void reconnect() {//等待，直到连接上服务器
  while (!client.connected()) {//如果没有连接上
    if (client.connect("yinxin")+mac) {//接入时的用户名，尽量取一个很不常用的用户名
      client.subscribe("666");//接收外来的数据时的intopic
    } else {
      Serial.print("failed, rc=");//连接失败
      Serial.print(client.state());//重新连接
      Serial.println(" try again in 5 seconds");//延时5秒后重新连接
      delay(5000);
    }
  }
}


 void show(){
  Serial.print(Temperature); 
  Serial.print(" *C, "); 
  Serial.print(Humidity); 
  Serial.println(" H");
  Serial.println("json");
  Serial.println(msg);
 }

void DhT11(){
  byte temperature = 0;
  byte humidity = 0;
  int err = SimpleDHTErrSuccess;
  if ((err = dht11.read(pinDHT11, &temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
  //  Serial.print("Read DHT11 failed, err="); Serial.println(err);delay(1000);
    return;
  }
  Humidity=(int)humidity;
  Temperature=(int)temperature;
  delay(1000);
}



void encodeJson(){
  DynamicJsonBuffer jsonBuffer;
  JsonObject& root1 = jsonBuffer.createObject();
  root1["Humidity"] = Humidity;
  root1["Temperature"] = Temperature;
  root1.printTo(msg);
 }



void setup() {//初始化程序，只运行一遍
  Serial.begin(9600);//设置串口波特率（与烧写用波特率不是一个概念）
   mac=ESP.getChipId();
  setup_wifi();//自动连WIFI接入网络
  client.setServer(mqtt_server, 1883);//1883为端口号
  client.setCallback(callback); //用于接收服务器接收的数据
    Serial.println(mac);
}



void loop() {//主循环
   reconnect();//确保连上服务器，否则一直等待。
   client.loop();//MUC接收数据的主循环函数。
    DhT11();
    encodeJson();
    show();
    delay(10000);//让其过十秒再收一次，后期可能会再提高间隔
   long now = millis();//记录当前时间
   if (now - lastMsg > 1000) {//每隔1秒进行if内部语句
    client.publish("dht22", msg);//发送数据，其中temperature是发出去的outtopic
    lastMsg = now;//刷新上一次发送数据的时间
  }
}

