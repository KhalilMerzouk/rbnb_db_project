

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * Entry point for the whole program. The main page is designed here
 */

public class Main extends Application{



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

        //insert Back button

        Button backFromSearch = new Button("Back");
        backFromSearch.setOnAction(e -> window.setScene(MyApplication.mainScene));

        Button backFromInsert = new Button("Back");
        backFromInsert.setOnAction(e -> window.setScene(MyApplication.mainScene));

        Button backFromPredefined = new Button("Back");
        backFromPredefined.setOnAction(e -> window.setScene(MyApplication.mainScene));

        searchLayout.setTop(backFromSearch);
        insertDeleteLayout.setTop(backFromInsert);
        predefinedQueriesLayout.setTop(backFromPredefined);


        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(15);


        //add buttons to the panel

        hbox.getChildren().addAll(insertDeleteButton, searchButton, predefinedQueriesButton);


        //setup scenes

        MyApplication.mainScene = new Scene(hbox, 1000, 700);      //set scene with window dimension (if not specified => fullscreen)
        insertDeleteScene = new Scene(insertDeleteLayout, 1000, 700);
        searchScene = new Scene(searchLayout, 1000, 700);
        predefinedQueriesScene = new Scene(predefinedQueriesLayout, 1000, 700);



        //add scene to the stage and displays it
        primaryStage.setScene(MyApplication.mainScene);
        primaryStage.show();

    }




}



