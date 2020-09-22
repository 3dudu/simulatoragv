package com.hydrogen.mqtt.connector.msghandle.agv.msg;

public class AGVTaskMsg extends AGVBaseMsg{

	public AGVTaskMsg(int seq) {
		super(seq);
	}

	int taskstatus;
	
	public int getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(int taskstatus) {
		this.taskstatus = taskstatus;
	}

	@Override
	public byte[] encoder() {
		return null;
	}

	@Override
	public void decoder(byte[] msgstr) {
		taskstatus = msgstr[0] & 0xFF;
	}

	@Override
	public CMD msgCmd() {
		return CMD.CMD_DF;
	}


}
