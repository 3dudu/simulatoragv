package com.hydrogen.mqtt.connector.msghandle.agv;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.car.Car;
import com.hydrogen.mqtt.connector.car.StationPoint;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVRouteReqMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVTaskMsg;

public class AGVCar implements Car {
	private final static Logger LOG = LoggerFactory.getLogger(AGVCar.class);

	private static int STEP_TIMEOUT = 200;
	private static int STEP_SPEED = STEP_TIMEOUT / 2; // mm
	private ConcurrentLinkedQueue<StationPoint> routeList = new ConcurrentLinkedQueue<StationPoint>();
	private ConcurrentLinkedQueue<AGVBaseMsg> msgList = new ConcurrentLinkedQueue<AGVBaseMsg>();
	private Thread msgWorker;
	private Thread driver;
	private Thread powerWorker;
	
	private Object lock = new Object();
	private int taskStatus;
	private int isClose = 0;;
	private IoSession iosession;
	private int id;
	private int status;
	private int driverLength=0;
	public int getDriverLength() {
		return driverLength;
	}

	public void setDriverLength(int driverLength) {
		this.driverLength = driverLength;
	}

	private int isCharge = 0;
	
	public int getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(int isCharge) {
		this.isCharge = isCharge;
	}

	public static void intSpeed(int step, int speed) {
		STEP_TIMEOUT = step;
		STEP_SPEED = speed / (1000 / step);
	}

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

	public ConcurrentLinkedQueue<StationPoint> getRouteList() {
		return routeList;
	}

	public void setRouteList(ConcurrentLinkedQueue<StationPoint> routeList) {
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
		this.isClose = 1;
		this.taskStatus = 0x04;
	}

	public void recMsg(AGVBaseMsg e) {
		msgList.add(e);
	}
	
	public void addRoute(List<StationPoint> routeList) {
		setStatus(0x01);
		setIsCharge(0);
		this.taskStatus = 0x04;
		LOG.info("car_" + id + ",recive new routelist!");
		this.routeList.clear();
		this.routeList.addAll(routeList);
		synchronized (lock) {
			lock.notify();
		}
	}

	public void start() {
		if(isClose==1) {
			isClose = 0;
			powerThread();
			msgThread();
			driverThread();
		}
		setStatus(0x01);
		this.taskStatus = 0x04;
		synchronized (lock) {
			lock.notify();
		}
	}

	public void close() {
		isClose = 1;
		LOG.info("car_" + id + ",stop task!");
		routeList.clear();
		synchronized (lock) {
			lock.notify();
		}
		driver.interrupt();
		msgWorker.interrupt();
		powerWorker.interrupt();
	}

	public void changeTask(int taskStatus) {
		if (taskStatus == 0x03) {
			setIsCharge(0);
			this.taskStatus = taskStatus;
			setStatus(0x03);
			LOG.info("car_" + id + ",pause task!");
		} else if (taskStatus == 0x04) {
			setIsCharge(0);
			if (this.taskStatus == 0x03) {
				this.taskStatus = taskStatus;
				setStatus(0x01);
				LOG.info("car_" + id + ",go on task!");
				synchronized (lock) {
					lock.notify();
				}
			} else {
				this.taskStatus = taskStatus;
				setStatus(0x01);
				routeList.clear();
				synchronized (lock) {
					lock.notify();
				}
			}
		} else if (taskStatus == 0x05) {
			setIsCharge(0);
			this.taskStatus = taskStatus;
			setStatus(0x00);
			LOG.info("car_" + id + ",stop task!");
			routeList.clear();
			synchronized (lock) {
				lock.notify();
			}
		}
	}

