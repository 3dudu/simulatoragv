package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg.CMD;

import org.springframework.core.task.TaskExecutor;

import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.QueryAGVInfoMsg;

public class QueryAGVInfoMsgHandler extends  AGVMsgHandler<QueryAGVInfoMsg>{

	public QueryAGVInfoMsgHandler(TaskExecutor taskExecutor) {
		super(taskExecutor);
	}

	@Override
	public AGVInfoRspMsg handler(QueryAGVInfoMsg italkmsg,AGVCar car) {
		return null;
	}

	@Override
	public int getHandlerId() {
		return CMD.CMD_DD.getCmd();
	}

}
