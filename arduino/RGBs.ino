#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <Adafruit_NeoPixel.h>
#include <ArduinoJson.h> 
#define LED1 D1
#define LED2 D2
#include <Adafruit_NeoPixel.h>
#define PIN   D3 //接esp8266D1脚
#define NUM   60//灯个数
#define NUM_OF_RGB  255  //RGB的灯范围
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUM, PIN, NEO_GRB + NEO_KHZ800);
unsigned int mac=0;
unsigned long previousMillis = 0;

//const char* ssid = "WUZEEN";//连接的路由器的名字
//const char* password = "wuzeen1996";//连接的路由器的密码

const char* ssid = "666";//连接的路由器的名字
const char* password = "456456456";//连接的路由器的密码


const char* mqtt_server = "39.108.118.166";//吴泽恩服务器//服务器的地址 iot.eclipse.org是开源服务器,实验室服务器：123.206.127.199

long lastMsg = 0;//存放时间的变量 

WiFiClient espClient;
PubSubClient client(espClient);

char msg[100];//存放要发的数据

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

 if(char(payload[0])=='0'){
  digitalWrite(LED1,LOW);
  Serial.println("灯1关了~~");
  
 }
 if(char(payload[0])=='1'){
  digitalWrite(LED1,HIGH);
  Serial.println("灯1开了");
  
 }
if(char(payload[0])=='2'){
  digitalWrite(LED2,LOW);
  Serial.println("灯2关了");
  
 }
 if(char(payload[0])=='3'){
  digitalWrite(LED2,HIGH);
  Serial.println("灯2开了");
 }
if(char(payload[0])=='4'){
  style1(NUM);
 }
if(char(payload[0])=='5'){
  style2(NUM);
 }
 if(char(payload[0])=='6'){
  style3(NUM);
 }
 if(char(payload[0])=='7'){
  for(int i=0;i<32;i++){
  style5(i);
 delay(50);
}
 }
 if(char(payload[0])=='8'){
  style0(NUM);
 }
 if(char(payload[0])=='9'){
  for(int i=0;i<32;i++){
  style6(8,i,0);
  style5(i);
 delay(10);
}
 }
 if(char(payload[0])=='a'){
  style00(NUM);
 }
 
}
void style00(int N){
  for(int i=0;i<N;i++){
   pixels.setPixelColor(i, pixels.Color(0,0,0)); //白灯
    pixels.show(); 
  }
}
void style0(int N){
  for(int i=0;i<N;i++){
   pixels.setPixelColor(i, pixels.Color(255,255,255)); //白灯
    pixels.show(); 
  }
}
void style1(int N){
  for(int i=0;i<N;i++){
   pixels.setPixelColor(i, pixels.Color(0,0,255)); 
    pixels.show(); 
  }
}

void style2(int N){
  for(int i=0;i<N;i++){
   pixels.setPixelColor(i, pixels.Color(255,255,0)); 
     pixels.show(); 
   delay(500);
     pixels.setPixelColor(i, pixels.Color(0,0,0));  
    pixels.show(); 
  }
}

void style3(int N){
  for(int i=0;i<N;i++){
   pixels.setPixelColor(i, pixels.Color(0,150,150)); 
     delay(500);
    pixels.show(); 
  }
}

void style4(int N){
  for(int i=0;i<N;i++){
   pixels.setPixelColor(i, pixels.Color(255,0,255)); 
    delay(500);
    pixels.show(); 
  }
}

