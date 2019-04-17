

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
     * @return the layout for the search window
     */
    private BorderPane getSearchLayout(){

        BorderPane b = new BorderPane();

        TextField txt = new TextField("Please type your search here");

        Button searchButton = new Button("Search");

        b.setTop(searchButton);
        b.setLeft(txt);


        //create checkboxes and add them to the layout

        CheckBox t1 = new CheckBox(Table.HOSTS.toString());
        CheckBox t2 = new CheckBox(Table.REVIEWS.toString());
        CheckBox t3 = new CheckBox(Table.LISTINGS.toString());          //TODO put labels next to checkboxes
        CheckBox t4 = new CheckBox();
        CheckBox t5 = new CheckBox();
        CheckBox t6 = new CheckBox();

        t1.setLayoutX(20);
        t1.setLayoutY(200);
        t2.setLayoutX(20);
        t2.setLayoutY(220);
        t3.setLayoutX(20);
        t3.setLayoutY(240);
        t4.setLayoutX(20);
        t4.setLayoutY(260);
        t5.setLayoutX(20);
        t5.setLayoutY(280);
        t6.setLayoutX(20);
        t6.setLayoutY(300);



        //prepare list of checkboxes that will match with the list of tables

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



        b.getChildren().addAll(t1,t2,t3,t4,t5,t6);



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


        //scroll panel is in the layout and container is in the scroll panel

        ScrollPane scroll = new ScrollPane();

        Pane container = new Pane();

        scroll.setContent(container);

        b.getChildren().add(scroll);



        table.forEach(t -> {

            //create result table for each table
            TableView tableView = new TableView();


            //will specify which column to search into for each table
            ArrayList<String> columnNames = new ArrayList<>();

            switch (t) {

                case LISTINGS:
                    columnNames.add("Listing_id");

                    break;

                case HOSTS:
                    columnNames.add("Host_id");

                    break;

                case REVIEWS:
                    columnNames.add("Review_id");

                    break;

            }

            //build the query

            String query = "SELECT * FROM  " + table + " WHERE " + text + " IN " +   "("+columnNames.toString()+")";


            //execute statement and insert them into the table view
            try {
                Statement s = sqlConnection.createStatement();

                ResultSet res = s.executeQuery(query);



                //prepare columns

                ArrayList<TableColumn> tableColumns = new ArrayList<>();

                columnNames.forEach(c ->{
                    tableColumns.add(new TableColumn(c)) ;
                });


                //read result and insert data into the table

                while (res.next()) {

                    //get data from result set and put them in corresponding columns

                    tableColumns.forEach(c -> {

                        try {

                            c.setCellValueFactory(new PropertyValueFactory<>(res.getString(c.getText())));

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } );


                    //insert columns in the table view

                    tableView.getColumns().addAll(tableColumns);

                }

                //add table view to the container
                container.getChildren().add(tableView);


            } catch (Exception e) {
                e.printStackTrace();
            }

        });


    }

}



