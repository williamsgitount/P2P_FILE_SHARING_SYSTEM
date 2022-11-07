import java.net.*;
import java.io.*;

public class Ping extends Thread {
    String fileName;
    String source;
    String destination;
    String ip

    Ping(String fileName, String destination, String source) {
        super();
        this.fileName = fileName;
        this.destination = destination;
        this.source = source;
        this.ip = ip;
    }

    public void createConnection() throws IOException, ClassNotFoundException {

        Socket socket = new Socket(this.destination ,5000);
        System.out.println("CLIENT : CONNECTED TO : " + destination);
        OutputStream outputstream = socket.getOutputStream();
        Packet packet = new Packet(1, false, source, destination, fileName, false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
        System.out.println("CLIENT : SENDING PING PACKET TO " + destination);
        objectOutputStream.writeObject(packet);
        socket.close();
    }

    public void run() {
        try {
            System.out.println("INSIDE PING");
            this.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
