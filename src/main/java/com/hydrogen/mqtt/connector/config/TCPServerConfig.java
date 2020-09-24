package com.hydrogen.mqtt.connector.config;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hydrogen.mqtt.connector.TCPServer;
import com.hydrogen.mqtt.connector.car.House;
import com.hydrogen.mqtt.connector.msghandle.AGVMessageHandle;
import com.hydrogen.mqtt.connector.msghandle.AbstractMsgHandlerFactory;
import com.hydrogen.mqtt.connector.msghandle.MinaTCPServer;
import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.codec.AGVCodecFactory;
import com.hydrogen.mqtt.connector.msghandle.agv.handler.AGVMsgHandlerFactory;
import com.hydrogen.mqtt.connector.nettyserver.NettyConfig;
import com.hydrogen.mqtt.connector.nettyserver.ServerChannelHandlerAdapter;

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
	
	@Resource
	private NettyConfig nettyConfig;

	@Bean("TCPServer")
	public TCPServer getTCPServer(IoHandlerAdapter messageHandle) {
		AGVCar.intSpeed(steptime, speed);
		//热车
		AGVCar car = new AGVCar();
    	car.setId(0);
		House.addCar(car);
		car.init();
		car.start();
		
		
		MinaTCPServer server = new MinaTCPServer();
		server.setCodecFactory(protocolCodecFactory());
		server.setHandler(messageHandle);
		server.setPort(port);
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		nettyConfig.setPort(port);
		NettyServer server2 = new NettyServer();
		server2.setNettyConfig(nettyConfig);
		server2.setChannelHandlerAdapter(serverChannelHandlerAdapter());
		server2.start();
		*/
		return server;
	}
	
	@Bean
	public ServerChannelHandlerAdapter serverChannelHandlerAdapter() {
		ServerChannelHandlerAdapter handler = new ServerChannelHandlerAdapter();
		handler.register(msgHandlerFactory());
		return handler;
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
