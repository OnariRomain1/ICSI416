import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.CopyOption;
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
    //works but i need to figure out a way to use different host names and store the uploaded files
    static class UploadHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String hostname = exchange.getRequestHeaders().get("hostName").getFirst();
            String fileName = exchange.getRequestHeaders().get("fileName").getFirst();

            Path dir = Path.of("uploads", hostname);
            Files.createDirectories(dir);

            Path savePath = dir.resolve(fileName);

            try (InputStream clientBody = exchange.getRequestBody()) {
                Files.copy(clientBody, savePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }


            String response = "File uploaded successfully!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }
//fix the download handler
    static class DownloadHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //so does th
            String request = exchange.getRequestMethod();
            if (request.equals("GET")){
                //System.out.println("This is a get request, retrive the file ");
                String hostName = exchange.getRequestHeaders().get("hostName").getFirst();
                String filePath = exchange.getRequestHeaders().get("filePath").getFirst();
                String fileName =exchange.getRequestHeaders().get("fileName").getFirst();
                //use the file name and search in the hostanme directory then copy the file

                Path uploadDir = Path.of("uploads", hostName);
                File uploadDir_ = uploadDir.toFile();

                Path downloadDir = Path.of("downloads", hostName);
                Files.createDirectories(downloadDir);
                Path foundFile = uploadDir.resolve(fileName);
                if (Files.exists(foundFile)){
                    long fileSize = Files.size(foundFile);
                    exchange.sendResponseHeaders(200, fileSize);
                    try (OutputStream out = exchange.getResponseBody()) {
                        Files.copy(foundFile, out);
                    }
                    Files.copy(foundFile, downloadDir.resolve(fileName), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    exchange.close();
                } else{
                    String response = "File not found for download.";
                    exchange.sendResponseHeaders(404, response.length());
                    try (OutputStream out = exchange.getResponseBody()) {
                        out.write(response.getBytes());
                    }
                    exchange.close();
                }

                String response = "File delivered from server";
                //String fileSize = exchange.getResponseHeaders().get("fileSize").getFirst();
                //System.out.println(fileSize);

            }

            //Files.copy(clientBody,Paths.get("uploaded1_file.html"));




        }
    }
}


