package com.hydrogen.mqtt.connector.config;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
		return new AGVMsgHandlerFactory(taskExecutor());
	}

	@Bean("messageHandle")
	public IoHandlerAdapter messageHandle(AbstractMsgHandlerFactory msgHandlerFactory) {
		AGVMessageHandle handler = new AGVMessageHandle(idletime, white);
		handler.register(msgHandlerFactory);
		return handler;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(5);
		// 设置最大线程数
		executor.setMaxPoolSize(10);
		// 设置队列容量
		executor.setQueueCapacity(20);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("car-driver-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}
}
