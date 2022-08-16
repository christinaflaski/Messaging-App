import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;


public class ActionForBrokers extends Thread {
    private Socket connection;
    Broker broker;
    public ObjectInputStream in;
    public static ObjectOutputStream out;
    public static ObjectOutputStream out2;
    private String publisherport;

    public ActionForBrokers(Socket connection, Broker broker) {
        this.connection=connection;
        this.broker=broker;
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run() {
        String sendMessage = "";
        String message = "";
        try {
            try {
                int num = Integer.parseInt(broker.getHashCode()) + 1;
                sendMessage = "broker No" + num + " >> welcome";
                out.writeObject(sendMessage);
                message = (String) in.readObject();
                out.flush();
                if (message.equals("C")) {
                    try {
                        broker.pull(broker.Sip,Integer.parseInt(broker.Sport), out,in,publisherport);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (message.equals("P") && Integer.parseInt(broker.Sport) == 4321) {
                    String[] tempPublisher = new String[4];
                    message = (String) in.readObject();
                    tempPublisher[0] = message; //publisher name
                    message = (String) in.readObject();
                    tempPublisher[1] = message; //publisher gc
                    message = (String) in.readObject();
                    tempPublisher[2] = message; //publisher ip
                    message = (String) in.readObject();
                    tempPublisher[3] = message; //publisher port
                    publisherport=message;
                    System.out.println(tempPublisher[0] + " with ip: " + tempPublisher[2] + " and port: " + tempPublisher[3] + " has connected to "+tempPublisher[1]);
                    String newport = String.valueOf(findBroker(tempPublisher[1]));
                    out.writeObject(newport);
                    String newip=null;
                    for(int i=0; i<broker.brokers.length;i++){
                        if(newport.equals(broker.brokers[i][1])){
                            newip=broker.brokers[i][0];
                        }
                    }
                    out.writeObject(newip);
                    saveGroupchat(tempPublisher[1],tempPublisher[3],newport,tempPublisher[0]);
                    out.flush();
                    //broker.pull(broker.Sip,Integer.parseInt(broker.Sport), out,in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //psaxnei na brei se poion broker tairiazei to sygkekrimeno groupchat me bash to hash toys
    public int findBroker(String gcName) throws IOException {
        int gcHash = Integer.parseInt(hashclass.hashString(gcName));
        int port = 0;
        for(int i = 0; i < broker.brokers.length; i++) {
            if (gcHash==Integer.parseInt(broker.brokers[i][2])) {
                port = Integer.parseInt(broker.brokers[i][1]);
            }
        }
        return port;
    }
    public void saveGroupchat(String gc,String port,String newport,String name)  {
        try{
            String filename="publishers_info.txt";
            FileWriter fw =new FileWriter(filename,true);
            FileReader fr=new FileReader(filename);
            BufferedReader file=new BufferedReader(fr);
            String line=file.readLine();
            boolean b=false;
            while(line!=null){
                String[] temp =  line.split(" ");
                if((port.equals(temp[0]))&& (gc.equals(temp[2]))){
                    b=true;
                    break;
                }
                line = file.readLine();
            }
            if(b==false) {
                fw.write(port + " " + name + " " + gc + " " + newport + "\n");
            }
            fw.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
