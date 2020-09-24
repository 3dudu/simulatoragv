package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.msghandle.AGVMsgHandlerInterface;
import com.hydrogen.mqtt.connector.msghandle.AbstractMsgHandlerFactory;
import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;

public class AGVMsgHandlerFactory extends AbstractMsgHandlerFactory{
    private final Logger LOG = LoggerFactory.getLogger(AGVMsgHandlerFactory.class);
	
	public static AGVMsgHandler<AGVBaseMsg> defaultHandler;

	@Override
	public void regHandler() {
		QueryAGVInfoMsgHandler queryAGVInfoMsgHandler = new QueryAGVInfoMsgHandler();
		putHandler(queryAGVInfoMsgHandler);
		
		RoutReqHandler routReqHandler = new RoutReqHandler();
		putHandler(routReqHandler);
		
		TaskReqHabdler taskReqHabdler = new TaskReqHabdler();
		putHandler(taskReqHabdler);		
	}

	@Override
	public AGVMsgHandlerInterface defaultHandler() {
		if(defaultHandler==null) {
			defaultHandler = new AGVMsgHandler<AGVBaseMsg>() {
				@Override
				public int getHandlerId() {
					return 0;
				}

				@Override
				public AGVBaseMsg handler(AGVBaseMsg italkmsg,
						AGVCar car) {
					return null;
				}
			};
			LOG.info("No fonud handler, return default AGVMsgHandler.");
		}
		return defaultHandler;
	}
}
