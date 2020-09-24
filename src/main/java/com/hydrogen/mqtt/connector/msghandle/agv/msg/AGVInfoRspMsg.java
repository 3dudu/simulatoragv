package com.hydrogen.mqtt.connector.msghandle.agv.msg;

public class AGVInfoRspMsg  extends AGVBaseMsg{

	public AGVInfoRspMsg(int seq) {
		super(seq);
	}

	@Override
	public byte[] encoder() {
		byte[] body = new byte[21];
		body[0] = Integer.valueOf(status).byteValue();
		body[1] = Integer.valueOf(taskComplete).byteValue();
		body[2] = Integer.valueOf(taskRedo).byteValue();
		byte[] xbyte = intToByteArray(x);
		System.arraycopy(xbyte, 0, body, 3, 4);
		byte[] ybyte = intToByteArray(y);
		System.arraycopy(ybyte, 0, body, 7, 4);
		byte[] wbyte = intToByteArray(w);
		System.arraycopy(wbyte, 0, body, 11, 4);
		body[15] = Integer.valueOf(onTop).byteValue();
		body[16] = Integer.valueOf(flip).byteValue();
		body[17] = Integer.valueOf(hand).byteValue();
		body[18] = Integer.valueOf(power).byteValue();
		body[19] = Integer.valueOf(speed).byteValue();
		body[20] = Integer.valueOf(alarmLength).byteValue();

		return body;
	}

	@Override
	public void decoder(byte[] msgstr) {
		
	}

	@Override
	public int msgCmd() {
		return CMD.CMD_D1.getCmd();
	}
	
	private int status;
	private int taskComplete;
	private int taskRedo;
	private int x;
	private int y;
	private int w;
	private int onTop;
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTaskComplete() {
		return taskComplete;
	}

	public void setTaskComplete(int taskComplete) {
		this.taskComplete = taskComplete;
	}

	public int getTaskRedo() {
		return taskRedo;
	}

	public void setTaskRedo(int taskRedo) {
		this.taskRedo = taskRedo;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getOnTop() {
		return onTop;
	}

	public void setOnTop(int onTop) {
		this.onTop = onTop;
	}

	public int getFlip() {
		return flip;
	}

	public void setFlip(int flip) {
		this.flip = flip;
	}

	public int getHand() {
		return hand;
	}

	public void setHand(int hand) {
		this.hand = hand;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getAlarmLength() {
		return alarmLength;
	}

	public void setAlarmLength(int alarmLength) {
		this.alarmLength = alarmLength;
	}

	public char[] getAlarm() {
		return alarm;
	}

	public void setAlarm(char[] alarm) {
		this.alarm = alarm;
	}

	private int flip;
	private int hand;
	private int power;
	private int speed;
	private int alarmLength;
	private char[] alarm;
}
