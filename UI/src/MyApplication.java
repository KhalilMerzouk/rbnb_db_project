import javafx.scene.Scene;

import java.sql.Connection;

/**
 * Contains objects that are needed across the entire program
 */
public abstract class MyApplication {


    //DB connection following Singleton design pattern
    private static Connection sqlConnection = null;

    public static Scene mainScene;

    /**
     * Getter for Singleton Object Connection
     * @return the DB Connection instance
     */
    public static Connection getDBConnection(){


        if(sqlConnection == null){
            sqlConnection = Utils.getConnection();

        }

        return sqlConnection;

    }



}
