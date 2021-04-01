package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    Button button1;
    Button button2;

    @Override
    public void start(Stage primaryStage) throws Exception{

        button1= new Button("Upload");
        button2= new Button("Download");

        ListView<String> listView1  = new ListView<>();
        listView1.getItems().addAll("Hello");
        listView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ListView<String> listView2  = new ListView<>();
        listView2.getItems().addAll("Hello","Titanic");
        listView2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(listView1,listView2);


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(button1,button2,splitPane);

        Scene scene = new Scene(layout,400,500);
        primaryStage.setScene(scene);





        primaryStage.setTitle("JavaFX App");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        FileServerClient client = new FileServerClient();
    }
}
