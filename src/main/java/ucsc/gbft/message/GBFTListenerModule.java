package ucsc.gbft.message;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GBFTListenerModule {

    private GBFTListenerModule() {}

    private volatile  static  GBFTListenerModule instance;

    public static GBFTListenerModule getInstance() {
        if(instance == null) {
            synchronized (GBFTListenerModule.class) {
                if(instance == null) {
                    instance = new GBFTListenerModule();
                }
            }
        }
        return instance;
    }

    private Server server;

    public void start(int _port, int _centerId) throws IOException {
        server = ServerBuilder.forPort(_port)
                .addService(new GBFTServerImpl(_centerId))
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                stopServer();
                System.exit(1);
            }
        });
    }

    private void stopServer() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
