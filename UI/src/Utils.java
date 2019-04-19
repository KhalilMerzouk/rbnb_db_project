import java.sql.*;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Bunch of useful methods used in Main
 */

public abstract class Utils {


    /**
     * Generate a part of the query for substring match
     * @param search
     * @param columns
     * @return
     */
    public static String generateSubstringMatch(String search, List<String> columns){


        StringBuilder sb = new StringBuilder();

        sb.append(" WHERE");

        columns.forEach(c ->{

            sb.append(" "+c+" LIKE '%"+search+"%'  or");


        });

        String res = sb.toString();

        return res.substring(0, res.length() -3);

    }



    /**
     * Method to covert from String to Table enum
     * @param s the string to convert
     * @return the table corresponding to the string
     */
    public static Table stringToTable(String s){

        switch(s){

            case "LISTING":
                return Table.LISTING;
            case "HOST":
                return Table.HOST;
            case "REVIEWS":
                return Table.REVIEWS;

        }

        return Table.NONE;

    }


    /**
     * Method to covert from Table enum to string
     * @param t the string to convert
     * @return the table corresponding to the string
     */
    public static String tableToString(Table t){

        switch(t){

            case LISTING:
                return "LISTING";
            case HOST:
                return "HOST";
            case REVIEWS:
                return "REVIEWS";

        }

        return "NONE";

    }




    /**
     * Initialize DB connection
     * @return the database connection's instance
     */
    public static Connection getConnection(){


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


    /**
     * Get list of tables from list of checkboxes
     * @param checks the list of checkboxes with names corresponding to table names
     * @return the list of tables that are selected for the query
     */
    public static ArrayList<Table> getCheckedBoxes(ArrayList<CheckBox> checks){


        ArrayList<Table> tables = new ArrayList<>();


        checks.removeIf(e -> !e.isSelected());

        for(int i = 0; i < checks.size();++i){

            tables.add(Utils.stringToTable(checks.get(i).getText()));

        }

        return tables;

    }


    /**
     * Method to retrieve all Checkboxes from a list of Nodes
     * @param nodes the list of nodes (can get it with Panel.getChildren)
     * @return a list of checkboxes
     */
    public static ArrayList<CheckBox> getCheckboxFromLayout(ObservableList<Node> nodes){


        ArrayList<CheckBox> checks = new ArrayList<>();

        nodes.forEach(n -> {

            if(n instanceof CheckBox){
                checks.add((CheckBox) n);
            }
            else if (n instanceof HBox){
                checks.addAll(getCheckboxFromLayout(((HBox) n).getChildren()));
            }

        });

        return checks;

    }


    /**
     * Method to execute queries and return the resultset
     * @param query the query to execute
     * @return teh resultset of the given query
     */
    public static ResultSet executeQuery(String query){


        ResultSet res = null;

        try {
            Statement s = MyApplication.getDBConnection().createStatement();
            System.out.println("Executing query...");            //TODO remove sysout
            res = s.executeQuery(query);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return res;
    }


    /**
     * Method to retrieve content of textfield even inside a VBox
     * @param nodes the list of nodes returned by a getChildren method
     * @return the content of the textfield inside an arraylist of strings
     */
    public static ArrayList<String> getTextFieldsFromLayout(ObservableList<Node> nodes){

        ArrayList<String> textFields = new ArrayList<>();

        nodes.forEach(child -> {

            if(child instanceof TextField){
                textFields.add(((TextField) child).getText());
            }
            else if(child instanceof VBox){

                textFields.addAll(getTextFieldsFromLayout(((VBox)child).getChildren()));
            }

        });

        return textFields;
    }


    /**
     * Get the column names of a given table
     * @param t the table
     * @return an arraylist containing the column names
     */
    public static ArrayList<String> getColumnsFromTable(Table t){

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

        return columnNames;

    }


    /**
     * Generate an insert query given a table and all the values
     * @param data the values for all the column of the given table
     * @param t the table in whhich we will insert the data
     * @return the query as a string
     */
    public static String generateInsertQuery(ArrayList<String> data, Table t){

        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO "+Utils.tableToString(t)+" VALUES (");

        data.forEach(d -> sb.append(d+" ,"));


        //remove the last comma and close the parenthesis
        sb.substring(0, sb.length()-2);
        sb.append(")");


        return sb.toString();
    }


}
