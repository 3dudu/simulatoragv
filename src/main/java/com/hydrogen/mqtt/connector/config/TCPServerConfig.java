package com.hydrogen.mqtt.connector.config;

import java.io.IOException;

import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hydrogen.mqtt.connector.msghandle.TCPKeepAliveMessageFactory;
import com.hydrogen.mqtt.connector.msghandle.TCPServer;
import com.hydrogen.mqtt.connector.msghandle.agv.handler.AGVMsgHandlerContext;

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
	private String[] white ;
	
	@Bean("TCPServer")
	public TCPServer getTCPServer(KeepAliveFilter keepAliveFilter) {
		AGVMsgHandlerContext.init();
		TCPServer server = new TCPServer();
		server.setPort(port);
		server.setWhite(white);
		server.setKeepAliveFilter(keepAliveFilter);
		
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return server;
	}
	
	@Bean
	public KeepAliveFilter keepAliveFilter() {
		KeepAliveFilter kl = new KeepAliveFilter(new TCPKeepAliveMessageFactory(),org.apache.mina.core.session.IdleStatus.BOTH_IDLE);
		return kl;
	}
	
}
