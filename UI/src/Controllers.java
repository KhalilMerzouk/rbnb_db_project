import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Contains the methods that control the layout
 */
public abstract class Controllers {



    /**
     * Modify the Panel according to the search
     * @param text the text to perform the search with
     * @param b the panel to modify
     */
    public static void search(String text, BorderPane b) {

        //retrieve checkboxes and convert them into tables

        ArrayList<CheckBox> checks = Utils.getCheckboxFromLayout(b.getChildren());
        ArrayList<Table> table = Utils.getCheckedBoxes(checks);


        //scroll panel is in the layout and container is in the scroll panel

        ScrollPane scroll = new ScrollPane();

        Pane container = new Pane();

        scroll.setContent(container);

        b.setCenter(scroll);



        table.forEach(t -> {



            //will specify which column to search into for each table
            ArrayList<String> columnNames = Utils.getColumnsFromTable(t);

             //TODO remove sysout
            //build the query
            String query = "SELECT * FROM  " +Utils.tableToString(t) + Utils.generateSubstringMatch(text,columnNames);

            System.out.println("Search query ready");

            //launch asynchronous query
            Thread thread = new Thread(() -> {
                System.out.println("Query thread launched");
                //execute statement and insert them into the table view
                ResultSet res = Utils.executeQuery(query);
                System.out.println("Resultset OK");


                //trick to run the update on the UI thread
                Platform.runLater(() -> putResInTable(columnNames, res, container));

            });

            thread.start();


        });


    }


    /**
     * Method used to insert into a panel a tableview filled with a resulset
     * @param columnNames the name of the columns of the tableview
     * @param res the resulset of the executed query
     * @param container the container in which the tableview must be inserted
     */
    public static void putResInTable(List<String> columnNames, ResultSet res, Pane container){      //FIXME data is not correctly displayed in tableviews


        //create result table for each table
        TableView tableView = new TableView();


        //prepare columns
        ArrayList<TableColumn> tableColumns = new ArrayList<>();

        columnNames.forEach(c -> tableColumns.add(new TableColumn(c)));


        //TODO remove sysout
        System.out.println("Try to insert into table");

        //read result and insert data into the table
        try {
            while (res.next()) {                    //TODO need to test this part.. must wait until data are all imported into the DB

                //get data from result set and put them in corresponding columns

                tableColumns.forEach(c -> {

                    try {

                        // those are no proper column names in the DB => retrieve data by column index !!!!
                        if(c.getText().equals("average") || c.getText().equals("average_difference") || c.getText().equals("count")){

                            c.setCellValueFactory(new PropertyValueFactory<>(res.getString(1)));
                        }
                        else {
                            c.setCellValueFactory(new PropertyValueFactory<>(res.getString(c.getText())));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } );


                //insert columns in the table view

                tableView.getColumns().addAll(tableColumns);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        //add table view to the container
        container.getChildren().add(tableView);

        System.out.println("View updated !");
    }



    /**
     * Method to execute the predefined queries and modify the layout accordingly
     * @param query the query to execute
     * @param b the layout Object
     */
    public static void executePredefined(String query, BorderPane b, ArrayList<String> columnNames){

      //create a scrollpane and insert it in the center of the layout
      ScrollPane scroll = new ScrollPane();
      b.setCenter(scroll);

      //the container will contain the tableview and is inserted into the scrollpane
      Pane container = new Pane();
      scroll.setContent(container);


      //launch asynchronous query
      Thread t = new Thread(() -> {

          ResultSet res = Utils.executeQuery(query);

          //trick to run the update on the UI thread
          Platform.runLater(() -> putResInTable(columnNames, res, container));

      });

      t.start();
    }


    /**
     * Method called when trying to insert data to DB
     * @param b layout element
     */
    public static void insertIntoTables(BorderPane b){

        ArrayList<CheckBox> checks = Utils.getCheckboxFromLayout(b.getChildren());

        ArrayList<Table> tables = Utils.getCheckedBoxes(checks);

        tables.forEach(t -> createWindowInsert(t));

    }


    /**
     * Method called when trying to delete data from DB
     * @param b layout element
     */
    public static void deleteFromTable(BorderPane b){

        ArrayList<CheckBox> checks = Utils.getCheckboxFromLayout(b.getChildren());

        ArrayList<Table> tables = Utils.getCheckedBoxes(checks);

        tables.forEach(t -> createWindowDelete(t));

    }


    /**
     * Build the layout for insertion according to the table
     * @param t the table into which the data will be inserted
     */
    public static void createWindowInsert(Table t){
        Stage stage = new Stage();
        stage.setTitle("Insert into "+Utils.tableToString(t));

        //get specific layout for the given table
        Parent root = Layouts.getInsertLayout(t);


        //display the insertion window
        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }

    /**
     * Build the layout for deletion according to the table
     * @param t the table from which data will be deleted
     */
    public static void createWindowDelete(Table t){

        Stage stage = new Stage();
        stage.setTitle("Delete from "+Utils.tableToString(t));


        //select what type of insertion layout to provide
        Parent root =  Layouts.getDeleteLayout(t);

        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }


    /**
     * Method used to insert data into DB
     * @param b layout
     * @param t table in which to perform the insertion
     */
    public static void insertData(BorderPane b, Table t){

        ArrayList<String> data = Utils.getTextFieldsFromLayout(b.getChildren());    //TODO check correctness of input data ?


        String query = Utils.generateInsertQuery(data, t);


        //launch query asynchronously
        Thread thread = new Thread(() -> {
            Utils.executeQuery(query);      //TODO may have to use executeUpdate() => may have to write another function to perform delete, update and insert statements

            System.out.println("Inserton done");
        });


        thread.start();
    }


    /**
     * Method used to delete data from DB
     * @param b layout
     * @param t table from where the data will be deleted
     */
    public static void deleteData(BorderPane b, Table t){

        ArrayList<String> data = Utils.getTextFieldsFromLayout(b.getChildren());    //TODO check correctness of input data ?


        String query = Utils.generateDeleteQuery(data, t);


        //launch query asynchronously
        Thread thread = new Thread(() -> {
            Utils.executeQuery(query);      //TODO may have to use executeUpdate() => may have to write another function to perform delete, update and insert statements

            System.out.println("Deletion done");

        });


        thread.start();
    }


}
