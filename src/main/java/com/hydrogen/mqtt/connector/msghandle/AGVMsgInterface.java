package com.hydrogen.mqtt.connector.msghandle;


public interface AGVMsgInterface {
	
	public byte[] encoder();
	
	public void decoder(byte[] msgstr);
	
	public int msgCmd();
	
	public int agvid();
	
}
