import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {

    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 10;

    private static final Object WORK_MONITOR = new Object();

    private static int currentPosition = 0;
    private static WorkMode workMode = WorkMode.GET_NEXT_COUNTER_VALUE;
    private static CountDirection countDirection = CountDirection.INCREMENT;

    public static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        Thread masterThread = new Thread(() -> {
            var currentThread = Thread.currentThread();
            currentThread.setName("MasterThread");

            while (!currentThread.isInterrupted()) {
                synchronized (WORK_MONITOR) {
                    if (countDirection == CountDirection.INCREMENT) {
                        currentPosition++;
                        if (currentPosition == MAX_RANGE) countDirection = CountDirection.DECREMENT;
                    } else if (countDirection == CountDirection.DECREMENT) {
                        currentPosition--;
                        if (currentPosition == MIN_RANGE) countDirection = CountDirection.INCREMENT;
                    }

                    logger.info("{}", currentPosition);

                    workMode = WorkMode.SHOW_CURRENT_COUNTER_VALUE;
                    WORK_MONITOR.notify();

                    try {
                        while (workMode != WorkMode.GET_NEXT_COUNTER_VALUE) {
                            WORK_MONITOR.wait();
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });

        Thread slaveThread = new Thread(() -> {
            var currentThread = Thread.currentThread();
            currentThread.setName("SlaveThread");

            while (!currentThread.isInterrupted()) {
                synchronized (WORK_MONITOR) {
                    while (workMode != WorkMode.SHOW_CURRENT_COUNTER_VALUE) {
                        try {
                            WORK_MONITOR.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                    logger.info("{}", currentPosition);

                    try {
                        Thread.sleep(1000);// чтоб успевали прочесть сообщения
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    workMode = WorkMode.GET_NEXT_COUNTER_VALUE;
                    WORK_MONITOR.notify();
                }
            }
        });

        masterThread.start();
        slaveThread.start();
    }
}

enum WorkMode {
    GET_NEXT_COUNTER_VALUE, SHOW_CURRENT_COUNTER_VALUE
}

enum CountDirection {
    INCREMENT, DECREMENT
}