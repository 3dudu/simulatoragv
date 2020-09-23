package com.hydrogen.mqtt.connector.car;

import java.util.LinkedList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AGVCar {
    private final static Logger LOG = LoggerFactory.getLogger(AGVCar.class);

	private static final int SPEED_1000 = 200;
	private static final int SPEED = SPEED_1000/8;
	private LinkedList<StationPoint> routeList = new LinkedList<StationPoint>();
	private Thread worker;
	private Object lock = new Object();
	private int taskStatus;
	private int isClose = 0;;
	private IoSession iosession;
	private int id;
	private int status;
	public IoSession getIosession() {
		return iosession;
	}
	public void setIosession(IoSession iosession) {
		this.iosession = iosession;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private int taskComplete;
	private int taskRedo;
	private int x;
	private int y;
	public LinkedList<StationPoint> getRouteList() {
		return routeList;
	}
	public void setRouteList(LinkedList<StationPoint> routeList) {
		this.routeList = routeList;
	}
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
	private int w;
	private int onTop;
	private int flip;
	private int hand;
	private int power;
	private int speed;
	private int alarmLength;
	public void init() {
		setStatus(0x00);
		setTaskComplete(0x00);
		setTaskRedo(0x00);
		setX(1200);
		setY(1200);
		setW(2);
		setOnTop(0x01);
		setFlip(0x01);
		setHand(0x01);
		setPower(98);
		setSpeed(0x01);
		setAlarmLength(0x00);	
		taskStatus=0x04;

		worker = new CarWorkThread("Car-"+id);	
		worker.start();
	}
	
	public void addRoute(List<StationPoint> routeList) {
		synchronized (lock) {
			setStatus(0x01);
			this.routeList.clear();
			this.routeList.addAll(routeList);
			LOG.info("car_"+id+",recive new routelist!");
			lock.notify();
		}
	}
	
	public void start() {
		synchronized (lock) {
			setStatus(0x01);
			//testdata();
			lock.notify();
		}
	}
	
	public void close() {
		synchronized (lock) {
			isClose = 1;
			routeList.clear();
			LOG.info("car_"+id+",stop task!");
			lock.notify();
		}
		worker.interrupt();
	}
	
	public void changeTask(int taskStatus) {
		if(taskStatus==0x03) {
			this.taskStatus = taskStatus;
			setStatus(0x03);
			LOG.info("car_"+id+",pause task!");
		}else if(taskStatus==0x04) {
			synchronized (lock) {
				if(this.taskStatus==0x03) {
					this.taskStatus = taskStatus;
					setStatus(0x01);
					LOG.info("car_"+id+",go on task!");
					lock.notify();
				}else{
					this.taskStatus = taskStatus;
					setStatus(0x01);
					routeList.clear();
					//testdata();
					lock.notify();
				}
			}
		}else if(taskStatus==0x05){
			this.taskStatus = taskStatus;
			setStatus(0x00);
			synchronized (lock) {
				routeList.clear();
				LOG.info("car_"+id+",stop task!");
				lock.notify();
			}
		}
		
	}
	
	public void testdata() {
		int x = this.id * 10000;
		int y =  this.id * 2000;
		for(int i=0;i<20;i++) {
			x += 1000;
			StationPoint e = new StationPoint();
			e.setX(x);
			e.setY(y);
			e.setW(2);
			e.setAction(0x02);
			e.setSpeed(0x02);
			e.setRuntype(0x02);
			routeList.add(e);
		}
		for(int i=0;i<20;i++) {
			y += 1000;
			StationPoint e = new StationPoint();
			e.setX(x);
			e.setY(y);
			e.setW(2);
			e.setAction(0x02);
			e.setSpeed(0x02);
			e.setRuntype(0x02);
			routeList.add(e);
		}
	}

	class CarWorkThread extends Thread{
	   public CarWorkThread(String name){    
	        super( name );
	    }
		@Override
		public void run() {
			while (isClose == 0) {
				synchronized (lock) {
					if (taskStatus == 3) {
						try {
							lock.wait();
							continue;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (routeList.size() == 0) {
						LOG.info("car_"+id+",routeList empty,stop task!");

						setStatus(0x00);
						try {
							lock.wait();
							continue;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					StationPoint curpoint = routeList.poll();
					setX(curpoint.getX());
					setY(curpoint.getY());
					setW(curpoint.getW());
					setSpeed(curpoint.getSpeed());

					if (routeList.size() > 0) {
						StationPoint nextpoint = routeList.peek();
						if ( Math.abs(curpoint.getX() - nextpoint.getX())<100) {
							int d_y = nextpoint.getY() - curpoint.getY();
							d_y = Math.abs(d_y);
							if (d_y > SPEED*4) {
								while (d_y > SPEED*2) {
									if (nextpoint.getY() > curpoint.getY()) {
										setY(getY() + SPEED);
									} else {
										setY(getY() - SPEED);
									}
									d_y -= SPEED;
									try {
										Thread.sleep(SPEED_1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								continue;
							}

						} else if ( Math.abs(curpoint.getY() - nextpoint.getY())<100 ) {
							int d_x = nextpoint.getX() - curpoint.getX();
							d_x = Math.abs(d_x);
							if (d_x > SPEED*4) {
								while (d_x > SPEED*2) {
									if (nextpoint.getX() > curpoint.getX()) {
										setX(getX() + SPEED);
									} else {
										setX(getX() - SPEED);
									}
									d_x -= SPEED;
									try {
										Thread.sleep(SPEED_1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								continue;
							}
						}
					}
				}
				try {
					Thread.sleep(SPEED_1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
