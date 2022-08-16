import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Broker{
    private static String hashcode;
    public String Sip;
    public String Sport;
    private static Socket connection = null;
    public static String[][] brokers =new String[3][3];
    private ServerSocket providerSocket;
    InetAddress addr;

    public Broker(String Sip, String Sport) throws UnknownHostException {
        this.Sport=Sport;
        this.Sip=Sip;
        for(int i=0;i<3;i++){
            if(Sport==brokers[i][1]){
                setHashCode(brokers[i][2]);
                break;
            }
        }
        openServer(Sport,Sip);
    }

    public static void main(String[] args) throws UnknownHostException {
        getipandport();
        new Broker(brokers[1][0],brokers[1][1]);

    }

    public static void pull(String ip,int port, ObjectOutputStream output,ObjectInputStream in,String publisherport){
        String sendMessage="";
        String message="";
        MultimediaFile mmf;
        try{
            sendMessage = "Broker with port : " + port + " established connection with Publisher with port : "+port;
            System.out.println(sendMessage);
            int count = (int) in.readObject();
            System.out.println(count);
            output.writeObject(count);
            if(count==0){
                message= (String) in.readObject();
                //output.writeObject(publisherport);
                System.out.println(message);
                output.writeObject(message);
            }else {
                int size = (int) in.readObject();
                output.writeObject(size);
                int last = (int) in.readObject();
                output.writeObject(last);
                System.out.println("size: " + size + ", total of chunks: " + count + ", size of last piece: " + last);
                for (int i = 0; i < count; i++) {
                    mmf = (MultimediaFile) in.readObject();
                    output.writeObject(mmf);
                }
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void openServer(String port,String ip){

        try {
            addr = InetAddress.getByName(ip);
            providerSocket = new ServerSocket(Integer.parseInt(port),10,addr);
            System.out.println("THIS IS THE PORT "+ port);
            while(true) {
                connection = providerSocket.accept();
                System.out.println("Accepted connection : " + connection);
                Thread afb = new ActionForBrokers(connection, this);
                afb.start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                //connection.close();
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    public static String[][] getipandport(){
        try {
            BufferedReader file = new BufferedReader(new FileReader("brokers.txt"));
            Scanner sc=new Scanner(file);
            String line = file.readLine();
            int count =0;
            while(line!=null){
                String[] temp =  line.split(" ");
                brokers[count][0] = temp[0]; //ip
                brokers[count][1] = temp[1]; //port
                brokers[count][2]=hashclass.hashString(temp[0]+temp[1]); //hash
                count++;
                line = file.readLine();
            }
            sc.close();
        }catch(IOException e){
            System.out.println("Could not open file.");
        }
        return brokers;
    }


    public static void setHashCode(String hc){
        hashcode= hc;
    }


    public static String getHashCode() {
        return hashcode;
    }
}
