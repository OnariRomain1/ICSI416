import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Scanner;
public class Server {


    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Port Number");
        int portNumber = Integer.parseInt(scanner.nextLine());









        /*
        HttpClient client = HttpClient.newHttpClient();
        //put file on
           //
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/upload/file1.txt")).
                header("Content-Type","text/plain").
                PUT(HttpRequest.BodyPublishers.ofFile(Path.of("/Users/onariromain/Documents/Fall 2024/ICSI 412/OS_V0/ICSI416_Web/src/file1.txt"))).
                build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + response.statusCode());
        System.out.println(response.body());
*/
        /*
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            //awaits for the client to connect when it does it binds to the socket
            Socket clientSocket = serverSocket.accept();

            if (clientSocket.isBound()) {
                System.out.println("Connection Established");
                System.out.println(clientSocket.getInetAddress());
                HttpServer.create(new InetSocketAddress(8080), 0);
            }

            while(clientSocket.isBound()){
                //generate put request

            }


        }catch (Exception e){

        }

         */
    }
}
