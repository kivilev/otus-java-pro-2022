package ru.kivilev.protobuf;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kivilev.protobuf.client.CountService;
import ru.kivilev.protobuf.client.NumberResponseStreamObserver;
import ru.kivilev.protobuf.generated.NumberRequest;
import ru.kivilev.protobuf.generated.RemoteNumberGeneratorServiceGrpc;

import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);


    public static void main(String[] args) throws InterruptedException {

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var asyncClient = RemoteNumberGeneratorServiceGrpc.newStub(channel);

        var latch = new CountDownLatch(1);
        var numberResponseStreamObserver = new NumberResponseStreamObserver(latch);

        asyncClient.getNumbers(
                NumberRequest.newBuilder().setFirstValue(1).setLastValue(30).build(),
                numberResponseStreamObserver
        );

        var countService = new CountService(numberResponseStreamObserver);
        countService.runCycle();

        latch.await();
        logger.info("client is shutting down...");
        channel.shutdown();
    }
}
