package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.apache.mina.core.session.IoSession;

import com.hydrogen.mqtt.connector.car.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.QueryAGVInfoMsg;

public class QueryAGVInfoMsgHandler extends  AGVMsgHandler<QueryAGVInfoMsg>{

	@Override
	public AGVMsgInterface handler(QueryAGVInfoMsg italkmsg,IoSession session) {
		AGVCar car = initCar(italkmsg,session);
		AGVInfoRspMsg rsp = new AGVInfoRspMsg(italkmsg.getMsgseq());

		return getCarInfo(rsp,car);
	}

	@Override
	public int getHandlerId() {
		return AGVMsgInterface.CMD.CMD_DD.getCmd();
	}

}
