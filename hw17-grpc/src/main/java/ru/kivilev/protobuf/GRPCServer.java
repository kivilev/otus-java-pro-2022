package ru.kivilev.protobuf;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import ru.kivilev.protobuf.server.RemoteNumberGeneratorServiceImpl;

import java.io.IOException;

public class GRPCServer {
    public static final int SERVER_PORT = 8190;
    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteDBService = new RemoteNumberGeneratorServiceImpl();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteDBService).build();
        server.start();

        logger.info("Server waiting for client connections...");

        server.awaitTermination();
    }
}
