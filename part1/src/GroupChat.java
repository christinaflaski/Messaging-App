import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupChat implements Serializable{
    private String gcname;
    HashMap<String,String> allmembers=new HashMap<>();
    private String brokerport;
    private ArrayList<String> chat;

    public GroupChat(String gcname,String brokerport){
        this.gcname= gcname;
        this.brokerport=brokerport;
    }

    public String getGcname(){
        return gcname;
    }

    public void setGcname(){
        this.gcname = gcname;
    }

    public HashMap<String, String> getAllmembers() {
        return allmembers;
    }

    public void setAllmembers(HashMap<String, String> allmembers) {
        this.allmembers = allmembers;
    }

    public String getBrokerPort() {
        return brokerport;
    }

    public void setBrokerport(String brokerport) {
        this.brokerport = brokerport;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
    }
}

