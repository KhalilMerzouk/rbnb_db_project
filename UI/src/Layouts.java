import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public abstract class Layouts {


    /**
     * Setup layout and event handlers for the predefined queries layout
     * @return the layout for the predefined queries window
     */
    public static BorderPane getPredefinedQueriesLayout(){

        BorderPane b = new BorderPane();

        String query1 = "select AVG(CD.PRICE)\n" +
                "from COSTS_DETAILS CD , LISTING L\n" +
                "where L.LISTING_ID = CD.LISTING_ID AND L.LISTING_ID IN (Select M.LISTING_ID\n" +
                "\n" +
                "                                                        from MATERIAL_DESCRIPTION M\n" +
                "where M.BEDROOMS = 8)";

        String query2 = "select AVG(RS.REVIEW_SCORES_CLEANLINESS)\n" +
                "from REVIEWS_SCORES RS, LISTING L\n" +
                "where RS.LISTING_ID = L.LISTING_ID AND L.LISTING_ID IN (select LA.LISTING_ID\n" +
                "                                                        from AMENITIES AM, LISTING_AMENITIES LA\n" +
                "                                                        where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'TV'\n" +
                ")";

        String query3 = "select * \n" +
                "from HOST H\n" +
                "where H.HOST_ID IN (select L.HOST_ID\n" +
                "                    from LISTING L, CALENDAR CA\n" +
                "                    where L.LISTING_ID = CA.LISTING_ID and CA.AVAILABLE = 't' and CA.CALENDAR_DATE >= '01-MAR-19' and CA.CALENDAR_DATE <= '30-SEP-19'\n" +
                ")";

        String query4 = "select COUNT(*)\n" +
                "from LISTING L, HOST H\n" +
                "where L.HOST_ID = H.HOST_ID and H.HOST_ID IN (Select H1.HOST_ID\n" +
                "                      from HOST H1, Host H2\n" +
                "                      where H1.HOST_NAME = H2.HOST_NAME and H1.HOST_ID != H2.HOST_ID\n" +
                ")";

        String query5 = "select CA.CALENDAR_DATE\n" +
                "from CALENDAR CA, LISTING L\n" +
                "where CA.LISTING_ID = L.LISTING_ID and CA.AVAILABLE = 't' and L.LISTING_ID IN (select L1.LISTING_ID\n" +
                "                                                                              from LISTING L1, HOST H\n" +
                "                                                                              where L1.HOST_ID = H.HOST_ID and H.HOST_NAME = 'Viajes Eco'\n" +
                ")";

        String query6 = "select H.HOST_ID, H.HOST_NAME\n" +
                "from HOST H \n" +
                "where H.HOST_ID IN (select L.HOST_ID\n" +
                "                    from LISTING L\n" +
                "                    group by L.HOST_ID having COUNT(*) = 1\n" +
                ")";

        String query7 = "select AVG(CD1.PRICE) - AVG(CD2.PRICE)\n" +
                "from COSTS_DETAILS CD1, COSTS_DETAILS CD2\n" +
                "where CD1.LISTING_ID in (select LA.LISTING_ID\n" +
                "               from AMENITIES AM, LISTING_AMENITIES LA\n" +
                "               where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'Wifi')\n" +
                "                                                        \n" +
                "and CD2.LISTING_ID not in (select LA.LISTING_ID\n" +
                "               from AMENITIES AM, LISTING_AMENITIES LA     \n" +
                "               where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'Wifi')\n";

        String query8 = "select AVG(CD1.PRICE) - AVG(CD2.PRICE)\n" +
                "from COSTS_DETAILS CD1, COSTS_DETAILS CD2\n" +
                "where CD1.LISTING_ID IN (select MD.LISTING_ID\n" +
                "                         from MATERIAL_DESCRIPTION MD                                    \n" +
                "                         where MD.BEDS = 8)\n" +
                "\n" +
                "and CD1.LISTING_ID IN (select L.LISTING_ID\n" +
                "                      from LISTING L\n" +
                "                      where L.CITY = 'Berlin')\n" +
                "                                                        \n" +
                "and CD2.LISTING_ID not in (select MD.LISTING_ID\n" +
                "                         from MATERIAL_DESCRIPTION MD                                    \n" +
                "                         where MD.BEDS = 8)  \n" +
                "\n" +
                "and CD2.LISTING_ID IN (select L.LISTING_ID\n" +
                "                      from LISTING L\n" +
                "where L.CITY = 'Madrid') ";

        String query9 = "select * from \n" +
                "  (select H.HOST_ID , H.HOST_NAME\n" +
                "    from LISTING L, HOST H\n" +
                "    where L.COUNTRY = 'Spain' and L.HOST_ID = H.HOST_ID\n" +
                "    group by L.HOST_ID, H.HOST_NAME, H.HOST_ID\n" +
                "    order by COUNT(*) DESC)\n" +
                "    \n" +
                "    where rownum <= 10\n" +
                "\n";

        String query10 = "select * from \n" +
                "(select L.LISTING_ID, L.LISTING_NAME\n" +
                "from LISTING L, REVIEWS_SCORES RS\n" +
                "where L.LISTING_ID = RS.LISTING_ID \n" +
                "  and L.LISTING_ID IN (select L1.LISTING_ID\n" +
                "                      from LISTING L1, MATERIAL_DESCRIPTION MD\n" +
                "                      where L.CITY = 'Barcelona' and MD.LISTING_ID = L.LISTING_ID and MD.PROPERTY_TYPE = 'Apartment')\n" +
                "order by RS.REVIEW_SCORES_RATING DESC)\n" +
                "where rownum <=10";


        Button q1 = new Button("Q1");
        Button q2 = new Button("Q2");
        Button q3 = new Button("Q3");
        Button q4 = new Button("Q4");
        Button q5 = new Button("Q5");
        Button q6 = new Button("Q6");
        Button q7 = new Button("Q7");
        Button q8 = new Button("Q8");
        Button q9 = new Button("Q9");
        Button q10 = new Button("Q10");


        q1.setLayoutX(40);
        q1.setLayoutY(100);
        ArrayList<String> columnNames1 = new ArrayList<>();
        columnNames1.add("average");
        q1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query1, b, columnNames1));

        q2.setLayoutX(40);
        q2.setLayoutY(120);
        ArrayList<String> columnNames2 = new ArrayList<>();
        columnNames2.add("average");
        q2.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query2, b, columnNames2));

        q3.setLayoutX(40);
        q3.setLayoutY(140);
        ArrayList<String> columnNames3 = new ArrayList<>();
        columnNames3.add("host_id");
        columnNames3.add("host_url");
        columnNames3.add("host_name");
        columnNames3.add("host_since");
        columnNames3.add("host_thumbnail_url");
        q3.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query3, b, columnNames3));

        q4.setLayoutX(40);
        q4.setLayoutY(160);
        ArrayList<String> columnNames4 = new ArrayList<>();
        columnNames4.add("count");
        q4.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query4, b, columnNames4));

        q5.setLayoutX(40);
        q5.setLayoutY(180);
        ArrayList<String> columnNames5 = new ArrayList<>();
        columnNames5.add("calendar_date");
        q5.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query5, b, columnNames5));

        q6.setLayoutX(40);
        q6.setLayoutY(200);
        ArrayList<String> columnNames6 = new ArrayList<>();
        columnNames6.add("host_id");
        columnNames6.add("host_name");
        q6.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query6, b, columnNames6));

        q7.setLayoutX(40);
        q7.setLayoutY(220);
        ArrayList<String> columnNames7 = new ArrayList<>();
        columnNames7.add("average_difference");
        q7.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query7, b, columnNames7));

        q8.setLayoutX(40);
        q8.setLayoutY(240);
        ArrayList<String> columnNames8 = new ArrayList<>();
        columnNames8.add("average_difference");
        q8.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query8, b, columnNames8));

        q9.setLayoutX(40);
        q9.setLayoutY(260);
        ArrayList<String> columnNames9 = new ArrayList<>();
        columnNames9.add("host_id");
        columnNames9.add("host_name");
        q9.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query9, b, columnNames9));

        q10.setLayoutX(40);
        q10.setLayoutY(280);
        ArrayList<String> columnNames10 = new ArrayList<>();
        columnNames10.add("listing_id");
        columnNames10.add("listing_name");
        q10.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query10, b, columnNames10));


        //add the buttons to the layout

        VBox box = new VBox();
        box.setSpacing(10);

        box.getChildren().addAll(q1,q2,q3,q4,q5,q6,q7,q8,q9,q10);

        b.setLeft(box);

        return b;

    }



    /**
     * Setup layout and event handlers for the insert/delete layout
     * @return the layout for the insert/delete window
     */
    public static BorderPane getInsertDeleteLayout(){

        BorderPane b = new BorderPane();

        //create buttons
        Button insert = new Button("Insert");
        Button delete = new Button("Delete");

        //insert buttons in a horizontal box
        HBox box = new HBox();
        box.getChildren().addAll(insert, delete);
        box.setSpacing(20);

        //insert into layout the checkboxes to choose table in which to insert/delete
        getTableCheckbox(b);


        insert.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {Controllers.insertIntoTables(b);});

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {Controllers.deleteFromTable(b);});

        b.setTop(box);

        return b;
    }


    /**
     * Setup layout and event handlers for the search layout
     * @return the layout for the search window
     */
    public static BorderPane getSearchLayout(){

        BorderPane b = new BorderPane();

        TextField txt = new TextField("Please type your search here");

        Button searchButton = new Button("Search");

        b.setTop(searchButton);
        b.setLeft(txt);

        //put checkboxes in the layout
        getTableCheckbox(b);

        //call controler's procedure if search button is clicked
        searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.search(txt.getText(), b) );


        return b;
    }


    /**
     * Method to put checkboxes to choose which table to perform a query on in the layout
     * @param b a borderpane in which the checkboxes will be inserted
     */
    public static void getTableCheckbox(BorderPane b){


        //create checkboxes, labels and add them to the layout

        HBox h1 = new HBox();
        HBox h2 = new HBox();
        HBox h3 = new HBox();
        HBox h4 = new HBox();
        HBox h5 = new HBox();
        HBox h6 = new HBox();


        CheckBox t1 = new CheckBox(Utils.tableToString(Table.HOST));
        CheckBox t2 = new CheckBox(Utils.tableToString(Table.REVIEWS));
        CheckBox t3 = new CheckBox(Utils.tableToString(Table.LISTING));
        CheckBox t4 = new CheckBox();                                   //TODO add other tables
        CheckBox t5 = new CheckBox();
        CheckBox t6 = new CheckBox();


        t1.setMinWidth(300);
        t2.setMinWidth(300);
        t3.setMinWidth(300);
        t4.setMinWidth(300);
        t5.setMinWidth(300);
        t6.setMinWidth(300);

        h1.getChildren().addAll(t1,t2,t3,t4,t5,t6);
        h2.getChildren().addAll(t2);
        h3.getChildren().addAll(t3);
        h4.getChildren().addAll(t4);
        h5.getChildren().addAll(t5);
        h6.getChildren().addAll(t6);

        h1.setLayoutX(20);
        h1.setLayoutY(200);
        h2.setLayoutX(20);
        h2.setLayoutY(220);
        h3.setLayoutX(20);
        h3.setLayoutY(240);
        h4.setLayoutX(20);
        h4.setLayoutY(260);
        h5.setLayoutX(20);
        h5.setLayoutY(280);
        h6.setLayoutX(20);
        h6.setLayoutY(300);

        b.getChildren().addAll(h1,h2,h3,h4,h5,h6);

    }


    /**
     * Build layout for insertion given a table
      * @param t table in which to perform the insertion
     * @return a layout for the insertion window
     */
    public static BorderPane getInsertLayout(Table t){


        BorderPane b = new BorderPane();

        Button insert = new Button("Insert "+Utils.tableToString(t));

        setupQuerySubmissionLayout(b, insert, Utils.getColumnsFromTable(t));

        insert.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.insertData(b, t));

        return b;
    }


    /**
     * Build layout for data deletion
     * @return the layout element for data deletion
     */
    public static Parent getDeleteLayout(Table t){

        BorderPane b = new BorderPane();

        Button delete = new Button("Delete "+Utils.tableToString(t));

        setupQuerySubmissionLayout(b, delete, Utils.getColumnsFromTable(t));

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.deleteData(b, t));


        return b;
    }


    /**
     * Build a layout used for form submission (insert and delete)
     * @param b teh layout
     * @param submitButton the button used for submission
     * @param textFields the list of textfields that will appear in the layout
     */
    public static void setupQuerySubmissionLayout(BorderPane b, Button submitButton, ArrayList<String> textFields){


        HBox hBox = new HBox();
        hBox.getChildren().add(submitButton);

        b.setBottom(hBox);

        VBox box = new VBox();
        b.setCenter(box);

        //TODO auto-generate listing-id ??????

        //generate textFields
        ArrayList<TextField> fields = new ArrayList<>();
        textFields.forEach(t -> fields.add(new TextField(t)));

        //add them into the layout
        box.getChildren().addAll(fields);
        box.setSpacing(15);

    }

}
