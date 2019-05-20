package ucsc.gbft.model;

public class GBFTData {
    double randomId;
    String key;
    String value;

    public GBFTData(double _randomId, String _key, String _value) {
        randomId = _randomId;
        key = _key;
        value = _value;
    }

    public double getRandomId() {
        return randomId;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
