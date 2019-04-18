import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Bunch of useful methods used in Main
 */

public class Utils {


    /**
     * Generate a part of the query for substring match
     * @param search
     * @param columns
     * @return
     */
    public static String GenerateSubstringMatch(String search, List<String> columns){


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
    public static Table StringToTable(String s){

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
     * Method to covert from Table enum to string
     * @param t the string to convert
     * @return the table corresponding to the string
     */
    public static String TableToString(Table t){

        switch(t){

            case LISTINGS:
                return "LISTINGS";
            case HOSTS:
                return "HOSTS";
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

            tables.add(Utils.StringToTable(checks.get(i).getText()));

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

}
