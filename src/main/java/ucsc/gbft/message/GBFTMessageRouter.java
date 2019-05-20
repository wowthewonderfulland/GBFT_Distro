package ucsc.gbft.message;

import ucsc.gbft.model.GBFTData;
import ucsc.gbft.model.GBFTMessage;

import java.util.ArrayDeque;

public class GBFTMessageRouter {

    public ArrayDeque<GBFTMessage> viewMessageQueue;
    public ArrayDeque<GBFTData> dataQueue;

    private volatile static GBFTMessageRouter instance;
    private GBFTMessageRouter() {
        viewMessageQueue = new ArrayDeque<>();
        dataQueue = new ArrayDeque<>();
    }
    public static GBFTMessageRouter getInstance() {
        if(instance == null) {
            synchronized (GBFTMessageRouter.class) {
                if(instance == null) {
                    instance = new GBFTMessageRouter();
                }
            }
        }
        return instance;
    }

    public void clearMessage() {
        while (!viewMessageQueue.isEmpty()){
            viewMessageQueue.poll();
        }
    }

}
