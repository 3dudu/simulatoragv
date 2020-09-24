package com.hydrogen.mqtt.connector.msghandle.agv.handler;

import org.apache.mina.core.session.IoSession;

import com.hydrogen.mqtt.connector.car.House;
import com.hydrogen.mqtt.connector.msghandle.AGVMsgHandlerInterface;
import com.hydrogen.mqtt.connector.msghandle.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.agv.AGVCar;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVInfoRspMsg;

public abstract class AGVMsgHandler<T extends AGVBaseMsg> implements AGVMsgHandlerInterface{
	@SuppressWarnings("unchecked")
	@Override
	public AGVMsgInterface process(AGVMsgInterface taskMsg, IoSession session) {
		T msg = (T)taskMsg;
		AGVInfoRspMsg rsp = new AGVInfoRspMsg(msg.getMsgseq());
		AGVCar car = initCar(msg,session);
		//改为异步操作车子
		handler(msg,car);
		return getCarInfo(rsp,car);
	}
	
	public abstract AGVBaseMsg handler(T italkmsg,AGVCar car);

	
	public AGVCar initCar(AGVBaseMsg italkmsg,IoSession session) {
		int carid = italkmsg.agvid();
		if(carid!=0) {
			session.setAttribute("carid", carid);
		}
		AGVCar car = (AGVCar)House.getCar(carid);
		if(car!=null) {
			return car;
		}
		
		car = new AGVCar();
    	car.setId(carid);
		House.addCar(car);
		car.init();
		car.start();
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
