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
import java.util.StringTokenizer;

public class printUI extends Stage {
    private static String filePath;
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

    public printUI(String filePath) {
        this.filePath = filePath;
    }

    //This is where I draw the UI
    public void newStage(){

        button1 = new Button("Upload");
        button2 = new Button("Download");

        String[] files = FileServer.getFiles();
        String[] files2 = getFilesClient();

        //Upload files
        listView1 = new ListView<>();
        listView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for (String file : files2) {
            listView1.getItems().addAll(file);
        }

        //Download files
        listView2 = new ListView<>();
        for (String file : files) {
            listView2.getItems().addAll(file);
        }
        listView2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(listView1, listView2);


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(button1, button2, splitPane);

        this.setScene(new Scene(layout, 400, 500));
        this.setTitle("File sharer");
        this.show();


        button1.setOnAction(e->{
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(actionEvent1 -> {
                //Upload Button
                String fileName = null;
                fileName = listView1.getSelectionModel().getSelectedItem();
                dowOrUpl = "Upload";
            });
            pause.play();
        });

        button2.setOnAction(e->{
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(actionEvent1 -> {
                //Upload Button
                String fileName = null;
                fileName = listView1.getSelectionModel().getSelectedItem();
                dowOrUpl = "Upload";
            });
            pause.play();
        });

    }

    //Gets the files for the client
    public static String[] getFilesClient(){
        String[] files;
        File root = new File(filePath);
        files = root.list();
        return files;
    }


}