	public void testdata() {
		int x = this.id * 10000;
		int y = this.id * 2000;
		for (int i = 0; i < 20; i++) {
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
		for (int i = 0; i < 20; i++) {
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
	
	
	class PowerThread extends Thread {
		public PowerThread(String name) {
			super(name);
		}
		@Override
		public void run() {
			int driverLength = 0;
			int d_length = 0;
			int chargecount=0;
			int standcount=0;

			while (isClose == 0) {
				//耗电
				if(getIsCharge()==1) {
					int cporwer = getPower();
					if(cporwer<100) {
						chargecount++;
						if(chargecount>30) {
							chargecount = 0;
							cporwer = cporwer + 1;
							if(cporwer>100) {
								cporwer = 100;
							}
							setPower(cporwer);
						}
					}

				}else{
					//待机
					int nowLength = getDriverLength();
					if(driverLength==0 || driverLength>nowLength) {
						driverLength = getDriverLength();
					}
					if(nowLength>driverLength) {
						int _length = d_length + nowLength-driverLength;
						int _power = _length / 10000;
						d_length = _length % 1000;
						int cporwer = getPower();
						cporwer = cporwer - _power;
						if(cporwer<0) {
							cporwer = 0;
						}
						setPower(cporwer);
						driverLength = getDriverLength();
					}else {
						int cporwer = getPower();
						if(cporwer>0) {
							standcount++;
							if(standcount>100) {
								standcount = 0;
								cporwer = cporwer - 1;
								if(cporwer<0) {
									cporwer = 0;
								}
								setPower(cporwer);
							}
						}
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	class CarWorkThread extends Thread {
		public CarWorkThread(String name) {
			super(name);
		}
		@Override
		public void run() {
			while (isClose == 0) {
				if (msgList.isEmpty()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				AGVBaseMsg msg = msgList.poll();
				
				switch (msg.getCmd()) {
				
					case 0xDF:
						AGVTaskMsg taskmsg = (AGVTaskMsg)msg;
						changeTask(taskmsg.getTaskstatus());
						break;
						
					case 0xDE:
						AGVRouteReqMsg routeReqMsg = (AGVRouteReqMsg)msg;
						addRoute(routeReqMsg.getRouteList());
						break;
						
					default:
						break;
				}
				
			}
		}
	}
	

	class CarDriverThread extends Thread {
		public CarDriverThread(String name) {
			super(name);
		}
		
		private void move(StationPoint curpoint, StationPoint nextpoint) {
			int speed = STEP_SPEED;
			if (curpoint.getSpeed() == 0x02) {
				speed = STEP_SPEED * 7 / 10;
			}
			if (Math.abs(curpoint.getX() - nextpoint.getX()) < 100) {
				int d_y = nextpoint.getY() - curpoint.getY();
				d_y = Math.abs(d_y);
				while (d_y > speed) {
					if (taskStatus == 3 || taskStatus == 5) {
						try {
							synchronized (lock) {
								lock.wait();
							}
							if (taskStatus != 4) {
								break;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (nextpoint.getY() > curpoint.getY()) {
						setY(getY() + speed);
					} else {
						setY(getY() - speed);
					}
					driverLength += speed;
					d_y -= speed;
					try {
						Thread.sleep(STEP_TIMEOUT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				int d_x = nextpoint.getX() - curpoint.getX();
				d_x = Math.abs(d_x);
				while (d_x > speed) {
					if (taskStatus == 3 || taskStatus == 5) {
						try {
							synchronized (lock) {
								lock.wait();
							}
							if (taskStatus != 4) {
								break;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (nextpoint.getX() > curpoint.getX()) {
						setX(getX() + speed);
					} else {
						setX(getX() - speed);
					}
					d_x -= speed;
					driverLength += speed;
					try {
						Thread.sleep(STEP_TIMEOUT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}

		@Override
		public void run() {
			while (isClose == 0) {
				if (routeList.isEmpty()) {
					LOG.info("car_" + id + ",routeList empty,stop task!");
					setStatus(0x00);
					try {
						synchronized (lock) {
							lock.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				StationPoint curpoint = routeList.poll();
				LOG.info("car_" + id + ",move to curpoint:["+curpoint.getX()+","+curpoint.getY()+","+curpoint.getW()+"]");

				setX(curpoint.getX());
				setY(curpoint.getY());
				setW(curpoint.getW());
				setSpeed(curpoint.getSpeed());
				
				//处理动作
				action(curpoint);
				
				//行动
				if (!routeList.isEmpty()) {
					StationPoint nextpoint = routeList.peek();
					move(curpoint,nextpoint);
					try {
						Thread.sleep(STEP_TIMEOUT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void action(StationPoint curpoint) {
			if(curpoint.getAction()!=0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			switch(curpoint.getAction()) {
				case 0x01:
					setIsCharge(1);
					break;
				case 0x02:
					setFlip(0x01);
					break;
				case 0x03:
					setFlip(0x01);
					setHand(0x00);
					break;
				case 0x04:
					setFlip(0x01);
					setHand(0x01);
					break;
				case 0x05:
					setFlip(0x01);
					setHand(0x02);
					break;
				case 0x06:
					setFlip(0x01);
					setHand(0x03);
					break;
				case 0x07:
					setFlip(0x00);
					break;
				case 0x08:
					setFlip(0x00);
					setHand(0x00);
					break;
				case 0x09:
					setFlip(0x00);
					setHand(0x01);
					break;
				case 0x0A:
					setFlip(0x00);
					setHand(0x02);
					break;
				case 0x0B:
					setFlip(0x00);
					setHand(0x03);
					break;
				default:
					break;
			}
		}

	}


	@Override
	public Thread msgThread() {
		Thread msgWorker = new CarWorkThread("Car-msg-" + id);
		msgWorker.start();
		return msgWorker;
	}

	@Override
	public Thread powerThread() {
		Thread powerThread = new PowerThread("Car-power-" + id);
		powerThread.start();
		return powerThread;
	}

	@Override
	public Thread driverThread() {
		Thread driver = new CarDriverThread("Car-driver-" + id);
		driver.start();
		return driver;
	}
}
