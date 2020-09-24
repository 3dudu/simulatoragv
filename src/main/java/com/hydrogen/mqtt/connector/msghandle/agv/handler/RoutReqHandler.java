package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg.CMD;

import org.springframework.core.task.TaskExecutor;

import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVRouteReqMsg;

public class RoutReqHandler  extends  AGVMsgHandler<AGVRouteReqMsg>{
	
	public RoutReqHandler(TaskExecutor taskExecutor) {
		super(taskExecutor);
	}

	@Override
	public AGVInfoRspMsg handler(AGVRouteReqMsg routeReqMsg, AGVCar car) {
		car.addRoute(routeReqMsg.getRouteList());
		return null;
	}
	
	@Override
	public int getHandlerId() {
		return CMD.CMD_DE.getCmd();
	}

}