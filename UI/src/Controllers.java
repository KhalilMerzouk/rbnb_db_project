import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        b.getChildren().add(scroll);



        table.forEach(t -> {



            //will specify which column to search into for each table
            ArrayList<String> columnNames = new ArrayList<>();

            switch (t) {

                case LISTING:
                    columnNames.add("listing_id");
                    columnNames.add("listing_url");
                    columnNames.add("listing_name");
                    columnNames.add("listing_summary");
                    columnNames.add("picture_url");
                    columnNames.add("country");
                    columnNames.add("city");
                    break;

                case HOST:
                    columnNames.add("host_id");
                    columnNames.add("host_url");
                    columnNames.add("host_name");
                    columnNames.add("host_since");
                    columnNames.add("host_thumbnail_url");

                    break;

                case REVIEWS:
                    columnNames.add("review_id");
                    columnNames.add("listing_id");
                    columnNames.add("reviewer_id");
                    columnNames.add("comments");
                    columnNames.add("review_date");
                    break;

            }
        //TODO remove sysout
            //build the query
            String query = "SELECT * FROM  " +Utils.TableToString(t) + Utils.GenerateSubstringMatch(text,columnNames);

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
    public static void putResInTable(List<String> columnNames, ResultSet res, Pane container){


        //create result table for each table
        TableView tableView = new TableView();


        //prepare columns
        ArrayList<TableColumn> tableColumns = new ArrayList<>();

        columnNames.forEach(c ->{
            tableColumns.add(new TableColumn(c)) ;
        });
        //TODO remove sysout
        System.out.println("Try to insert into table");

        //read result and insert data into the table
        try {
            while (res.next()) {                    //TODO need to test this part.. must wait until data are all imported into the DB

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



}
