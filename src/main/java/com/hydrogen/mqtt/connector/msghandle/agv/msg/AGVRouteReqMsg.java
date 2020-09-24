package com.hydrogen.mqtt.connector.msghandle.agv.msg;

import java.util.ArrayList;
import java.util.List;

import com.hydrogen.mqtt.connector.car.StationPoint;

public class AGVRouteReqMsg  extends AGVBaseMsg{
	public AGVRouteReqMsg(int seq) {
		super(seq);
	}

	private List<StationPoint> routeList;


	public List<StationPoint> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<StationPoint> routeList) {
		this.routeList = routeList;
	}

	@Override
	public byte[] encoder() {
		return null;
	}

	@Override
	public void decoder(byte[] msgstr) {
		routeList = new ArrayList<StationPoint>();
		for(int i=0;i<msgstr.length;i++) {
			StationPoint point = new StationPoint();
			point.setRuntype(msgstr[i]&0xFF);
			
			i++;
			byte[] byte_x = new byte[4];
			System.arraycopy(msgstr, i, byte_x, 0, 4);
			int x = AGVBaseMsg.byteArrToInt(byte_x);
			point.setX(x);
			
			i += 4;
			byte[] byte_y = new byte[4];
			System.arraycopy(msgstr, i, byte_y, 0, 4);
			int y = AGVBaseMsg.byteArrToInt(byte_y);
			point.setY(y);
			
			
			i += 4;
			byte[] byte_w = new byte[4];
			System.arraycopy(msgstr, i, byte_w, 0, 4);
			int w = AGVBaseMsg.byteArrToInt(byte_w);
			point.setW(w);
			
			i += 4;
			point.setSpeed(msgstr[i]&0xFF);
			
			i++;
			point.setAction(msgstr[i]&0xFF);
			routeList.add(point);
		}
	}

	@Override
	public int msgCmd() {
		return CMD.CMD_DE.getCmd();
	}

}
