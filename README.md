## 编译
```
mvn package
```
## 启动
```
cd bin
./start.sh
```
## 配置项

```
tcpserver.port  #服务端口

tcpserver.white  #IP白名单，多个IP逗号隔开
```

外部要连的话要修改application.yml里面的地址白名单

## 代码结构

> com.hydrogen.mqtt.connector.msghandle 

tcp服务，依赖mina网络开发框架。

> com.hydrogen.mqtt.connector.msghandle.coder 

协议编解码实现

> com.hydrogen.mqtt.connector.msghandle.msg

具体的业务消息 
