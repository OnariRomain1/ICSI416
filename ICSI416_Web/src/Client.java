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
import java.util.concurrent.CompletableFuture;

public class Client {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter host name ");
        String hostName = scanner.nextLine();
        int portNumber = scanner.nextInt();

        try {
            Socket server = new Socket(hostName ,portNumber);
            if (server.isBound()){
                System.out.println(server.getInetAddress());

            }
            System.out.println("Enter File Path");
            String filePath = scanner.nextLine();

            HttpClient client = HttpClient.newHttpClient();
            //put file on
            //
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create("http://localhost:8080/upload/file1.txt")).
                    header("Content-Type","text/plain").
                    PUT(HttpRequest.BodyPublishers.ofFile(Path.of(filePath))).
                    build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(System.out::println);

            while(server.isBound()){

            }
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        //awaits for the client to connect when it does it binds to the socket


    }
}
