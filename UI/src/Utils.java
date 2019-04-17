import java.sql.Connection;
import java.sql.DriverManager;
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

        sb.append("WHERE");

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
    public static Main.Table StringToTable(String s){

        switch(s){

            case "LISTINGS":
                return Main.Table.LISTINGS;
            case "HOSTS":
                return Main.Table.HOSTS;
            case "REVIEWS":
                return Main.Table.REVIEWS;

        }

        return Main.Table.NONE;

    }


    /**
     * Method to covert from Table enum to string
     * @param t the string to convert
     * @return the table corresponding to the string
     */
    public static String TableToString(Main.Table t){

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






}
