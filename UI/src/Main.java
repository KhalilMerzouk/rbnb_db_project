

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Main extends Application{

    //declare components for the primary stage

    Button putButton;
    Button getButton;
    Button removeButton;


    public static void main(String[] args){
        launch(args);   //launch the javaFX app (goes to start method)
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //set titles of components during initialization

        primaryStage.setTitle("Welcome to AirBnb");

        putButton = new Button("Put");
        getButton = new Button("Get");
        removeButton = new Button("Remove");


        //prepare panel and put buttons on the panel

        StackPane layout = new StackPane();

        layout.getChildren().add(putButton);
        layout.getChildren().add(getButton);
        layout.getChildren().add(removeButton);
        


        Scene mainScene = new Scene(layout, 1000, 700);      //set scene with window dimension (if not specified => fullscreen)


        //add scene to the stage and displays it
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }




}
