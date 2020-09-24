package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.apache.mina.core.session.IoSession;

import com.hydrogen.mqtt.connector.car.AGVCar;
import com.hydrogen.mqtt.connector.car.House;
import com.hydrogen.mqtt.connector.msghandle.AGVMsgHandlerInterface;
import com.hydrogen.mqtt.connector.msghandle.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;

public abstract class AGVMsgHandler<T extends AGVBaseMsg> implements AGVMsgHandlerInterface{
	
	@Override
	public AGVMsgInterface process(AGVMsgInterface taskMsg, IoSession session) {
		T msg = (T)taskMsg;
		AGVInfoRspMsg rsp = new AGVInfoRspMsg(msg.getMsgseq());
		AGVCar car = initCar(msg,session);
		
		AGVBaseMsg rsp2 = handler(msg,car);
		if(rsp2==null) {
			return getCarInfo(rsp,car);
		}else {
			return rsp2;
		}
	}
	
	public abstract AGVBaseMsg handler(T italkmsg,AGVCar car);

	
	public AGVCar initCar(AGVBaseMsg italkmsg,IoSession session) {
		int carid = italkmsg.agvid();
		if(carid!=0) {
			session.setAttribute("carid", carid);
		}
		AGVCar car = House.getCar(carid);
		if(car!=null) {
			return car;
		}
		
		car = new AGVCar();
    	car.setId(carid);
		House.addCar(car);
		car.init();
		return car;
	}

	public AGVInfoRspMsg getCarInfo(AGVInfoRspMsg rsp, AGVCar car) {
		rsp.setStatus(car.getStatus());
		rsp.setTaskComplete(car.getTaskComplete());
		rsp.setTaskRedo(car.getTaskRedo());
		rsp.setX(car.getX());
		rsp.setY(car.getY());
		rsp.setW(car.getW());
		rsp.setOnTop(car.getOnTop());
		rsp.setFlip(car.getFlip());
		rsp.setHand(car.getHand());
		rsp.setPower(car.getPower());
		rsp.setSpeed(car.getSpeed());
		rsp.setAlarmLength(car.getAlarmLength());
		return rsp;
	}
}
