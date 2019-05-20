package ucsc.gbft.model;

import ucsc.gbft.model.GBFTEnum.GBFTViewStage;

public class GBFTView {

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
}