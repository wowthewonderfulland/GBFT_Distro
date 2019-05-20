package ucsc.gbft;

import ucsc.gbft.message.GBFTCenterRouter;
import ucsc.gbft.message.GBFTListenerModule;
import ucsc.gbft.message.GBFTSendingModule;
import ucsc.gbft.protocol.GBFTDataCenter;

import java.io.IOException;
import java.util.HashMap;

public class App
{   
    public static void main( String[] args ) {

        if (args.length != 1) {
            System.out.println("wrong args!");
            System.exit(1);
        }

        GBFTCenterRouter centerRouter = GBFTCenterRouter.getInstance();
        centerRouter.init(Integer.parseInt(args[0]));

        GBFTListenerModule server = GBFTListenerModule.getInstance();
        new Thread (()->{
            try {
                server.start(centerRouter.getCurrentPort(), centerRouter.getCurrentCenterId());
                try {
                    server.blockUntilShutdown();
                }catch (InterruptedException e) {
                    System.out.println("Interrupt Error");
                    System.exit(1);
                }
            } catch (IOException e) {
                System.out.println("IO Error");
                System.exit(1);
            }
        }).start();

        GBFTSendingModule sender = GBFTSendingModule.getInstance();
        sender.init(centerRouter.getWorkingPorts(), centerRouter.getWorkingHosts());

        GBFTDataCenter center = GBFTDataCenter.getInstance();
        center.init();
        center.start();

    }

}