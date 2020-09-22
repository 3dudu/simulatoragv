package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.apache.mina.core.session.IoSession;

import com.hydrogen.mqtt.connector.car.AGVCar;
import com.hydrogen.mqtt.connector.car.House;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface;

public abstract class AGVMsgHandler<T extends AGVMsgInterface> {
	
	abstract public AGVMsgInterface handler(T italkmsg,IoSession session);

	
	public AGVCar initCar(T italkmsg,IoSession session) {
		if(null!=session.getAttribute("carid")) {
			int carid = (int)session.getAttribute("carid");
			AGVCar car = House.getCar(carid);
			if(car!=null) {
				return car;
			}
		}
		
		AGVCar car = new AGVCar();
		House.addCar(car);
		car.init();
		car.start();
		session.setAttribute("carid", car.getId());
		return car;
	}

	abstract public int getHandlerId();

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
