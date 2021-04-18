package ucsc.gbft.model;

import ucsc.gbft.model.GBFTEnum.GBFTViewStage;

import java.io.Serializable;

public class GBFTView implements Serializable {

    int masterId;
    int centerId;
    int viewId;
    int sequenceId;
    GBFTViewStage viewStage;

    public GBFTView(int _masterId, int _centerId, int _viewId, int _sequenceId, int _stage){
        masterId = _masterId;
        centerId = _centerId;
        viewId = _viewId;
        sequenceId = _sequenceId;
        viewStage = _stage == 0?GBFTViewStage.None:(_stage == 1?GBFTViewStage.Prepare:GBFTViewStage.Commit);
    }

    public GBFTViewStage getViewStage() {
        return viewStage;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public int getViewId() {
        return viewId;
    }

    public int getCenterId() {
        return centerId;
    }

    public int getMasterId() {
        return masterId;
    }

    @Override
    public boolean equals(Object obj) {
        GBFTView v = (GBFTView)obj;
        return v.masterId == masterId && viewId == v.viewId && sequenceId == v.sequenceId && viewStage == v.viewStage;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public void setViewStage(GBFTViewStage viewStage) {
        this.viewStage = viewStage;
    }

    @Override
    public String toString() {
        return "Master: "+ masterId+ " Center: " + centerId + " View: " + viewId + " Sequence: " + sequenceId + " Stage: "
                + (viewStage == GBFTViewStage.None? 0 :(viewStage == GBFTViewStage.Prepare? 1:2));
    }
}