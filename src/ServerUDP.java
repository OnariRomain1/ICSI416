import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
        while (true){
            byte[] serverBuffer = new byte[1000];
            DatagramPacket packet = new DatagramPacket(serverBuffer,serverBuffer.length);
            socket.receive(packet);

           String message = new String(packet.getData());
           //System.out.println("Message: "+ message);

           if (message.startsWith("LEN:")){
               StringBuilder fileSize = new StringBuilder();
               fileSize_ = server.parseFileLengthMessage(4,message,fileSize);
             // System.out.println(fileSize.toString());
           }

           if (message.startsWith("SEND:")) {
               StringBuilder sendMessage = new StringBuilder();
               server.parseMessage(5,message,sendMessage);
               System.out.println(sendMessage.toString());
               currentBytes += Integer.parseInt(sendMessage.toString());
               System.out.println("CurrentBytes: " +currentBytes);
               String reciveMsg = "ACK:";
               InetAddress clientAddress = packet.getAddress();
               DatagramPacket ackP = new DatagramPacket(reciveMsg.getBytes(),reciveMsg.length(),clientAddress, packet.getPort());
               socket.send(ackP);

                if (currentBytes >= fileSize_){
                    System.out.println("Finished recieving data");
                    String finalMsg = "FIN";
                    DatagramPacket finishedMsg = new DatagramPacket(finalMsg.getBytes(),finalMsg.length(),clientAddress, packet.getPort());
                    socket.send(finishedMsg);
                }

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
