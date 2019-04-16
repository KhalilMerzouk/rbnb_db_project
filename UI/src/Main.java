

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


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

    private Connection sqlConnection;



    public static void main(String[] args){
        launch(args);   //launch the javaFX app (goes to start method)
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        //initialize DB connection
        sqlConnection = getConnection();


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


        BorderPane searchLayout = getSearchLayout();
        BorderPane insertDeleteLayout = getInsertDeleteLayout();
        BorderPane predefinedQueriesLayout = getPredefinedQueriesLayout();

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


    /**
     * Initialize DB connection
     * @return the database connection's instance
     */
    private Connection getConnection(){


        //will only work with a VPN connection to EPFL's network ;)

        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@cs322-db.epfl.ch:1521:ORCLCDB";
        String username = "C##DB2019_G26";
        String password = "DB2019_G26";


        Connection connection = null;

        try {
            // make the connection
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);

        } catch(Exception e) {
           e.printStackTrace();
        }

        return connection;

    }


    private BorderPane getPredefinedQueriesLayout(){

        BorderPane b = new BorderPane();



        return b;

    }




    private BorderPane getInsertDeleteLayout(){

        BorderPane b = new BorderPane();


        return b;
    }


    /**
     * Setup layout and event handlers for the search layout
     * @return
     */
    private BorderPane getSearchLayout(){

        BorderPane b = new BorderPane();

        TextField txt = new TextField("Please type your search here");

        Button searchButton = new Button("Search");

        TableView t = new TableView();

        CheckBox t1 = new CheckBox(Table.HOSTS.toString());
        CheckBox t2 = new CheckBox(Table.REVIEWS.toString());
        CheckBox t3 = new CheckBox(Table.LISTINGS.toString());
        CheckBox t4 = new CheckBox();
        CheckBox t5 = new CheckBox();
        CheckBox t6 = new CheckBox();

        ArrayList<Table> tables = new ArrayList<>();
        ArrayList<CheckBox> checks = new ArrayList<>();

        checks.add(t1);
        checks.add(t2);
        checks.add(t3);
        checks.add(t4);
        checks.add(t5);
        checks.add(t6);

        checks.removeIf(e -> !e.isSelected());

        for(int i = 0; i < checks.size();++i){

            tables.add(StringToTable(checks.get(i).getText()));

        }


        searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED,e -> search(txt.getText(), b, tables) );

        return b;
    }


    /**
     * Method to covert fromm Strong to Table enum
     * @param s the string to convert
     * @return the table corresponding to the string
     */
    private Table StringToTable(String s){

        switch(s){

            case "LISTINGS":
                return Table.LISTINGS;
            case "HOSTS":
                return Table.HOSTS;
            case "REVIEWS":
                return Table.REVIEWS;

        }

        return Table.NONE;

    }


    /**
     * Modify the Panel according to the search
     * @param text the text to perform the search with
     * @param b the panel to modify
     */
    private void search(String text, BorderPane b, ArrayList<Table> table) {


        table.forEach(t -> {

            //will specify which column to search into for each table
            String columns = null;

            switch (t) {

                case LISTINGS:
                    columns = "()";
                    break;

                case HOSTS:
                    columns = "()";
                    break;

                case REVIEWS:
                    columns = "()";
                    break;

            }

            String query = "SELECT * FROM  " + table + " WHERE " + text + " IN " + columns;


            try {
                Statement s = sqlConnection.createStatement();

                ResultSet res = s.executeQuery(query);

                //add data into panel

                while (res.next()) {


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        });


    }

}



