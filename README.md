# NeoSocket
[![ownner](https://img.shields.io/badge/owner-neocross-green.svg)](http://www.neocorss.cn)
[![maven](https://img.shields.io/badge/maven-v1.0.5-ff69b4.svg)](https://bintray.com/neocross2017/maven/NeoSocket)
[![license](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![download aar](https://img.shields.io/badge/Download-aar-yellowgreen.svg)](https://dl.bintray.com/neocross2017/maven/cn/neocross/libs/neosocket/1.0.5/neosocket-1.0.5.aar)

![logo](https://github.com/neocross/NeoSocket/blob/master/library/pom_icon.png)

Socket.Java 快速开发框架，用于简单 Socket 层通讯，支持基于 Java NIO 技术的 TCP/UDP 应用程序开发、串口通讯等。

* 数据通过JSON传递
* 可实现双向通讯,一个机器同时拥有服务端和客户端
* 多线程消息循环,支持多客户端
* 主线程回调

![](https://github.com/neocross/NeoSocket/blob/master/screenshot/pic_server.png)
![](https://github.com/neocross/NeoSocket/blob/master/screenshot/pic_client.png)

## Maven build settings
build.gradle
```gradle
dependencies {
  compile 'cn.neocross.libs:neosocket:1.0.5'
}
```
or maven
```maven
<dependency>
  <groupId>cn.neocross.libs</groupId>
  <artifactId>neosocket</artifactId>
  <version>1.0.5</version>
  <type>pom</type>
</dependency>
```
or lvy
```lvy
<dependency org='cn.neocross.libs' name='neosocket' rev='1.0.5'>
  <artifact name='neosocket' ext='pom' ></artifact>
</dependency>
```
## Include
- NeoSocketClient
- NeoSocketServer
- thread
- bean

## Usage
1. Start Server
```java
NeoSocketServer server = new NeoSocketServer(5556, new NeoSocketServerCallback() {
    @Override
    public void onServerStatusChanged(MsgEngine msgEngine) {
        System.out.println("Server status: " + msgEngine);
    }
    @Override
    public void onServerMsgReceived(Object message) {
        System.out.println("Server receive the client message: " + message.toString());
    }
});
```
2. Client Connect
```java
NeoSocketClient client = new NeoSocketClient(InetAddress.getLocalHost(), 5556);
```
3. Send Message
```java
client.send(new InstantMessage(StatusType.TYPE_MSG, "client say Hello!"));
client.addClientListener(new NeoSocketClientCallback() {
    @Override
    public void onClientStatusChange() {
    System.out.println("ClientStatusChanged");
    }
    @Override
    public void onClientMessageReceived(Object msg) {
        System.out.println("Server return: " + msg.toString);
    }
});
```
4. Close
```java
client.close();
server.close();
```

## Attention
- When the client connects to the server, please do it in the worker thread.
- Intent permission need to be declared in AndroidManifest.xml.

## License

    Copyright 2017 neocross

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
