package ucsc.gbft.protocol;

import ucsc.gbft.message.GBFTCenterRouter;
import ucsc.gbft.message.GBFTMessageRouter;
import ucsc.gbft.message.GBFTSendingModule;
import ucsc.gbft.model.GBFTEnum.*;
import ucsc.gbft.model.*;

public class GBFTDataCenter {

    private GBFTDataCenter() {}

    private volatile static GBFTDataCenter instance;

    public static GBFTDataCenter getInstance() {
        if(instance == null) {
            synchronized (GBFTDataCenter.class) {
                if(instance == null) {
                    instance = new GBFTDataCenter();
                }
            }
        }
        return instance;
    }

    private GBFTView checkpoint;

    public void init() {
        GBFTCenterRouter centerRouter = GBFTCenterRouter.getInstance();
        checkpoint = new GBFTView(0, centerRouter.getCurrentCenterId(), 0, 0, 0);
    }

    private void prepare(GBFTMessage m) {
        GBFTSendingModule sender = GBFTSendingModule.getInstance();

        setUpTimer();
        sender.broadcastConsensusMessage(m);
    }

    private void prePrepare(GBFTData data) {
        GBFTSendingModule sender = GBFTSendingModule.getInstance();

        setUpTimer();
        sender.broadcastConsensusMessage(new GBFTMessage(checkpoint, data));
    }


    private void commit(GBFTMessage m) {
        GBFTSendingModule sender = GBFTSendingModule.getInstance();

        setUpTimer();
        sender.broadcastConsensusMessage(m);
    }

    private void response(GBFTMessage m) {
        GBFTSendingModule sender = GBFTSendingModule.getInstance();
        sender.releaseResponseToClient(m);
    }

    private GBFTVoteResult vote(GBFTMessage m) {
        GBFTCenterRouter centerRouter = GBFTCenterRouter.getInstance();
        int totalCenters = centerRouter.getTotalVoters();
        //TODO
        return GBFTVoteResult.None;
    }

    private void viewChange() {
        
    }

    private void setUpTimer() {

    }

    public void start(){
        GBFTMessageRouter messageRouter = GBFTMessageRouter.getInstance();


        for(;;){
            GBFTData data;
            synchronized (messageRouter.dataQueue){
                data = messageRouter.dataQueue.poll();
            }

            if(data == null) {
                GBFTMessage message;
                synchronized (messageRouter.viewMessageQueue) {
                    message = messageRouter.viewMessageQueue.poll();
                }
                if(message == null) {
                    continue;
                }
                if(checkpoint.equals(message.getView())) {

                        GBFTVoteResult r = vote(message);
                        if(r == GBFTVoteResult.Accept) {
                            if(this.checkpoint.getViewStage() == GBFTViewStage.Prepare) {
                                commit(message);
                            }else if(this.checkpoint.getViewStage() == GBFTViewStage.Commit){
                                response(message);
                            }
                        }else if(r == GBFTVoteResult.Abort) {
                            viewChange();
                        }else if(r == GBFTVoteResult.None) {
                            continue;
                        }

                }else{
                    continue;
                }
            }else {
                synchronized (messageRouter.viewMessageQueue) {
                    messageRouter.clearMessage();
                }
                prePrepare(data);
            }
        }
    }

}