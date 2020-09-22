package com.hydrogen.mqtt.connector.msghandle.agv.msg;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface.CMD;

public class AGVMessageFactory {

	private static AGVMessageFactory instance = new AGVMessageFactory();
	
	public static AGVMessageFactory getInstance() {
		return instance;
	}

	public AGVMsgInterface buildMsg(int cmd,int seq,byte[] body) {
		
		AGVMsgInterface msg = null;

		if(CMD.CMD_DD.getCmd()==cmd){
			msg = new QueryAGVInfoMsg(seq);
		}else if(CMD.CMD_DF.getCmd()==cmd){
			msg = new AGVTaskMsg(seq);
		}else if(CMD.CMD_DE.getCmd()==cmd){
			msg = new AGVRouteReqMsg(seq);
		}
		if(msg!=null && body!=null){
			msg.decoder(body);
		}
		
		return msg;
	}

}
