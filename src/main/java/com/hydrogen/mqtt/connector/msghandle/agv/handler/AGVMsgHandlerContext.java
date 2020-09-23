package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import java.util.HashMap;
import java.util.Map;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface;

public class AGVMsgHandlerContext{

	private static Map<Integer,AGVMsgHandler<? extends AGVMsgInterface>> handlerMap = new HashMap<Integer,AGVMsgHandler<? extends AGVMsgInterface>>();
	
	public static AGVMsgHandler<? extends AGVMsgInterface> getHandler(int msgId) {
		return handlerMap.get(msgId);
	}
	public static AGVMsgHandler<? extends AGVMsgInterface> putHandler(AGVMsgHandler<? extends AGVMsgInterface> handler) {
		return handlerMap.put(handler.getHandlerId(), handler);
	}
	public static void init() {
		
		QueryAGVInfoMsgHandler queryAGVInfoMsgHandler = new QueryAGVInfoMsgHandler();
		handlerMap.put(queryAGVInfoMsgHandler.getHandlerId(), queryAGVInfoMsgHandler);
		
		RoutReqHandler routReqHandler = new RoutReqHandler();
		handlerMap.put(routReqHandler.getHandlerId(), routReqHandler);
		
		TaskReqHabdler taskReqHabdler = new TaskReqHabdler();
		handlerMap.put(taskReqHabdler.getHandlerId(), taskReqHabdler);
		
	}
}
