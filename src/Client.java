import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

public class Client {

    Protocols currentProtocol;
    /*
    Upload: Copy a file from the client to the server using the put command, which
takes as an input argument the full path to a file <file> on the client. Files uploaded to the server
should be stored in separate directories based on the client IP Address. Upon a successful receipt
of a file, the origin server would send back “File successfully uploaded.” message and close the
connection.
Example execution with prompts on the client:
put <file>
File successfully uploaded.
Download: Copy a file from the server to the client using the get command, which
also takes as an argument the full path to a file <file> on the server.
A control message sent from the server to the client stating “File delivered from server.” should
be displayed to the user.
Example execution with prompts on the client:
get <file>
File delivered from server.
Quit: Close the program per user request.
quit
Command Line Arguments. When starting your client and server, you will need to
specify several command line arguments, as detailed below:
• server – the server will take as command line inputs
(1) a port on which to run
• client – the client will take as command line inputs
(1) the server IP, (2) the server port
     */

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter File Path to upload file ");
        String filePath = scanner.nextLine();
        System.out.println("Enter host name(Local host: 127.0.0.1) ");
        String hostName = scanner.nextLine();
        System.out.println("Enter Port number ");
        int portNumber = scanner.nextInt();


        //System.out.println(CreateUri.
        HttpClient client = HttpClient.newHttpClient();
        Client c = new Client();

        //c.UPLOAD(client,hostName,portNumber,filePath);
        c.Download(client,hostName,portNumber,filePath);
        //c.Download(portNumber,client,filePath);
/*
        StringBuilder downloadUri = new StringBuilder();
        downloadUri.append("http://localhost:");
        downloadUri.append(portNumber);
        downloadUri.append("/upload");
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(downloadUri.toString()))
                .GET() // Specify the GET method
                .build();
        HttpResponse<String> getFile = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("getFile response"+getFile.body());
*/



        /* Working upload
        File newFile = new File("/Users/onariromain/Documents/Fall 2024/ICSI 412/OS_V0/ICSI416_Web/src/file1.txt");
        if (newFile.exists()) {
            System.out.println("File found at path: " + newFile.getAbsolutePath());

            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(new URI(createUri.toString()))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofFile(newFile.toPath()))
                    .build();
            //sends the request while also receiving a response
            HttpResponse<String> uploadFile = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response from server: " + uploadFile.body());
        }else {
            System.out.println("File not Found:" + "User file path: filePath");
        }

         */

    }

    public void Display(){

        DisplayMessage();
        while(true){
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.equals("a")){
                currentProtocol = Protocols.UPLOAD;
            } else if (input.equals("b")) {
                currentProtocol = Protocols.DOWNLOAD;
            }else if (input.equals("quit")) {
                currentProtocol = Protocols.QUIT;
            }

            switch (currentProtocol){
                case UPLOAD:
                   // UPLOAD();
                case DOWNLOAD:
                   // Download();
                case QUIT:
                    break;
            }
        }

    }

    void UPLOAD(HttpClient client,String hostName,int portNumber,String filePath) throws IOException, InterruptedException, URISyntaxException {
        StringBuilder createUri = new StringBuilder();
        createUri.append("http://");
        createUri.append(hostName +":");
        createUri.append(portNumber);
        createUri.append("/upload");

        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("File found at path: " + file.getAbsolutePath() + " "+file.getName());
            System.out.println(createUri.toString());
            HttpRequest putRequest = HttpRequest.newBuilder()

                    .uri(new URI(createUri.toString()))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .setHeader("fileName", file.getName())
                    .setHeader("filePath", filePath)
                    .setHeader("hostName", hostName)
                    .PUT(HttpRequest.BodyPublishers.ofFile(file.toPath()))
                    .build();


            //sends the request while also receiving a response
            HttpResponse<String> uploadFile = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response from server: " + uploadFile.body());
        }else {
            System.out.println("File not Found:" + "User file path: " + filePath);
        }
    }
    void Download(HttpClient client,String hostName,int portNumber,String filePath) throws IOException, InterruptedException {
        StringBuilder downloadUri = new StringBuilder();
        downloadUri.append("http://");
        downloadUri.append(hostName +":");
        downloadUri.append(portNumber);
        downloadUri.append("/download");
        File file = new File(filePath);
        long fileSize = file.length();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(downloadUri.toString()))
                .setHeader("hostName", hostName)
                .setHeader("filePath",file.getPath())
                .setHeader("fileSize", ""+fileSize)
                .GET()
                .build();
        HttpResponse<Path> response = client.send(getRequest, HttpResponse.BodyHandlers.ofFile(Paths.get(file.getName())));
        System.out.println("File delivered from server.");
    }

    void Quit(){
        System.out.println("Exiting....");
    }
    void DisplayMessage(){
        System.out.println("Welcome, \n");
        System.out.println("Which operation would you like to perform");

        System.out.println("A: Upload File");
        System.out.println("(B): Download File");
        System.out.println("Quit");
        System.out.println("____________________");
    }
    /*
    public void UploadFile(HttpClient client,String uri, String filePath) throws IOException, URISyntaxException, InterruptedException {
       //creates a new file and checks if the file exists then using Post
        File newFile = new File(filePath);
        if (newFile.exists()) {
            System.out.println("File found at path: " + newFile.getAbsolutePath());

            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofFile(newFile.toPath()))
                    .build();
            HttpResponse<Path> uploadFile = client.send(putRequest, HttpResponse.BodyHandlers.ofFile(Paths.get("example.html")));
            System.out.println("Response from server: " + uploadFile.body());
        }

    }
*/



    //when a client wants to upload a file call a function that gets the file path and then sends a post request to the server and awaits a response
}