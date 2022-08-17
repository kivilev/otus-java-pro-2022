import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {

    public static void main(String[] args) throws InterruptedException {
        var calculationThread = new WorkThread(WorkMode.COUNT, "CalculationThread");
        var displayThread = new WorkThread(WorkMode.DISPLAY, "DisplayThread");

        calculationThread.start();
        displayThread.start();

        Thread.sleep(50000);

        calculationThread.interrupt();
        displayThread.interrupt();
    }
}

class WorkThread extends Thread {

    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 10;

    private static final Object WORK_MONITOR = new Object();

    private static int currentPosition = 0;
    private static CountDirection countDirection = CountDirection.INCREMENT;

    private static final Logger logger = LoggerFactory.getLogger(WorkThread.class);

    private final WorkMode threadWorkMode;
    private static WorkMode currentWorkMode = WorkMode.COUNT;// за счет этого происходит выбор какой поток будет запущен первым

    public WorkThread(WorkMode threadWorkMode, String threadName) {
        this.threadWorkMode = threadWorkMode;
        setName(threadName);
    }

    @Override
    public void run() {
        var currentThread = Thread.currentThread();

        while (!currentThread.isInterrupted()) {
            synchronized (WORK_MONITOR) {
                while (threadWorkMode != currentWorkMode) {
                    try {
                        WORK_MONITOR.wait();
                    } catch (InterruptedException e) {
                        logger.info("Thread {} was interrupted", this.getName());
                        return;
                    }
                }

                if (currentWorkMode == WorkMode.COUNT) {
                    calculate();
                    currentWorkMode = WorkMode.DISPLAY;
                } else {
                    currentWorkMode = WorkMode.COUNT;
                }

                logger.info("Counter: {}", currentPosition);

                WORK_MONITOR.notifyAll();

                // для плавного отображения сообщений
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.info("Thread {} was interrupted", this.getName());
                    return;
                }
            }
        }
    }

    private void calculate() {
        if (countDirection == CountDirection.INCREMENT) {
            currentPosition++;
            if (currentPosition == MAX_RANGE) countDirection = CountDirection.DECREMENT;
        } else if (countDirection == CountDirection.DECREMENT) {
            currentPosition--;
            if (currentPosition == MIN_RANGE) countDirection = CountDirection.INCREMENT;
        }
    }

}


enum WorkMode {
    COUNT, DISPLAY
}

enum CountDirection {
    INCREMENT, DECREMENT
}