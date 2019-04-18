import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class Layouts {


    /**
     * Setup layout and event handlers for the predefined queries layout
     * @return the layout for the predefined queries window
     */
    public static BorderPane getPredefinedQueriesLayout(){

        BorderPane b = new BorderPane();



        return b;

    }



    /**
     * Setup layout and event handlers for the insert/delete layout
     * @return the layout for the insert/delete window
     */
    public static BorderPane getInsertDeleteLayout(){

        BorderPane b = new BorderPane();


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


        //create checkboxes, labels and add them to the layout

        HBox h1 = new HBox();
        HBox h2 = new HBox();
        HBox h3 = new HBox();
        HBox h4 = new HBox();
        HBox h5 = new HBox();
        HBox h6 = new HBox();




        CheckBox t1 = new CheckBox(Utils.TableToString(Table.HOSTS));
        CheckBox t2 = new CheckBox(Utils.TableToString(Table.REVIEWS));
        CheckBox t3 = new CheckBox(Utils.TableToString(Table.LISTINGS));
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


        //prepare list of checkboxes that will match with the list of tables



        searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.search(txt.getText(), b) );



        b.getChildren().addAll(h1,h2,h3,h4,h5,h6);



        return b;
    }






}
