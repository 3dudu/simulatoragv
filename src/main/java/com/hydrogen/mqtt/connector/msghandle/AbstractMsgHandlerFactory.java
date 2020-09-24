package com.hydrogen.mqtt.connector.msghandle;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMsgHandlerFactory {
    private final Logger LOG = LoggerFactory.getLogger(AbstractMsgHandlerFactory.class);

	private static Map<Integer,AGVMsgHandlerInterface> handlerMap = new HashMap<Integer,AGVMsgHandlerInterface>();
	
	public AGVMsgHandlerInterface getHandler(int msgId) {
		if(handlerMap.containsKey(msgId)) {
			LOG.debug("No fonud handler with id:"+ msgId +", return default AGVMsgHandler.");
			return handlerMap.get(msgId);
		}else {
			return defaultHandler();
		}
	}
	public AGVMsgHandlerInterface putHandler(AGVMsgHandlerInterface handler) {
		return handlerMap.put(handler.getHandlerId(), handler);
	}
	public abstract void regHandler();
	public abstract AGVMsgHandlerInterface defaultHandler();
}
