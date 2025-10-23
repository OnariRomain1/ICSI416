import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ServerUDP {
    public static void main(String[]args) throws IOException {
        ServerUDP server = new ServerUDP();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Port number ");
        int portNumber = scanner.nextInt();
        DatagramSocket socket = new DatagramSocket(portNumber);
        int currentBytes =0;
        int fileSize_ =0;
        byte[] targetFile = new byte[0];
        while (true){
            byte[] serverBuffer = new byte[1000];
            DatagramPacket packet = new DatagramPacket(serverBuffer,serverBuffer.length);

            socket.receive(packet);
            InetAddress clientAddress = packet.getAddress();

           String message = new String(packet.getData());
           //System.out.println("Message: "+ message);

           if (message.startsWith("LEN:")){
               StringBuilder fileSize = new StringBuilder();
               fileSize_ = server.parseFileLengthMessage(4,message,fileSize);
             // System.out.println(fileSize.toString());
               targetFile = new byte[fileSize_];
           }
               currentBytes += packet.getLength();
               System.out.println("CurrentBytes: " +currentBytes);
            if (currentBytes >= fileSize_){
                System.out.println("Finished recieving data");
                String finalMsg = "FIN";
                DatagramPacket finishedMsg = new DatagramPacket(finalMsg.getBytes(),finalMsg.length(),clientAddress, packet.getPort());
                socket.send(finishedMsg);
                //add to the directory depending on what the user chooses: Get/Put
                Files.write(Paths.get("file.txt"), targetFile);
            } else {
                System.arraycopy(packet.getData(), 0, targetFile, currentBytes - packet.getLength(), packet.getLength());

                String reciveMsg = "ACK:";
                DatagramPacket ackP = new DatagramPacket(reciveMsg.getBytes(), reciveMsg.length(), clientAddress, packet.getPort());
                socket.send(ackP);


            }



        }

    }

    int parseFileLengthMessage(int start, String message, StringBuilder buffer){
        for (int i =start; i < message.length();i++){
            if (message.charAt(i) == '\0' || message.charAt(i) == '\n') {
                System.out.println(buffer.toString());
                break;
            }
            buffer.append(message.charAt(i));
        }
        return Integer.parseInt(buffer.toString());
    }
    void parseMessage(int start, String message, StringBuilder buffer){
        for (int i =start; i < message.length();i++){
            if (message.charAt(i) == '\0' || message.charAt(i) == '\n') {
                break;
            }
            buffer.append(message.charAt(i));
        }

    }

    public void Recieve(){

    }
}
