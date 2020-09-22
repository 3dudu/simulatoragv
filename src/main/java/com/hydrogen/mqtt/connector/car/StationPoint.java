package com.hydrogen.mqtt.connector.car;

public class StationPoint {
	private int runtype;
	public int getRuntype() {
		return runtype;
	}
	public void setRuntype(int runtype) {
		this.runtype = runtype;
	}
	private int x;
	private int y;
	private int w;
	private int speed;
	private int action;
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
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
}
