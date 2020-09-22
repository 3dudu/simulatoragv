package com.hydrogen.mqtt.connector.msghandle.agv.msg;

public class QueryAGVInfoMsg extends AGVBaseMsg{

	private byte onpos;
	
	public byte getOnpos() {
		return onpos;
	}

	public void setOnpos(byte onpos) {
		this.onpos = onpos;
	}

	public QueryAGVInfoMsg(int seq) {
		super(seq);
	}

	@Override
	public CMD msgCmd() {
		return CMD.CMD_DD;
	}

	@Override
	public byte[] encoder() {
		byte[] body = new byte[1];
		body[0] = this.onpos;
		return body;
	}

	@Override
	public void decoder(byte[] msgstr) {
		this.onpos = msgstr[0];
	}

}
