package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg.CMD;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVTaskMsg;

public class TaskReqHabdler extends  AGVMsgHandler<AGVTaskMsg>{
	@Override
	public AGVInfoRspMsg handler(AGVTaskMsg taskMsg,  AGVCar car) {
		car.recMsg(taskMsg);
		return null;
	}
	
	@Override
	public int getHandlerId() {
		return CMD.CMD_DF.getCmd();
	}
}