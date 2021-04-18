package ucsc.gbft.model;

import ucsc.gbft.comm.ConsensusRequest;

import java.io.Serializable;

public class GBFTMessage implements Serializable {
    GBFTView view;
    GBFTData data;

    public GBFTMessage(ConsensusRequest _req) {
        view = new GBFTView(_req.getMasterId(),
                _req.getCenterId(),
                _req.getViewId(),
                _req.getSequenceId(),
                _req.getConsensusStage());
        data = new GBFTData(_req.getRandomId(),
                _req.getKey(),
                _req.getValue());
    }

    public GBFTMessage(GBFTView _view, GBFTData _data){
        view = new GBFTView(_view.masterId, _view.centerId, _view.viewId, _view.sequenceId, _view.viewStage == GBFTEnum.GBFTViewStage.None?0:(_view.viewStage == GBFTEnum.GBFTViewStage.Prepare?1:2));
        data = new GBFTData(_data.getRandomId(),_data.getKey(),_data.getValue());
    }

    public GBFTData getData() {
        return data;
    }

    public GBFTView getView() {
        return view;
    }

    public void setView(GBFTView view) {
        this.view = view;
    }

    public void setData(GBFTData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Current View: " + view + " Data: " + data;
    }
}