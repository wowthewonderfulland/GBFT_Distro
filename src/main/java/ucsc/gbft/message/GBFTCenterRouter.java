package ucsc.gbft.message;

import java.util.HashMap;

public class GBFTCenterRouter {
    HashMap<Integer, String> hostList;
    HashMap<Integer, Integer> portList;

    int currentCenterId;
    String currentHost;
    int currentPort;

    HashMap<Integer, String> workingHosts;
    HashMap<Integer, Integer> workingPorts;

    int clientPort;
    String clientHost;

    int totalVoters;

    private volatile static GBFTCenterRouter instance;

    private GBFTCenterRouter() {
        hostList = new HashMap<>();
        hostList.put(0, "127.0.0.1");
        hostList.put(1, "127.0.0.1");
        hostList.put(2, "127.0.0.1");
        hostList.put(3, "127.0.0.1");
        hostList.put(4, "127.0.0.1");

        portList = new HashMap<>();
        portList.put(0, 20000);
        portList.put(1, 20001);
        portList.put(2, 20002);
        portList.put(3, 20003);
        portList.put(4, 20004);

        totalVoters = 5;
        clientHost = "127.0.0.1";
        clientPort = 50021;
    }

    public static GBFTCenterRouter getInstance() {
        if (instance == null) {
            synchronized (GBFTMessageRouter.class) {
                if (instance == null) {
                    instance = new GBFTCenterRouter();
                }
            }
        }
        return instance;
    }

    public void init(int _currentId) {
        currentCenterId = _currentId;
        workingHosts = new HashMap<>(hostList);
        workingPorts = new HashMap<>(portList);
        workingHosts.remove(currentCenterId);
        workingPorts.remove(currentCenterId);
    }

    public int getCurrentCenterId() {
        return currentCenterId;
    }

    public int getCurrentPort() {
        return currentPort;
    }

    public String getCurrentHost() {
        return currentHost;
    }

    public HashMap<Integer, Integer> getWorkingPorts() {
        return workingPorts;
    }

    public HashMap<Integer, String> getWorkingHosts() {
        return workingHosts;
    }

    public void handleDeadCenter(int deadCenter) {
        workingPorts.remove(deadCenter);
        workingHosts.remove(deadCenter);
        totalVoters--;
    }

    public int getTotalVoters() {
        return totalVoters;
    }

    public int getClientPort() {
        return clientPort;
    }

    public String getClientHost() {
        return clientHost;
    }
}
