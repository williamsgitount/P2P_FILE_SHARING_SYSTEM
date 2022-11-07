import java.io.*;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {

        Pong pong_thread = new Pong();
        pong_thread.start();
        pong_thread.join();
    }
}