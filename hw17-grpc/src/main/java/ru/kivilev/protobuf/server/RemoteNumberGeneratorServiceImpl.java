package ru.kivilev.protobuf.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kivilev.protobuf.generated.NumberRequest;
import ru.kivilev.protobuf.generated.NumberResponse;
import ru.kivilev.protobuf.generated.RemoteNumberGeneratorServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class RemoteNumberGeneratorServiceImpl extends RemoteNumberGeneratorServiceGrpc.RemoteNumberGeneratorServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(RemoteNumberGeneratorServiceImpl.class);
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);


    @Override
    public void getNumbers(NumberRequest request,
                           StreamObserver<NumberResponse> responseObserver) {
        logger.info("Got request. FirstValue: {}, LastValue: {}", request.getFirstValue(), request.getLastValue());
        AtomicInteger currentValue = new AtomicInteger(request.getFirstValue());

        Runnable scheduledTask = () -> {
            var value = currentValue.incrementAndGet();
            logger.info("Current value: {}", value);

            responseObserver.onNext(NumberResponse.newBuilder().setNumberValue(value).build());

            if (value == request.getLastValue()) {
                responseObserver.onCompleted();
                logger.info("End of request");
                scheduledExecutorService.shutdown();
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(scheduledTask, 0, 2, TimeUnit.SECONDS);
    }
}
