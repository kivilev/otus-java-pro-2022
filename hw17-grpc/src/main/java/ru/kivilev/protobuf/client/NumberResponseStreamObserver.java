package ru.kivilev.protobuf.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kivilev.protobuf.generated.NumberResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberResponseStreamObserver implements StreamObserver<NumberResponse> {
    private final AtomicInteger valueFromServer = new AtomicInteger(0);
    private final CountDownLatch latch;
    private static final Logger logger = LoggerFactory.getLogger(NumberResponseStreamObserver.class);

    public NumberResponseStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(NumberResponse value) {
        valueFromServer.set(value.getNumberValue());
        logger.info("Client got value from server: {} ", valueFromServer.get());
    }

    @Override
    public void onError(Throwable t) {
        logger.error("Some error happened: {}", t.getMessage());
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        logger.info("got signal about completion");
        latch.countDown();
    }

    public int getAndSetNumberValueFromServer(int newValue) {
        return valueFromServer.getAndSet(newValue);
    }
}
