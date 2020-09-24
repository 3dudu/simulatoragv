package com.hydrogen.mqtt.connector.msghandle;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.car.House;

public class AGVMessageHandle extends IoHandlerAdapter {
    private final Logger LOG = LoggerFactory.getLogger(AGVMessageHandle.class);
    
    private AbstractMsgHandlerFactory msgHandlerFactory;

	private int idletime;
	
	
	private String[] white ;

	public AGVMessageHandle(int idletime , String[] white) {
		super();
		this.idletime = idletime;
		this.white = white;
	}

	int getMaxIdleTime() {
		return idletime;
	}

	@Override
	public void messageSent(IoSession session, Object message)
			throws Exception {
		if(message instanceof AGVMsgInterface) {
			AGVMsgInterface msg = (AGVMsgInterface)message;
			LOG.debug("message send to "+session.getAttribute("ip")+",msg:"+msg.msgCmd());
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if(null!=session.getAttribute("carid")) {
			House.removeCar((int)session.getAttribute("carid"));
		}
		super.sessionClosed(session);
		LOG.info("session has been closed...");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		LOG.info("session has been created!!!");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		LOG.warn("session idle, so disconnecting......");
		session.closeOnFlush();
        LOG.warn("disconnected.");
	}
	
	private boolean checkIp(String ip) {
		if(null!=white && white.length>0) {
			for(String whiteip : white) {
				if(whiteip.equals(ip)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		String remoteAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		LOG.info("session has opened...,IP:"+remoteAddress);
		if(!checkIp(remoteAddress)) {
			session.closeNow();
		}
		int idleTime = getMaxIdleTime() / 2;
		if (idleTime > 0) {
			session.getConfig().setIdleTime(IdleStatus.READER_IDLE,
					idleTime);
		}
	}

	@Override
	public void messageReceived(IoSession session, Object obj)
			throws Exception {
		String remoteAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
	    session.setAttribute("ip", remoteAddress);
		if(obj != null && obj instanceof AGVMsgInterface) {
			AGVMsgInterface msg = (AGVMsgInterface) obj;
			LOG.debug("rec from "+session.getAttribute("ip")+",msg:"+msg.msgCmd());
			AGVMsgInterface message = processPacket(msg,session);
			if(message!=null) {
				session.write(message);
			}
		}
	}
	
	public AGVMsgInterface processPacket(final AGVMsgInterface packet,IoSession session) {
		//处理消息
		//否则，交给消息Handler去处理
		int msgId = packet.msgCmd();
		AGVMsgHandlerInterface handler = msgHandlerFactory.getHandler(msgId);
		if(null == handler){
			LOG.error("未知消息，没有注册消息处理器");
			return null;
		} else {
			//直接交给消息handler去处理
			return handler.process(packet,session);
		}
	}
	

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		LOG.info("其他方法抛出异常",cause);
	}

	public void register(AbstractMsgHandlerFactory msgHandlerFactory) {
		this.msgHandlerFactory = msgHandlerFactory;
		this.msgHandlerFactory.regHandler();
	}

}
