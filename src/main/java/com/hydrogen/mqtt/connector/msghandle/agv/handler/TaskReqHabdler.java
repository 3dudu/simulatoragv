package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg.CMD;

import org.springframework.core.task.TaskExecutor;

import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVTaskMsg;

public class TaskReqHabdler extends  AGVMsgHandler<AGVTaskMsg>{
	
	public TaskReqHabdler(TaskExecutor taskExecutor) {
		super(taskExecutor);
	}

	@Override
	public AGVInfoRspMsg handler(AGVTaskMsg taskMsg,  AGVCar car) {
		car.changeTask(taskMsg.getTaskstatus());
		return null;
	}
	
	@Override
	public int getHandlerId() {
		return CMD.CMD_DF.getCmd();
	}
}