package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.apache.mina.core.session.IoSession;

import com.hydrogen.mqtt.connector.car.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVRouteReqMsg;

public class RoutReqHandler  extends  AGVMsgHandler<AGVRouteReqMsg>{
	
	@Override
	public AGVInfoRspMsg handler(AGVRouteReqMsg routeReqMsg, IoSession session) {
		AGVCar car = initCar(routeReqMsg,session);
		AGVInfoRspMsg rsp = new AGVInfoRspMsg(routeReqMsg.getMsgseq());

		car.addRoute(routeReqMsg.getRouteList());
		return getCarInfo(rsp,car);
	}
	
	@Override
	public int getHandlerId() {
		return AGVMsgInterface.CMD.CMD_DE.getCmd();
	}

}