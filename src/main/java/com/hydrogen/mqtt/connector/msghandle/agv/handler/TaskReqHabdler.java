package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.apache.mina.core.session.IoSession;

import com.hydrogen.mqtt.connector.car.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVTaskMsg;

public class TaskReqHabdler extends  AGVMsgHandler<AGVTaskMsg>{
	
	@Override
	public AGVInfoRspMsg handler(AGVTaskMsg taskMsg, IoSession session) {
		AGVInfoRspMsg rsp = new AGVInfoRspMsg(taskMsg.getMsgseq());

		AGVCar car = initCar(taskMsg,session);
		car.changeTask(taskMsg.getTaskstatus());
		return getCarInfo(rsp,car);
	}
	
	@Override
	public int getHandlerId() {
		return AGVMsgInterface.CMD.CMD_DF.getCmd();
	}
}