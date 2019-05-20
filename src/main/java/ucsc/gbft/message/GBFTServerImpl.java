package ucsc.gbft.message;

import io.grpc.stub.StreamObserver;
import ucsc.gbft.comm.*;
import ucsc.gbft.model.GBFTData;
import ucsc.gbft.model.GBFTMessage;

final class GBFTServerImpl extends ConsensusGrpc.ConsensusImplBase {
    private int centerId;

    public GBFTServerImpl(int _centerId) {
        centerId = _centerId;
    }
    @Override
    public void startMessage(StartRequest req, StreamObserver<ConfirmReply> responseObserver) {
        handleStartMessage(req);
        ConfirmReply reply = ConfirmReply.newBuilder().setConfirm(1).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void claimMessage(ConsensusRequest req, StreamObserver<ConfirmReply> responseObserver) {
        handleClaimMessage(req);
        ConfirmReply reply = ConfirmReply.newBuilder().setConfirm(1).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void viewChange(ViewChangeRequest req, StreamObserver<ConfirmReply> responseObserver) {
        handleViewChange(req);
        ConfirmReply reply = ConfirmReply.newBuilder().setConfirm(1).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private void handleStartMessage(StartRequest _req){
        GBFTMessageRouter messageRouter = GBFTMessageRouter.getInstance();
        GBFTData data = new GBFTData(_req.getRandomId(), _req.getKey(), _req.getValue());
        synchronized (messageRouter.dataQueue) {
            messageRouter.dataQueue.offer(data);
        }
    }

    private void handleViewChange(ViewChangeRequest _req){

    }

    private void handleClaimMessage(ConsensusRequest _req){
        GBFTMessageRouter messageRouter = GBFTMessageRouter.getInstance();
        GBFTMessage m = new GBFTMessage(_req);
        synchronized (messageRouter.viewMessageQueue) {
            messageRouter.viewMessageQueue.offer(m);
        }
    }
}