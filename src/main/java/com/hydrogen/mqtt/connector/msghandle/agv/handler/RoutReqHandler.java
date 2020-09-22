package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.apache.mina.core.session.IoSession;

import com.hydrogen.mqtt.connector.car.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVRouteReqMsg;

public class RoutReqHandler  extends  AGVMsgHandler<AGVRouteReqMsg>{
	
	@Override
	public AGVRouteReqMsg handler(AGVRouteReqMsg routeReqMsg, IoSession session) {
		AGVCar car = initCar(routeReqMsg,session);

		car.addRoute(routeReqMsg.getRouteList());
		return null;
	}
	
	@Override
	public int getHandlerId() {
		return AGVMsgInterface.CMD.CMD_DE.getCmd();
	}

}