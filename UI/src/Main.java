

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application{

    //enum for all tables
    enum Table{LISTINGS, HOSTS, REVIEWS, NONE}

    //declare components for the primary stage

    private Button insertDeleteButton;
    private Button searchButton;
    private Button predefinedQueriesButton;

    private Stage window;

    private Scene insertDeleteScene;
    private Scene searchScene;
    private Scene predefinedQueriesScene;




    public static void main(String[] args){
        launch(args);   //launch the javaFX app (goes to start method)
    }



    @Override
    public void start(Stage primaryStage) throws Exception {


        window = primaryStage;

        //set titles of components during initialization

        primaryStage.setTitle("Welcome to AirBnb");

        insertDeleteButton = new Button("Insert/Delete");
        searchButton = new Button("Search");
        predefinedQueriesButton = new Button("Predefined Queries");



        //switch scene if button is clicked

        insertDeleteButton.setOnAction(e -> window.setScene(insertDeleteScene));
        searchButton.setOnAction(e -> window.setScene(searchScene));
        predefinedQueriesButton.setOnAction(e -> window.setScene(predefinedQueriesScene));



        //prepare panels


        BorderPane searchLayout = Layouts.getSearchLayout();
        BorderPane insertDeleteLayout = Layouts.getInsertDeleteLayout();
        BorderPane predefinedQueriesLayout = Layouts.getPredefinedQueriesLayout();

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(15);


        //add buttons to the panel

        hbox.getChildren().addAll(insertDeleteButton, searchButton, predefinedQueriesButton);


        //setup scenes

        Scene mainScene = new Scene(hbox, 1000, 700);      //set scene with window dimension (if not specified => fullscreen)
        insertDeleteScene = new Scene(insertDeleteLayout, 1000, 700);
        searchScene = new Scene(searchLayout, 1000, 700);
        predefinedQueriesScene = new Scene(predefinedQueriesLayout, 1000, 700);



        //add scene to the stage and displays it
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }




}



