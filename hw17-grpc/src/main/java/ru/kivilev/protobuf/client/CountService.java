package ru.kivilev.protobuf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CountService {
    private static final int FROM_LOOP = 1;
    private static final int TO_LOOP = 60;

    private int currentValue = 0;
    private int currentLoopCount = FROM_LOOP;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final NumberResponseStreamObserver observer;
    private static final Logger logger = LoggerFactory.getLogger(CountService.class);


    public CountService(NumberResponseStreamObserver observer) {
        this.observer = observer;
    }

    public void runCycle() {
        Runnable scheduledTask = () -> {
            currentValue += 1 + observer.getAndSetNumberValueFromServer(0);
            logger.info("Current value: {}", currentValue);
            currentLoopCount++;
            if (currentLoopCount >= TO_LOOP) {
                scheduledExecutorService.shutdown();
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(scheduledTask, 0, 1, TimeUnit.SECONDS);
    }
}
