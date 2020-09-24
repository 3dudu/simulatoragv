package com.hydrogen.mqtt.connector.car;

import java.util.List;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;

public interface Car {
	public void init();

	int getId();

	void close();
	
	public void changeTask(int taskStatus);
	
	public void start();
	
	public void addRoute(List<StationPoint> routeList);

	public void recMsg(AGVBaseMsg e);
	
	public Thread msgThread();
	
	public Thread powerThread();

	public Thread driverThread();

}
