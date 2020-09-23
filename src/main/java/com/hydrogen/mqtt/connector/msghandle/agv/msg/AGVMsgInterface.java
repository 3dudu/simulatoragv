package com.hydrogen.mqtt.connector.msghandle.agv.msg;


public interface AGVMsgInterface {
	
	public byte[] encoder();
	
	public void decoder(byte[] msgstr);
	
	public CMD msgCmd();
	
	public int agvid();
	
	public enum CMD{
		CMD_DD(0xDD),
		CMD_D1(0xD1),
		CMD_DF(0xDF),
		CMD_DE(0xDE);
		
		private int cmdcode;
		CMD(int cmd){
            this.cmdcode = cmd;
        }

		public int getCmd() {
			return cmdcode;
		}
		
	}
	
}
