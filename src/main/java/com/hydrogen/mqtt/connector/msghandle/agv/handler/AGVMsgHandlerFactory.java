package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.hydrogen.mqtt.connector.msghandle.AGVMsgHandlerInterface;
import com.hydrogen.mqtt.connector.msghandle.AbstractMsgHandlerFactory;
import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;

public class AGVMsgHandlerFactory extends AbstractMsgHandlerFactory{
    private final Logger LOG = LoggerFactory.getLogger(AGVMsgHandlerFactory.class);
	
	public static AGVMsgHandler<AGVBaseMsg> defaultHandler;

	private TaskExecutor taskExecutor;
	
	public AGVMsgHandlerFactory(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void regHandler() {
		QueryAGVInfoMsgHandler queryAGVInfoMsgHandler = new QueryAGVInfoMsgHandler(taskExecutor);
		putHandler(queryAGVInfoMsgHandler);
		
		RoutReqHandler routReqHandler = new RoutReqHandler(taskExecutor);
		putHandler(routReqHandler);
		
		TaskReqHabdler taskReqHabdler = new TaskReqHabdler(taskExecutor);
		putHandler(taskReqHabdler);		
	}

	@Override
	public AGVMsgHandlerInterface defaultHandler() {
		if(defaultHandler==null) {
			defaultHandler = new AGVMsgHandler<AGVBaseMsg>(taskExecutor) {
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
