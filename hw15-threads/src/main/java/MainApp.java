public class MainApp {

    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 10;

    private static final Object WORK_MONITOR = new Object();

    private static int currentPosition = 0;
    private static WorkMode workMode = WorkMode.MAKE_OPERATION;
    private static CountDirection countDirection = CountDirection.INCREMENT;

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

                    System.out.printf("%s: %s\r\n", currentThread.getName(), currentPosition);

                    workMode = WorkMode.LOG;
                    WORK_MONITOR.notify();

                    try {
                        while (workMode != WorkMode.MAKE_OPERATION) {
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
                    while (workMode != WorkMode.LOG) {
                        try {
                            WORK_MONITOR.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                    System.out.printf("%s: %s\r\n", currentThread.getName(), currentPosition);

                    try {
                        Thread.sleep(1000);// чтоб успевали прочесть сообщения
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    workMode = WorkMode.MAKE_OPERATION;
                    WORK_MONITOR.notify();
                }
            }
        });

        masterThread.start();
        slaveThread.start();
    }
}

enum WorkMode {
    MAKE_OPERATION, LOG
}

enum CountDirection {
    INCREMENT, DECREMENT
}