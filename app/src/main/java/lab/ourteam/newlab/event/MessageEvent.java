package lab.ourteam.newlab.event;

import java.util.HashMap;

public class MessageEvent {
    private HashMap map;
    public MessageEvent(HashMap map){
        this.map=map;
    }

    public HashMap getMap() {
        return map;
    }
}
