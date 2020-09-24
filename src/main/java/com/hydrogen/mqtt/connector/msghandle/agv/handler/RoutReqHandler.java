package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import com.hydrogen.mqtt.connector.car.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg.CMD;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVRouteReqMsg;

public class RoutReqHandler  extends  AGVMsgHandler<AGVRouteReqMsg>{
	
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