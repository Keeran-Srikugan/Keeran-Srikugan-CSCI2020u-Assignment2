package sample;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileServerClient extends Stage {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;
    private String dowOrUpl;

    ListView<String> listView1;
    ListView<String> listView2;
    Button button1;
    Button button2;

    //we can read this from the user too
    public  static String SERVER_ADDRESS = "localhost";
    public  static int    SERVER_PORT = 16789;

    public FileServerClient() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("Server is not opened yet: "+SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("socket is null");
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
        try {
            String intro = networkIn.readLine();
            System.out.println(intro);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //This is where I call the process class
        networkOut.println("Successfully established connection");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage();
    }

    //This entire function draws the UI
    public void newStage(){

        button1 = new Button("Download");
        button2 = new Button("Upload");

        String[] files = FileServer.getFiles();
        String[] files2 = getFilesClient();

        //Upload files
        listView1 = new ListView<>();
        for (String file : files2) {
            listView1.getItems().addAll(file);
        }
        listView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Download files
        listView2 = new ListView<>();
        for (String file : files) {
            listView2.getItems().addAll(file);
        }
        listView2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        button1.setOnAction(e->{
            //Upload Button
            String fileName = null;
            fileName = listView2.getSelectionModel().getSelectedItem();

            //Check if client file was selected
            if(fileName == null){
                System.out.println("Invalid file chosen");
                System.exit(0);
            }else {
                System.out.println("File being downloaded: "+fileName);
                //Calls the function to connect to the thread and send in the file to upload
                FileServerClientDownload(fileName);
            }
        });

        button2.setOnAction(e->{
            //Upload Button
            String fileName = null;
            fileName = listView1.getSelectionModel().getSelectedItem();

            //Check if server file was selected
            if(fileName == null){
                System.out.println("Invalid file chosen");
                System.exit(0);
            }else {
                System.out.println("File being uploaded: " + fileName);
                //Calls the function ot connect to the thread and send in the file to download
                FileServerClientUpload(fileName);
            }
        });

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(listView1, listView2);


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(button1, button2, splitPane);

        this.setScene(new Scene(layout, 400, 500));
        this.setTitle("File sharer");
        this.show();

    }

    //This function gets the files for the client
    public static String[] getFilesClient(){
        String[] files;
        File root = new File("./input/User");
        files = root.list();
        return files;
    }


    public void FileServerClientDownload(String fileName){
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("Server is not opened yet: "+SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("socket is null");
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
        try {
            String intro = networkIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //This is where I call the process class
        networkOut.println("Download " + fileName);

        File inputFile = new File("./input/User/"+fileName);

        //Here I print out the contents of the file along with sending the information to the server
        System.out.println("Contents of file:");
        System.out.println("(start)");
        try{
            FileWriter writeToFile = new FileWriter(inputFile);
            String line = networkIn.readLine();
            while(line != null){
                System.out.println(line);
                writeToFile.write(line);
                System.out.println("(end)");
                writeToFile.close();
                line = networkIn.readLine();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void FileServerClientUpload(String fileName){
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("Server is not opened yet: "+SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("socket is null");
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }

        //This is where I call the process class
        networkOut.println("Upload " + fileName);

        File inputFile = new File("./input/User/"+fileName);

        //Here I print out the contents of the file along with sending the information to the server
        System.out.println("Contents of file:");
        System.out.println("(start)");
        try {
            Scanner scanner = new Scanner(inputFile);
            while(scanner.hasNext()){
                String token = scanner.nextLine();
                System.out.println(token);
                networkOut.println(token);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //indicated that the file content has ended
        System.out.println("(end)");

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}