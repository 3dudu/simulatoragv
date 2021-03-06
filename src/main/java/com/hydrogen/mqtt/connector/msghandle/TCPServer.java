package com.hydrogen.mqtt.connector.msghandle;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class TCPServer {
	private static int THREAD_NUM = Runtime.getRuntime().availableProcessors()+1;
	private int processorThreads = THREAD_NUM;
	private int codecThreads = THREAD_NUM;
	private boolean heapBuffer = true;
	private int port = 15151;
	private int timeout = 30;

	public int getProcessorThreads() {
		return processorThreads;
	}

	public void setProcessorThreads(int processorThreads) {
		if(processorThreads>0) {
			this.processorThreads = processorThreads;
		}
	}

	public int getCodecThreads() {
		return codecThreads;
	}

	public void setCodecThreads(int codecThreads) {
		if(codecThreads>0) {
			this.codecThreads = codecThreads;
		}
	}

	public boolean isHeapBuffer() {
		return heapBuffer;
	}

	public void setHeapBuffer(boolean heapBuffer) {
		this.heapBuffer = heapBuffer;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if(port>0) {
			this.port = port;
		}
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		if(timeout>0) {
			this.timeout = timeout;
		}
	}


	private KeepAliveFilter keepAliveFilter;
	
	public KeepAliveFilter getKeepAliveFilter() {
		return keepAliveFilter;
	}

	public void setKeepAliveFilter(KeepAliveFilter keepAliveFilter) {
		this.keepAliveFilter = keepAliveFilter;
	}

	public void start(ProtocolCodecFactory codecFactory,IoHandlerAdapter handler) throws IOException{
        //是否使用直接缓冲区，比直接缓存区快50%。待测试验证
        if (heapBuffer) {
            IoBuffer.setUseDirectBuffer(false);
            IoBuffer.setAllocator(new SimpleBufferAllocator());
        }
        if(codecFactory==null) {
        	codecFactory = new TextLineCodecFactory();
        }
        IoProcessor<NioSession> processor = new SimpleIoProcessorPool<NioSession>(NioProcessor.class,processorThreads);
        IoAcceptor acceptor = new NioSocketAcceptor(processor);

        acceptor.getFilterChain().addLast("logger",new LoggingFilter());
		acceptor.getFilterChain().addAfter("logger","codec",new ProtocolCodecFilter(codecFactory));
		// 线程池
		acceptor.getFilterChain().addAfter("codec","threadPool", createExecutorFilter(codecThreads,"AGV TCP Server"));
		if(keepAliveFilter!=null) {
			acceptor.getFilterChain().addAfter("threadPool","keepAliveFilter", keepAliveFilter);
		}
		acceptor.setHandler(handler);
		acceptor.getSessionConfig().setReadBufferSize(1024);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, timeout);
		acceptor.bind(new InetSocketAddress(port));
	}
	
    private ExecutorFilter createExecutorFilter(int eventThreads, final String threadName) {
        Executor executor = 
       	        new OrderedThreadPoolExecutor(eventThreads, eventThreads, 60, TimeUnit.SECONDS, new
       	        		RenamedThreadFactor(threadName, Executors.defaultThreadFactory()));
       ExecutorFilter executorFilter = new ExecutorFilter(executor);
       // 重命名线程
       return executorFilter;
   }
}
