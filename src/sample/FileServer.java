package sample;
import javafx.scene.control.Button;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class FileServer {
    protected Socket clientSocket           = null;
    protected ServerSocket serverSocket     = null;
    protected FileServerThread[] threads    = null;
    protected int numClients                = 0;
    protected Vector fileList;

    public static int SERVER_PORT = 16789;
    public static int MAX_CLIENTS = 10;

    public FileServer() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("---------------------------");
            System.out.println("The File Server Is Up And Running");
            System.out.println("---------------------------");
            System.out.println("Listening to port: "+SERVER_PORT);
            threads = new FileServerThread[MAX_CLIENTS];
            while(true) {
                clientSocket = serverSocket.accept();
                System.out.println("Client #"+(numClients+1)+" connected.");
                threads[numClients] = new FileServerThread(clientSocket,fileList);
                threads[numClients].start();
                numClients++;

            }
        } catch (IOException e) {
            System.err.println("IOException while creating server connection");
        }

    }

    public static String[] getFiles(){
        String[] files;
        File root = new File("./input/Server");
        files = root.list();
        return files;
    }

    public static void main(String[] args) {
        FileServer app = new FileServer();
    }
}