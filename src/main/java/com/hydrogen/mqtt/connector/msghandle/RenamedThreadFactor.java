package com.hydrogen.mqtt.connector.msghandle;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class RenamedThreadFactor implements ThreadFactory {

    public RenamedThreadFactor() {
		super();
	}

    public RenamedThreadFactor(String namePrefix, ThreadFactory threadFactory) {
    	this.namePrefix = namePrefix;
    	this.threadFactory = threadFactory;
    }
	private final AtomicInteger threadId = new AtomicInteger(0);

    private String namePrefix;

    private ThreadFactory threadFactory;


    public Thread newThread(Runnable runnable) {
        Thread t = threadFactory.newThread(runnable);
        t.setName(namePrefix + " - " + threadId.incrementAndGet());
        return t;
    }

}