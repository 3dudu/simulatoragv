package com.hydrogen.mqtt.connector.msghandle;

import org.apache.mina.core.session.IoSession;

public interface AGVMsgHandlerInterface{
	public AGVMsgInterface process(AGVMsgInterface taskMsg, IoSession session);
	abstract public int getHandlerId();
}
