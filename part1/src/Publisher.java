import java.io.*;
import java.net.*;
import java.util.*;

public class Publisher extends UserNode{
    private static String publisherport;
    private static String publisherip;
    private static ArrayList<String> topics = new ArrayList<String>();
    InetAddress addr;


    public Publisher(String name, String publisherip, String publisherport){
        super(name);

        this.publisherip = publisherip;
        this.publisherport = publisherport;
        try {
            addr = InetAddress.getByName(publisherip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        choosetopic(publisherport);

    }

    public void run() {
        try {
            connectToServer(brokerport,brokerip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        openPublisherServer();
    }

    public void connectToNewServer(String port) {
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        String sendMessage = "";
        try {
            socket = new Socket("localhost", Integer.parseInt(port));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println((String) in.readObject());
            connectedPub();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void connectToServer(String port,String ip) throws IOException {//sends the artists that are contained in it
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        String sendMessage = "";
        String message = "";
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            try {
                System.out.println((String) in.readObject());
                sendMessage = "P";
                out.writeObject(sendMessage);
                sendMessage = getPublisherName();
                out.writeObject(sendMessage);
                sendMessage = getGroupchat();
                out.writeObject(sendMessage);
                sendMessage = getPublisherip();
                out.writeObject(sendMessage);
                sendMessage = getPublisherPort();
                out.writeObject(sendMessage);
                sendMessage = "\nPublisher connected to broker with port: " + port + "\n";
                System.out.println(sendMessage);
                message = (String) in.readObject();
                brokerport = message;
                message = (String) in.readObject();
                brokerip = message;
                if (brokerport.equals("4321")) {
                    System.out.println("You r in the right port!");
                    connectedPub();
                } else {
                    System.out.println("The right port is: " + brokerport);
                    connectToNewServer(brokerport);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            in.close();
            out.close();
            socket.close();
        }
    }

    //prosthetei to groupchat sto txt file tou user, an eggraftei se neo groupchat
    public static void addgroupchat(String port, String newgc) {
        try {
            String filename = port + ".txt";
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write("\n" + newgc);//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public static String fileToString(File f) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String input = null;
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                input = sc.nextLine();
                stringBuffer.append(input);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        return stringBuffer.toString();
    }


    public void openPublisherServer() {
        ServerSocket providerSocket = null;
        Socket connection = null;
        try {
            providerSocket = new ServerSocket(Integer.parseInt(publisherport),10,addr);
            while (!providerSocket.isClosed()) {
                connection = providerSocket.accept();
                System.out.println("Accepted connection : " + connection);
                Thread afp = new ActionForPublishers(connection, this);
                afp.start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                //connection.close();
                providerSocket.close();
                b=false;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    public void connectedPub() {
        int connections = 0;
        try {
            File activeConnections = new File("publishers.txt");
            Scanner sc = new Scanner(activeConnections);
            String result = fileToString(activeConnections);
            while (sc.hasNextLine()) {
                connections = Integer.parseInt(sc.nextLine());
            }
            int newConnections = connections + 1;
            result = result.replaceAll(String.valueOf(connections), String.valueOf(newConnections));
            PrintWriter writer = new PrintWriter(new File("publishers.txt"));
            writer.append(result);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void choosetopic(String port) {
        Scanner input;
        BufferedReader file;
        System.out.println("Choose the groupchat you want to join >>");
        System.out.println("Your subscribed groupchats: ");
        try {
            file = new BufferedReader(new FileReader(port + ".txt"));
            String line = file.readLine();
            int count = 0;
            while (line != null) {
                topics.add(line);
                System.out.println(topics.get(count));
                count++;
                line = file.readLine();
            }
            System.out.println("\n If you want to subscribe to another groupchat, press 1");
            input = new Scanner(System.in);
            String answer = input.next();
            if (answer.equals("1")) {
                System.out.println("The available groupchats: ");
                file = new BufferedReader(new FileReader("groupchats.txt"));
                line = file.readLine();
                count = 0;
                while (line != null) {
                    topics.add(line);
                    System.out.println(topics.get(count));
                    count++;
                    line = file.readLine();
                }
                System.out.println("Choose the groupchat you want to subscribe >>");
                input = new Scanner(System.in);
                setGroupchat(input.nextLine());
                addgroupchat(port, getGroupchat());
                System.out.println("You succesfully subscribe to " + groupchat);
            } else {
                setGroupchat(answer);
            }
        } catch (IOException e) {
            System.out.println("Could not open file.");
        }
    }

    public static String getPublisherip() {
        return publisherip;
    }

    public static String getPublisherPort() {
        return publisherport;
    }

    public static String getPublisherName() {
        return name;
    }
}