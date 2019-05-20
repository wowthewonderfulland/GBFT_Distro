package ucsc.gbft.model;

public class GBFTEnum {

    public enum GBFTVoteResult {
        None,
        Accept,
        Abort;
    }

    public enum GBFTViewStage {
        None,Prepare,Commit;
    }
}
