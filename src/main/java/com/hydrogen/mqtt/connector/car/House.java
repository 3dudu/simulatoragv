package com.hydrogen.mqtt.connector.car;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class House {
    private final static Logger LOG = LoggerFactory.getLogger(House.class);

	private static AtomicInteger caridAtomic = new AtomicInteger();
	
    public static ConcurrentHashMap<Integer, Car> carmap = new ConcurrentHashMap<Integer, Car>();

    public static int addCar(Car car) {
    	int carid = car.getId();
    	LOG.info("car :"+carid+" work!");
    	carmap.put(carid, car);
    	return carid;
    }
    
    public static void removeCar(int carid) {
    	Car car = carmap.remove(carid);
    	caridAtomic.getAndDecrement();
    	LOG.info("car :"+carid+" stop!");
    	car.close();
    }

	public static Car getCar(int carid) {
		return carmap.get(carid);
	}
    
}
