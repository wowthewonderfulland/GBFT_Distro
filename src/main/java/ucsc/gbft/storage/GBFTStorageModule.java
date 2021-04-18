package ucsc.gbft.storage;

import ucsc.gbft.message.GBFTCenterRouter;
import ucsc.gbft.model.GBFTData;
import ucsc.gbft.model.GBFTView;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GBFTStorageModule {

    private String fileName;
    private SimpleDateFormat df;
    private GBFTStorageModule() {
        GBFTCenterRouter centerRouter = GBFTCenterRouter.getInstance();
        fileName = "center"+centerRouter.getCurrentCenterId()+"log";
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    private volatile static GBFTStorageModule instance;

    public static GBFTStorageModule GBFTStorageModule() {
        if(instance == null) {
            synchronized (GBFTStorageModule.class) {
                if(instance == null) {
                    instance = new GBFTStorageModule();
                }
            }
        }
        return instance;
    }

    public void recordCheckPoint(GBFTView view){
//        new Thread(()->{
            String log = df.format(new Date()) + " CheckPoint: " + view.toString() + "\n";
            try {
                Files.write(Paths.get(fileName), log.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {

            }
//        });
    }

    public void recordData(GBFTData data){
//        new Thread(()-> {
            String log = df.format(new Date()) + " CheckPoint: " + data.toString() + "\n";
            try {
                Files.write(Paths.get(fileName), log.getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {

            }
//        });
    }

}
