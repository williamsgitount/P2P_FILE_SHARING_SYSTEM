import java.util.*;

public class Menu extends Thread {
    public void run() {
        try {
            Scanner sc = new Scanner(System.in);

            while(true)
            {
                System.out.println();
                System.out.println("1. REQUEST A FILE");
                System.out.println("0. EXIT");
                int choice = sc.nextInt();
                sc.nextLine();
                if(choice == 1)
                {
                    System.out.print("ENTER FILE NAME : ");
                    String fileName = sc.nextLine();
                    System.out.print("FILE NAME : " + fileName);
                    System.out.println();
                    Ping ping_thread = new Ping(fileName, "dest_ip", "src_ip");
                    ping_thread.start();
                    ping_thread.join();
                }
                while(true)
                {
                    System.out.println("sending file!");
                }
                else if(choice == 0)
                {
                    System.exit(0);
                }
                else
                {
                    System.out.println("INVALID CHOICE !");
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }    
}
