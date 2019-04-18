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


/**
 * Contains the methods that control the layout
 */
public class Controllers {



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

            //create result table for each table
            TableView tableView = new TableView();


            //will specify which column to search into for each table
            ArrayList<String> columnNames = new ArrayList<>();

            switch (t) {

                case LISTINGS:
                    columnNames.add("listing_id");              //TODO complete list of columns

                    break;

                case HOSTS:
                    columnNames.add("host_id");

                    break;

                case REVIEWS:
                    columnNames.add("review_id");

                    break;

            }

            //build the query

            String query = "SELECT * FROM  " + t.toString() + Utils.GenerateSubstringMatch(text,columnNames);



            //execute statement and insert them into the table view

            ResultSet res = Utils.executeQuery(query);

                //prepare columns

                ArrayList<TableColumn> tableColumns = new ArrayList<>();

                columnNames.forEach(c ->{
                    tableColumns.add(new TableColumn(c)) ;
                });


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



        });


    }



    /**
     * Method to execute the predefined queries and modify the layout accordingly
     * @param query the query to execute
     * @param b the layout Object
     */
    public static void executePredefined(String query, BorderPane b){


        ResultSet res = Utils.executeQuery(query);

        System.out.println(res.toString());

        //TODO modify layout with resulset

    }



}
