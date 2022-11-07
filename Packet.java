import java.io.Serializable;

public class Packet implements Serializable {

    int packetID;
    Boolean available;
    Boolean needFile;
    String source;
    String destination;
    String fileName;
 
//Packet function to create packet
    Packet(int packetID, Boolean available, String source, String destination, String fileName, Boolean needFile) {
        this.packetID = packetID;
        this.available = available;
        this.source = source;
        this.destination = destination;
        this.fileName = fileName;
        this.needFile = needFile;
    }
}
