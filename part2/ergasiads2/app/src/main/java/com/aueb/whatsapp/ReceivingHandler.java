package com.aueb.whatsapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ReceivingHandler extends Thread {
    Socket socket = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    Thread thread;
    String message;
    public String ip;
    public String port;
    public String publisherport;
    public String msgType;
    public String returnmess;

    public ReceivingHandler(String ip, String port,String publisherport) {
        this.ip = ip;
        this.port = port;
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
            try {
                System.out.println((String) in.readObject());
                out.writeObject("C");
                int receive2 = (Integer) in.readObject();
                /*receive = (String) in.readObject();
                if(receive.equals(publisherport)){
                    msgType="MSG_TYPE_SENT";
                }else{
                    msgType="MSG_TYPE_RECEIVED";
                }*/
                returnmess = (String) in.readObject();
                System.out.println(returnmess);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getReturnmess() {
        return returnmess;
    }

    public void setReturnmess(String returnmess) {
        this.returnmess = returnmess;
    }
}
