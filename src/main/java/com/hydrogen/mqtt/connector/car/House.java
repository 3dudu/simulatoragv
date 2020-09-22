package com.hydrogen.mqtt.connector.car;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class House {
	private static AtomicInteger caridAtomic = new AtomicInteger();
	
    public static ConcurrentHashMap<Integer, AGVCar> carmap = new ConcurrentHashMap<Integer, AGVCar>();

    public static int addCar(AGVCar car) {
    	int carid = car.getId();
    	System.out.println("car :"+carid+" work!");
    	carmap.put(carid, car);
    	return carid;
    }
    
    public static void removeCar(int carid) {
    	AGVCar car = carmap.remove(carid);
    	caridAtomic.getAndDecrement();
    	System.out.println("car :"+carid+" stop!");
    	car.close();
    }

	public static AGVCar getCar(int carid) {
		return carmap.get(carid);
	}
    
}
