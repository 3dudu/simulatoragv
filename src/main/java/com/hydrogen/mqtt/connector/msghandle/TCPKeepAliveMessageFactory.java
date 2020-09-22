package com.hydrogen.mqtt.connector.msghandle;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPKeepAliveMessageFactory implements  KeepAliveMessageFactory{

    private final Logger LOG = LoggerFactory.getLogger(TCPKeepAliveMessageFactory.class);

    /** 心跳包内容 */  
    private static final String HEARTBEATREQUEST = "1111";  
    private static final String HEARTBEATRESPONSE = "1112"; 
    public Object getRequest(IoSession session) {
        LOG.warn("请求预设信息: " + HEARTBEATREQUEST);  
         return HEARTBEATREQUEST;
    }
    public Object getResponse(IoSession session, Object request) {
        LOG.warn("响应预设信息: " + HEARTBEATRESPONSE);  
        /** 返回预设语句 */  
        return HEARTBEATRESPONSE;  
    }
    public boolean isRequest(IoSession session, Object message) {
         if (message.equals(HEARTBEATREQUEST)) {
        	 LOG.warn("请求心跳包信息: " + message);  
        	 return true;  
         }
         return false;  
    }
    public boolean isResponse(IoSession session, Object message) {
      if(message.equals(HEARTBEATRESPONSE)) {
    	  LOG.warn("响应心跳包信息: " + message);  
    	  return true;
      }
        return false;
    }
}