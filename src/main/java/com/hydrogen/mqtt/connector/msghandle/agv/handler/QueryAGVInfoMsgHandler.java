package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg.CMD;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.QueryAGVInfoMsg;

public class QueryAGVInfoMsgHandler extends  AGVMsgHandler<QueryAGVInfoMsg>{
	@Override
	public AGVInfoRspMsg handler(QueryAGVInfoMsg italkmsg,AGVCar car) {
		return null;
	}

	@Override
	public int getHandlerId() {
		return CMD.CMD_DD.getCmd();
	}

}
