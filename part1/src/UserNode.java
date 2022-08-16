import java.io.IOException;
import java.util.Scanner;

public class UserNode extends Thread{
    public static String name;
    protected static String groupchat;
    protected static String brokerport="4321";
    protected static String brokerip="192.168.56.1";
    protected static boolean b=true;

    public UserNode(String name){
        this.name=name;
    }
    public static void main(String[] args){

        System.out.println("Enter Username:");
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();
        Publisher publisher = new Publisher(name,"192.168.56.1","500");
        Thread pThread = new Thread(publisher);

        Consumer consumer = new Consumer(name,"192.168.56.1","500");
        Thread cThread = new Thread(consumer);
        cThread.start();
        pThread.start();

    }

    public String getGroupchat() {
        return groupchat;
    }

    public void setGroupchat(String groupchat) {
        this.groupchat = groupchat;
    }
}
