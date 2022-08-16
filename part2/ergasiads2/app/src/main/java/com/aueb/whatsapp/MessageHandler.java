package com.aueb.whatsapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageHandler extends Thread {
    Socket socket = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    Thread thread;
    String message;
    public String ip;
    public static String port;
    public String publisherport;
    public String msgType;
    public String returnmess;
    public static String name;

    public MessageHandler(String name,String ip, String port, String message,String publisherport) {
        this.name=name;
        this.ip = ip;
        this.port = port;
        this.message = message;
        this.publisherport=publisherport;
        thread = new Thread(this);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();
    }

    public void run() {
        String sendMessage = "";
        String receive = "";
        String backmess="";
        // create a server socket
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println((String) in.readObject());
            out.writeObject("C");
            String editedmes=choosemessage(publisherport,message);
            int chunks=0;
            if(editedmes==message){//htan aplo string to message
                try {
                    out.flush();
                    out.writeObject(chunks);
                    out.writeObject(message);
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
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                int receive2 = (Integer) in.readObject();
                /*receive = (String) in.readObject();
                if(receive.equals(publisherport)){
                    msgType="MSG_TYPE_SENT";
                }else{
                    msgType="MSG_TYPE_RECEIVED";
                }*/
                backmess = (String) in.readObject();
                setReturnmess(backmess);
                System.out.println(backmess);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void getDataFromBroker(ObjectInputStream in){
        FileOutputStream fileoutput;
        String message="";
        try{
            message=(String)in.readObject();
            int chunks = Integer.parseInt(message);
            System.out.println("received mess");
            if(chunks==0){
                message=(String)in.readObject();
                System.out.println(name+">> "+ message);
            }else {
                message=(String)in.readObject();
                int size = Integer.parseInt(message);
                message=(String)in.readObject();
                int last = Integer.parseInt(message);
                MultimediaFile mmf = null;
                ArrayList<MultimediaFile> mmfList = new ArrayList<>();
                for (int i = 0; i < chunks; i++) { //store chunks in a list
                    mmf = (MultimediaFile) in.readObject();
                    mmfList.add(mmf);
                    System.out.println(name+">>Sending...>> " + mmf.getMFileName());
                }
                System.out.println("\n" + mmf.getMFileName().substring(3) + " has succesfully download!");
                String title = mmf.getMFileName().substring(3);
                File newFile = new File("./media/" + port + "/in/" + title);
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

    public String getMsgType(){
        return msgType;
    }

    public String getReturnmess() {
        return returnmess;
    }

    public void setReturnmess(String returnmess) {
        this.returnmess = returnmess;
    }
}
