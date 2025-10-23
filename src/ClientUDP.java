import java.io.File;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientUDP {

    public static void main(String[]args) throws UnknownHostException, SocketException {

        ClientUDP client = new ClientUDP();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter File Path to upload file ");
        String filePath = scanner.nextLine();
        System.out.println("Enter host name(Local host: 127.0.0.1) ");
        String hostName = scanner.nextLine();
        System.out.println("Enter Port number ");
        int portNumber = scanner.nextInt();
        Path clientFilePath = Path.of(filePath);

        try {
            DatagramSocket clientSocket = new DatagramSocket();

                byte[] fileData = Files.readAllBytes(clientFilePath);
                int filelength = fileData.length;
                String lengthMessage = "LEN:" + filelength;
                DatagramPacket lengthPacket = new DatagramPacket(lengthMessage.getBytes(),lengthMessage.length(),InetAddress.getByName(hostName), portNumber);
                clientSocket.send(lengthPacket);


                List<byte[]> dataChunks = client.splitPacket(fileData,1000);
                while (!dataChunks.isEmpty()) {

                    byte[] chunk = dataChunks.getFirst();
                    DatagramPacket dataPacket = new DatagramPacket(chunk, chunk.length, InetAddress.getByName(hostName), portNumber);
                    clientSocket.send(dataPacket);

                    byte[] ackBuffer = new byte[3];
                    DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
                    clientSocket.receive(ackPacket);

                    String cpMessage = new String(ackPacket.getData());
                    System.out.println(cpMessage);
                    if (cpMessage.startsWith("ACK")) {
                        dataChunks.removeFirst();
                    }
                }


                byte[] fpacket = new byte[3];
                DatagramPacket p = new DatagramPacket(fpacket,fpacket.length);
                clientSocket.receive(new DatagramPacket(fpacket,fpacket.length));
                String finMessage = new String(p.getData());
                System.out.println(finMessage);
                if (finMessage.startsWith("FIN")){
                    clientSocket.close();
                }







        } catch (Exception e){
            //do something

        }

    }

    public List<byte[]> splitPacket(byte[] buffer, int N) {
        List<byte[]> packets = new ArrayList<>();

        for (int i = 0; i < buffer.length; i += N) {
            int end = Math.min(buffer.length, i + N);
            byte[] chunk = new byte[end - i];
            System.arraycopy(buffer, i, chunk, 0, end - i);
            packets.add(chunk);
        }

        return packets;
    }


}


