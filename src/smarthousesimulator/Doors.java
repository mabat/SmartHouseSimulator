package smarthousesimulator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doors {

    private List<Long> Keys = new ArrayList<>();
    private Long resetCount;
    private Map<String, List<Long>> LogDoorOpening = new HashMap<>();
    private Map<String, List<Long>> LogDoorUnlocking = new HashMap<>();
    private Map<String, Long> Cmd = new HashMap<>();
    private Long cmdUnlock;


    public Doors() {} 
    
    public List<Long> getKeys(){
        return this.Keys;
    }
    public Long getResetCount(){
        return this.resetCount;
    }
    public Map<String, List<Long>> getLogDoorOpening(){
        return this.LogDoorOpening;
    }
    public Map<String, List<Long>> getLogDoorUnlocking(){
        return this.LogDoorUnlocking;
    }
    public Map<String, Long> getCmd(){
        return this.Cmd;
    }
    public Long getcmdUnlock(){
        return this.cmdUnlock;
    }
    
    public void setKeys(List<Long> keys){
        this.Keys = keys;
    }

}
