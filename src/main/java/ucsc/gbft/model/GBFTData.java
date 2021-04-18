package ucsc.gbft.model;

import java.io.Serializable;

public class GBFTData implements Serializable {
    transient double randomId;
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

    public void setRandomId(double _randomId) {
        randomId = _randomId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Data: "+ key + " : " + value;
    }

    @Override
    public boolean equals(Object obj) {
        GBFTData d = (GBFTData) obj;
        return randomId == d.getRandomId() && key == d.getKey() && value == d.getValue();
    }
}
