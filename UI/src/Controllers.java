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


/**
 * Contains the methods that control the layout
 */
public class Controllers {



    /**
     * Modify the Panel according to the search
     * @param text the text to perform the search with
     * @param b the panel to modify
     */
    public static void search(String text, BorderPane b, ArrayList<Main.Table> table) {


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

            String query = "SELECT * FROM  " + t.toString() + Utils.GenerateSubstringMatch(text,columnNames);


            //execute statement and insert them into the table view
            try {
                Statement s = MyApplication.getDBConnection().createStatement();

                ResultSet res = s.executeQuery(query);



                //prepare columns

                ArrayList<TableColumn> tableColumns = new ArrayList<>();

                columnNames.forEach(c ->{
                    tableColumns.add(new TableColumn(c)) ;
                });


                //read result and insert data into the table

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

                //add table view to the container
                container.getChildren().add(tableView);


            } catch (Exception e) {
                e.printStackTrace();
            }

        });


    }



}
