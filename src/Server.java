import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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


    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Port Number");
        int portNumber = Integer.parseInt(scanner.nextLine());

        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/upload", new UploadHandler());
        server.createContext("/download", new DownloadHandler());
        server.setExecutor(null);
        server.start();
    }

    static class UploadHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //so does th
            InputStream clientBody = exchange.getRequestBody();
            Files.copy(clientBody,Paths.get("uploaded1_file.html"));

            String response = "File uploaded successfully!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }

    static class DownloadHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //so does th
            String request = exchange.getRequestMethod();
            if (request.equals("GET")){
                //System.out.println("This is a get request, retrive the file ");
                String filePath = exchange.getRequestHeaders().get("filePath").getFirst();
                Path file = Path.of(filePath);
                long filesize = Files.size(file);
                OutputStream out = exchange.getResponseBody();
                exchange.sendResponseHeaders(200,filesize);
                Files.copy(file,out);
                out.close();



                //String response = "This is a get request, retrieving file" + buffer.length;
                //String fileSize = exchange.getResponseHeaders().get("fileSize").getFirst();
                //System.out.println(fileSize);

            }

            //Files.copy(clientBody,Paths.get("uploaded1_file.html"));



            exchange.close();
        }
    }
}


