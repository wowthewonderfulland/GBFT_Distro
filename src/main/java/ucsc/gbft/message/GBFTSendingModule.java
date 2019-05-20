package ucsc.gbft.message;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import ucsc.gbft.comm.*;
import ucsc.gbft.model.GBFTEnum.*;
import ucsc.gbft.model.GBFTMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GBFTSendingModule {


    private volatile static GBFTSendingModule instance;

    private GBFTSendingModule() {
        channels = new HashMap<>();
        blockingStubs = new HashMap<>();
    }

    public static GBFTSendingModule getInstance() {
        if (instance == null) {
            synchronized (GBFTSendingModule.class) {
                if (instance == null) {
                    instance = new GBFTSendingModule();
                }
            }
        }
        return instance;
    }

    private HashMap<Integer,ManagedChannel> channels;
    private HashMap<Integer,ConsensusGrpc.ConsensusBlockingStub> blockingStubs;
  
    public void init(HashMap<Integer, Integer> centerPorts,HashMap<Integer, String> centerIPs){
        for(Integer k : centerIPs.keySet()) {
            buildSingleConnection(k, centerIPs.get(k), centerPorts.get(k));
        }
    }

    private void buildSingleConnection(int k ,String host, int port){
        ManagedChannel c =  ManagedChannelBuilder.forAddress(host, port).useTransportSecurity().build();
        channels.put(k,c);
        ConsensusGrpc.ConsensusBlockingStub b = ConsensusGrpc.newBlockingStub(c);
        blockingStubs.put(k, b);
    }

    public void shutdown(int k) throws InterruptedException {
        ManagedChannel c = channels.get(k);
        c.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void handleDeadCenter(int k) throws InterruptedException {
        blockingStubs.remove(k);
        shutdown(k);
        channels.remove(k);
    }

    private ConsensusRequest generateRequest(GBFTMessage m) {
        ConsensusRequest.Builder b = ConsensusRequest.newBuilder();
        b.setCenterId(m.getView().getCenterId());
        b.setMasterId(m.getView().getMasterId());
        b.setViewId(m.getView().getViewId());
        b.setSequenceId(m.getView().getSequenceId());
        int nextStage = m.getView().getViewStage() == GBFTViewStage.None?0:(m.getView().getViewStage() == GBFTViewStage.Prepare?1:2);
        b.setConsensusStage(nextStage);
        b.setRandomId(m.getData().getRandomId());
        b.setKey(m.getData().getKey());
        b.setValue(m.getData().getValue());
        ConsensusRequest request = b.build();
        return request;
    }

    public void broadcastConsensusMessage(GBFTMessage m) {
        ConsensusRequest request = generateRequest(m);
        for(Integer k : blockingStubs.keySet()){
            ConsensusGrpc.ConsensusBlockingStub b = blockingStubs.get(k);
            try {
                b.claimMessage(request);
            } catch (StatusRuntimeException e) {
                return;
            }
        }
    }

    public void releaseResponseToClient(GBFTMessage m) {
        EndRequest.Builder b =  EndRequest.newBuilder();
        b.setCenterId(m.getView().getCenterId());
        b.setRandomId(m.getData().getRandomId());
        b.setKey(m.getData().getKey());
        b.setValue(m.getData().getValue());
        EndRequest req = b.build();
        try {

        }catch (StatusRuntimeException e) {
            return;
        }
    }
}