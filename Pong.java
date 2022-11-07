import java.net.*;
import java.io.*;

public class Pong extends Thread{

    public void startServer() throws IOException,ClassNotFoundException{

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(5000);
            socket.setReuseAddress(true);
            while (true) {
                Socket connect = socket.accept();

                System.out.println("SERVER : CONNECTED TO " + connect.getInetAddress());

                ClientHandler clientSock = new ClientHandler(connect);

                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket connect;

        public ClientHandler(Socket connect)
        {
            this.connect = connect;
        }

        public void run()
        {
            try {
                InputStream inputstream = connect.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputstream);
                Packet packet = (Packet) objectInputStream.readObject();

                if (packet != null) {
                    System.out.println("RECEIVED A PING PACKET");

                    if(packet.available == true) {
                        if(packet.needFile == true) {
                            Socket socketC = new Socket(packet.source, 4000);
                            System.out.println("SERVER : CONNECTED TO : " + packet.source);

                            File file = new File(packet.fileName);
                            long length = file.length();
                            byte[] bytes = new byte[16 * 1024];
                            InputStream in = new FileInputStream(file);
                            OutputStream out = socketC.getOutputStream();

                            int count;
                            while ((count = in.read(bytes)) > 0) {
                                out.write(bytes, 0, count);
                            }

                            out.close();
                            in.close();

                            System.out.println("SENDING FILE LENGTH : " + length + " BYTES");
                            socketC.close();
                        }
                        else {
                            packet.needFile = true;
                            Socket socketC = new Socket(packet.destination, 5000);
                            System.out.println("SERVER : CONNECTED TO : " + packet.source);
                            OutputStream outputstream = socketC.getOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
                            System.out.println("SERVER : SENDING A NEED FILE PACKET");
                            objectOutputStream.writeObject(packet);
                            socketC.close();

                            ServerSocket socketF = new ServerSocket(4000);
                            Socket connectF = socketF.accept();

                            try{
                                OutputStream out = null;
                                InputStream in = null;
                                in = connectF.getInputStream();
                                out = new FileOutputStream(packet.fileName);
                                byte[] bytes = new byte[16*1024];

                                int count;
                                while ((count = in.read(bytes)) > 0) {
                                    out.write(bytes, 0, count);
                                }
                                
                                out.close();
                                in.close();

                                System.out.println("FILE DOWNLOADED : " + packet.fileName);

                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }

                            socketF.close();
                        }
                    }
                    else {
                        File file = new File(packet.fileName);
                        if(file.exists())
                        {
                            packet.available = true;
                            Socket socketC = new Socket(packet.source, 5000);
                            System.out.println("SERVER : CONNECTED TO : " + packet.source);
                            OutputStream outputstream = socketC.getOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
                            System.out.println("SERVER : SENDING A FILE FOUND PACKET");
                            objectOutputStream.writeObject(packet);
                            socketC.close();
                        }
                        else
                        {
                            packet.destination = "next_peer_ip";
                            Socket socketC = new Socket(packet.destination, 5000);
                            System.out.println("SERVER : CONNECTED TO : " + packet.source);
                            OutputStream outputstream = socketC.getOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
                            System.out.println("SERVER : SENDING A NEXT PEER PACKET");
                            objectOutputStream.writeObject(packet);
                            socketC.close();
                        }
                    }
                } else {
                    System.out.println("INVALID PING PACKET");
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void run(){
        try{
            System.out.println("INSIDE PONG");
            this.startServer();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
