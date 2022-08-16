package com.aueb.whatsapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class InfoHandler extends Thread{

    private static InfoHandler instance = null;
    public static String publisherport;
    public static String publisherip;
    public String name;
    public String groupchat;
    Socket socket = null;
    public ObjectOutputStream out = null;
    public ObjectInputStream in = null;
    public static String brokerport="4321";
    public static String brokerip="192.168.56.1";
    private Thread thread;

    public InfoHandler(String name, String publisherip,String publisherport,String groupchat) {
        this.name=name;
        this.publisherip=publisherip;
        this.publisherport=publisherport;
        this.groupchat=groupchat;
        thread = new Thread(this);
        thread.setPriority( Thread.NORM_PRIORITY );
        thread.start();
    }

    public void run()
    {
        String sendMessage = "";
        String message = "";
        // create a server socket
        try {
            socket = new Socket("192.168.56.1", 4321);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            try {
                System.out.println((String) in.readObject());
                sendMessage = "P";
                out.writeObject(sendMessage);
                sendMessage = name;
                out.writeObject(sendMessage);
                sendMessage = groupchat;
                out.writeObject(sendMessage);
                sendMessage = publisherip;
                out.writeObject(sendMessage);
                sendMessage = publisherport;
                out.writeObject(sendMessage);
                sendMessage = "\nPublisher connected to broker with port: " + 4321 + "\n";
                System.out.println(sendMessage);
                message = (String) in.readObject();
                brokerport = message;
                message = (String) in.readObject();
                brokerip = message;
                if (brokerport.equals("4321")) {
                    System.out.println("You r in the right port!");
                } else {
                    System.out.println("The right port is: " + brokerport);
                    connectToNewServer(brokerip,brokerport);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println( "server thread stopped" );
    }

    public void connectToNewServer(String ip,String port) {
        String sendMessage = "";
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println((String) in.readObject());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}
