package ucsc.gbft.protocol;

import ucsc.gbft.message.GBFTCenterRouter;
import ucsc.gbft.message.GBFTMessageRouter;
import ucsc.gbft.message.GBFTSendingModule;
import ucsc.gbft.model.GBFTEnum.*;
import ucsc.gbft.model.*;

import java.util.concurrent.TimeUnit;


public class GBFTDataCenter {


    public enum GBFTVoteResult {
        None,
        Accept,
        Abort
    }

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

    private GBFTData currentData;

    private int votingTickets;

    private Thread timerThread;

    public void init() {
        GBFTCenterRouter centerRouter = GBFTCenterRouter.getInstance();
        checkpoint = new GBFTView(0, centerRouter.getCurrentCenterId(), 0, 0, 0);
    }

    private void prepare(GBFTMessage m) {
        GBFTSendingModule sender = GBFTSendingModule.getInstance();
        currentData = new GBFTData(m.getData().getRandomId(), m.getData().getKey(), m.getData().getValue());
        setUpTimer();
        sender.broadcastConsensusMessage(m);
    }

    private void prePrepare(GBFTData data) {
        GBFTSendingModule sender = GBFTSendingModule.getInstance();
        currentData = new GBFTData(data.getRandomId(), data.getKey(), data.getValue());
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

        if (m.getData().equals(currentData) && m.getView().equals(checkpoint)) {
            votingTickets++;
            if (votingTickets >= totalCenters * 2 / 3){
                timerThread.interrupt();
                return GBFTVoteResult.Accept;
            }
        }else if (!m.getData().equals(currentData) && m.getView().equals(checkpoint)) {
            return GBFTVoteResult.Abort;
        }

        return GBFTVoteResult.None;
    }

    private void viewChange() {

    }

    private void setUpTimer() {
        timerThread = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                return;
            }
            viewChange();
        });
        timerThread.start();
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

                if(message.getView().getViewStage() == GBFTViewStage.None) {
                    prepare(message);
                }else{
                    if(checkpoint.equals(message.getView())) {
                        GBFTVoteResult voteResult = vote(message);
                        if (voteResult == GBFTVoteResult.Accept) {
                            if (checkpoint.getViewStage() == GBFTViewStage.Prepare) {
                                commit(message);
                            }else  if (checkpoint.getViewStage() == GBFTViewStage.Commit) {
                                response(message);
                            }
                        } else if(voteResult == GBFTVoteResult.Abort){
                            viewChange();
                        }
                    }
                }
            }else {
                synchronized (messageRouter.viewMessageQueue) {
                    messageRouter.clearMessage();
                }
                prePrepare(data);
        }
    }

}