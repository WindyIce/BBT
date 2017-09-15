#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
// Trig接Arduino板的Digital 5口，触发测距；Echo接Digital 4口，接收距离信号。
#define inputPin D1 // 定义超声波信号接收接口
#define outputPin D2    // 定义超声波信号发出接口
#define ledpin   D3     //亮灯
unsigned int mac=0;//mac地址

const char* ssid = "WUZEEN";//连接的路由器的名字
const char* password = "wuzeen1996";//连接的路由器的密码
//const char* ssid = "yzzn";//连接的路由器的名字
//const char* password = "8888888888";//连接的路由器的密码

//const char* ssid = "YJS";//连接的路由器的名字
//const char* password = "YJS88888888";//连接的路由器的密码
//const char* mqtt_server = "47.93.196.49";//服务器的地址 iot.eclipse.org是开源服务器,实验室服务器：123.206.127.199
const char* mqtt_server = "39.108.118.166";//服务器的地址 iot.eclipse.org是开源服务器,实验室服务器：123.206.127.199

long lastMsg = 0;//存放时间的变量 

WiFiClient espClient;
PubSubClient client(espClient);


char msg[100];//存放要发的数据
int Distance=0;

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
    if (client.connect("wuzeenyi")+mac) {//接入时的用户名，尽量取一个很不常用的用户名
      client.subscribe("testdistance");//接收外来的数据时的intopic
    } else {
      Serial.print("failed, rc=");//连接失败
      Serial.print(client.state());//重新连接
      Serial.println(" try again in 5 seconds");//延时5秒后重新连接
      delay(5000);
    }
  }
}

void encodeJson(){
  DynamicJsonBuffer jsonBuffer;
  JsonObject& root1 = jsonBuffer.createObject();
  root1["Distance"] = Distance;
  root1.printTo(msg);
 }



void setup() {//初始化程序，只运行一遍
  Serial.begin(9600);//设置串口波特率（与烧写用波特率不是一个概念）
  mac=ESP.getChipId();
  pinMode(ledpin,OUTPUT);
  pinMode(inputPin, INPUT);
  pinMode(outputPin, OUTPUT);
  setup_wifi();//自动连WIFI接入网络
  client.setServer(mqtt_server, 1883);//1883为端口号
  client.setCallback(callback); //用于接收服务器接收的数据
  Serial.println(mac);
}

void HC_SR04(){    
digitalWrite(outputPin, LOW); // 使发出发出超声波信号接口低电平2μs
delayMicroseconds(2);
digitalWrite(outputPin, HIGH); // 使发出发出超声波信号接口高电平10μs，这里是至少10μs
delayMicroseconds(10);
digitalWrite(outputPin, LOW); // 保持发出超声波信号接口低电平
int distance = pulseIn(inputPin, HIGH); // 读出脉冲时间
distance= distance/58; // 将脉冲时间转化为距离（单位：厘米）
Distance=distance;//将所测的距离进行赋值
Serial.println(distance); //输出距离值 
delay(50); 
if (distance >=50)
{//如果距离大于50厘米小灯熄灭
digitalWrite(ledpin,LOW);
}//如果距离小于50厘米小灯亮起
else
digitalWrite(ledpin,HIGH);
//在此处我有新想法，在数据不变时不发数据，在数据变化后发数据。或者在数据比较大时不发数据，因为可以理解这个时候没人靠近，在有人靠近时发数据，可以结合其他指令来一起控制
}

void loop() {//主循环
   HC_SR04();
   reconnect();//确保连上服务器，否则一直等待。
   client.loop();//MUC接收数据的主循环函数。
    encodeJson();
   long now = millis();//记录当前时间
   if (now - lastMsg > 1000) {//每隔1秒进行if内部语句
    client.publish("distance", msg);//发送数据，其中temperature是发出去的outtopic
    lastMsg = now;//刷新上一次发送数据的时间
  }
}

