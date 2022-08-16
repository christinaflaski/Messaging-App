import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Consumer extends UserNode{
    protected static String consumerip;
    private static String consumerport;
    private static ArrayList<String> topicsRegistered = new ArrayList<String>();

    public Consumer(String name, String consumerip,String consumerport){
        super(name);
        this.consumerip=consumerip;
        this.consumerport=consumerport;
    }

    public void run(){
        waitForPublisher();
        connectWithBroker(brokerip, brokerport);
    }

    public static void connectWithBroker(String ip, String port){
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        String sendMessage = " ";
        String message = " ";

        try{
            requestSocket = new Socket(ip, Integer.parseInt(port));

            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());
            System.out.println(in.readObject());
            sendMessage = "C";
            out.writeObject(sendMessage);
            out.writeObject(name);
            out.writeObject(consumerport);
            out.writeObject(consumerip);
            out.writeObject(groupchat);
            getDataFromBroker(in);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void getDataFromBroker(ObjectInputStream in){
        FileOutputStream fileoutput;
        String message="";
        try{
            int chunks = (int)in.readObject();
            if(chunks==0){
                message=(String)in.readObject();
                System.out.println(name+">> "+ message);
            }else {
                int size = (int) in.readObject();
                int last = (int) in.readObject();
                MultimediaFile mmf = null;
                ArrayList<MultimediaFile> mmfList = new ArrayList<>();
                for (int i = 0; i < chunks; i++) { //store chunks in a list
                    mmf = (MultimediaFile) in.readObject();
                    mmfList.add(mmf);
                    System.out.println(name+">>Sending...>> " + mmf.getMFileName());
                }
                System.out.println("\n" + mmf.getMFileName().substring(3) + " has succesfully download!");
                String title = mmf.getMFileName().substring(3);
                File newFile = new File("./media/" + consumerport + "/in/" + title);
                try {
                    fileoutput = new FileOutputStream(newFile);
                    for (int i = 0; i < mmfList.size(); i++) {
                        if (i == mmfList.size() - 1) {
                            fileoutput.write(mmfList.get(i).getChunk(), 0, last);
                        } else {
                            fileoutput.write(mmfList.get(i).getChunk(), 0, size);
                        }
                    }
                    fileoutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void waitForPublisher(){
        try{
            File file = new File("publishers.txt");
            Scanner sc = new Scanner(file);
            int connections = Integer.parseInt(sc.nextLine());
            while(connections<3){
                try {
                    Thread.sleep(1000);
                    File file2 = new File("publishers.txt");
                    Scanner sc2 = new Scanner(file2);
                    connections = Integer.parseInt(sc2.nextLine());
                }
                catch(FileNotFoundException f){
                    System.out.println("file not found");
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted");
                }
            }
        }
        catch(FileNotFoundException f){
            System.out.println("file not found");
        }
    }

}
