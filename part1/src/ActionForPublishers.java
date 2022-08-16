import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ActionForPublishers extends Thread {
    public static ArrayList<ActionForPublishers> publishers = new ArrayList<ActionForPublishers>();
    private Socket connection;
    private Publisher publisher;
    public static ObjectInputStream in;
    public static ObjectOutputStream out;


    public ActionForPublishers(Socket connection,Publisher publisher) {
        this.connection = connection;
        this.publisher=publisher;
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
        publishers.add(this);
    }


    public void run(){
        String message = "";
        try {
            System.out.println("Publisher connected in " + publisher.getGroupchat() + " with ip:" + publisher.getPublisherip());
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            push(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void push(String mes) {
        String editedmes=choosemessage(publisher.getPublisherPort(),mes);
        int chunks=0;
        if(editedmes==mes){//htan aplo string to message
            try {
                out.writeObject(chunks);
                out.writeObject(mes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {//htan file to message

            try {
                File file=new File(editedmes);
                int sizeOfFiles = 1024 * 512;
                int partCounter = 1;
                byte[] buffer = new byte[sizeOfFiles];
                String nameOfFile = file.getName();
                chunks = (int) file.length() / sizeOfFiles;
                int last = (int) file.length() % sizeOfFiles;
                int totalChunks = chunks + 1;
                out.writeObject(totalChunks);
                out.writeObject(sizeOfFiles);
                out.writeObject(last);
                FileInputStream finput = new FileInputStream(file);
                MultimediaFile mmf;
                byte[] temp;
                for (int i = 0; i < totalChunks; i++) {
                    String filePartName = String.format("%02d_%s", partCounter++, nameOfFile);
                    if (i == chunks) {
                        finput.read(buffer, 0, last);
                        temp = Arrays.copyOf(buffer, last);
                    } else {
                        finput.read(buffer, 0, sizeOfFiles);
                        temp = Arrays.copyOf(buffer, sizeOfFiles);
                    }
                    mmf = new MultimediaFile(filePartName, temp);
                    out.writeObject(mmf);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String choosemessage(String port, String message){
        int len=message.length();
        char[] c=message.toCharArray();
        String newmes="";
        for (int i=0;i<len;i++){
            if(i>=len-4){
                newmes+=c[i];
            }
        }
        if(newmes.equals(".mp4")||newmes.equals(".png")){
            message="./media/"+port+"/"+message;
        }
        return message;
    }

    public void CloseAll(){
        try{
            in.close();
            out.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
