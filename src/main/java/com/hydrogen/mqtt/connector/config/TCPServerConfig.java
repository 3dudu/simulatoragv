package com.hydrogen.mqtt.connector.config;

import java.io.IOException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hydrogen.mqtt.connector.car.House;
import com.hydrogen.mqtt.connector.msghandle.AGVMessageHandle;
import com.hydrogen.mqtt.connector.msghandle.AbstractMsgHandlerFactory;
import com.hydrogen.mqtt.connector.msghandle.TCPServer;
import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.codec.AGVCodecFactory;
import com.hydrogen.mqtt.connector.msghandle.agv.handler.AGVMsgHandlerFactory;

@Configuration
public class TCPServerConfig {
	@Value("${tcpserver.host:0.0.0.0}")
	private String host;

	@Value("${tcpserver.port:0}")
	private int port;

	@Value("${tcpserver.heapBuffer:true}")
	private boolean heapBuffer;

	@Value("${tcpserver.processor:0}")
	private int processorThreads;

	@Value("${tcpserver.codec:0}")
	private int codecThreads;

	@Value("${tcpserver.timeout:0}")
	private int timeout;

	@Value("${tcpserver.idletime:0}")
	private int idletime;

	@Value("${tcpserver.white}")
	private String[] white;

	@Value("${car.speed:500}")
	private int speed;

	@Value("${car.steptime:200}")
	private int steptime;

	@Bean("TCPServer")
	public TCPServer getTCPServer(IoHandlerAdapter messageHandle) {
		AGVCar.intSpeed(steptime, speed);
		//热车
		AGVCar car = new AGVCar();
    	car.setId(0);
		House.addCar(car);
		car.init();
		car.start();
		
		TCPServer server = new TCPServer();
		server.setPort(port);
		try {
			server.start(protocolCodecFactory(), messageHandle);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return server;
	}

	@Bean
	public ProtocolCodecFactory protocolCodecFactory() {
		return new AGVCodecFactory();
	}

	@Bean("msgHandlerFactory")
	public AbstractMsgHandlerFactory msgHandlerFactory() {
		return new AGVMsgHandlerFactory();
	}

	@Bean("messageHandle")
	public IoHandlerAdapter messageHandle(AbstractMsgHandlerFactory msgHandlerFactory) {
		AGVMessageHandle handler = new AGVMessageHandle(idletime, white);
		handler.register(msgHandlerFactory);
		return handler;
	}

}