void style5(int n){
  int location=0;//记录当前灯的位置
  //yello
  for(int i=0;i<8;i++){
    pixels.setPixelColor(location++, pixels.Color(255,255,n*i)); 
      pixels.show(); 
  }
  //red
   for(int i=0;i<8;i++){
    pixels.setPixelColor(location++, pixels.Color(255,0,n*i)); 
      pixels.show(); 
  }
  //orange
   for(int i=0;i<8;i++){
    pixels.setPixelColor(location++, pixels.Color(255,153,n*i)); 
      pixels.show(); 
  }
  //green
   for(int i=0;i<8;i++){
    pixels.setPixelColor(location++, pixels.Color(153,204,n*i)); 
      pixels.show(); 
  }
  //blue
   for(int i=0;i<8;i++){
    pixels.setPixelColor(location++, pixels.Color(n*i,255,255)); 
      pixels.show(); 
  }
  //purple
   for(int i=0;i<8;i++){
    pixels.setPixelColor(location++, pixels.Color(204,n*i,153)); 
      pixels.show(); 
  }
  //pink
   for(int i=0;i<8;i++){
    pixels.setPixelColor(location++, pixels.Color(255,153,n*i)); 
      pixels.show(); 
  }
}
//style(13,8,0)
void style6(int n,int num,int delaytime){
  int location=0;//记录当前灯的位置
  //yello
  for(int i=0;i<num;i++){
     int temp=location;
    pixels.setPixelColor(location++, pixels.Color(255,255,n*i)); 
      pixels.show(); 
     delay(delaytime);
     pixels.setPixelColor(temp, pixels.Color(0,0,0));  
    pixels.show(); 
  }
  //red
   for(int i=0;i<num;i++){
      int temp=location;
    pixels.setPixelColor(location++, pixels.Color(255,0,n*i)); 
      pixels.show(); 
       delay(delaytime);
     pixels.setPixelColor(temp, pixels.Color(0,0,0));  
    pixels.show(); 
  }
  //orange
   for(int i=0;i<num;i++){
      int temp=location;
    pixels.setPixelColor(location++, pixels.Color(255,153,n*i)); 
      pixels.show(); 
       delay(delaytime);
     pixels.setPixelColor(temp, pixels.Color(0,0,0));  
    pixels.show(); 
  }
  //green
   for(int i=0;i<num;i++){
      int temp=location;
    pixels.setPixelColor(location++, pixels.Color(153,204,n*i)); 
      pixels.show(); 
       delay(delaytime);
     pixels.setPixelColor(temp, pixels.Color(0,0,0));  
    pixels.show(); 
  }
  //blue
   for(int i=0;i<num;i++){
      int temp=location;
    pixels.setPixelColor(location++, pixels.Color(n*i,255,255)); 
      pixels.show(); 
       delay(delaytime);
     pixels.setPixelColor(temp, pixels.Color(0,0,0));  
    pixels.show(); 
  }
  //purple
   for(int i=0;i<num;i++){
     int temp=location;
    pixels.setPixelColor(location++, pixels.Color(204,n*i,153)); 
      pixels.show(); 
       delay(delaytime);
     pixels.setPixelColor(temp, pixels.Color(0,0,0));  
    pixels.show(); 
  }
  //pink
   for(int i=0;i<num;i++){
      int temp=location;
    pixels.setPixelColor(location++, pixels.Color(255,153,n*i)); 
      pixels.show(); 
       delay(delaytime);
     pixels.setPixelColor(temp, pixels.Color(0,0,0));  
    pixels.show(); 
  }
}
void reconnect() {//等待，直到连接上服务器
  while (!client.connected()) {//如果没有连接上
    if (client.connect("cwb")+mac) {//接入时的用户名，尽量取一个很不常用的用户名
      client.subscribe("wuzeen");//接收外来的数据时的intopic
    } else {
      Serial.print("failed, rc=");//连接失败
      Serial.print(client.state());//重新连接
      Serial.println(" try again in 5 seconds");//延时5秒后重新连接
      delay(5000);
    }
  }
}
void setup() {//初始化程序，只运行一遍
  pixels.begin();
  pinMode(LED1,OUTPUT);//控制灯亮暗
  pinMode(LED2,OUTPUT);
  Serial.begin(9600);//设置串口波特率（与烧写用波特率不是一个概念）
  setup_wifi();//自动连WIFI接入网络
  client.setServer(mqtt_server, 1883);//1883为端口号
  client.setCallback(callback); //用于接收服务器接收的数据
}

void loop() {
  // put your main code here, to run repeatedly:
  reconnect();
  client.loop();

}


