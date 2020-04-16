//https://www.tutorialspoint.com/jdbc/jdbc-create-database.htm

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

class SQLInstructions {

    static Connection connectToSQL() {
        Connection connection = null;
        InputStream propertiesFileStream = null;

        try {
            //load properties file
            propertiesFileStream = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/database_config.properties");

            Properties dBProperties = new Properties();
            dBProperties.load(propertiesFileStream);

            //Registering the Driver
            Class.forName(dBProperties.getProperty("jdbc.package"));
            //Getting the connection
            connection = DriverManager.getConnection(dBProperties.getProperty("mysqlurl"),
                    dBProperties.getProperty("ethan.user"),
                    dBProperties.getProperty("ethan.password"));
        }
        catch (Exception e) {
//            System.out.println(CONNECTON_FAILED);
            e.printStackTrace();
        }
        //handles closing the input stream to the properties file
        finally {
            try {
                if (propertiesFileStream != null)
                    propertiesFileStream.close();
            }
            catch (IOException iOE) {
                iOE.printStackTrace();
            }

        }

//        System.out.println(CONNECTION_ESTABLISHED);
        return connection;
    }

    static Connection connectToPollDB(String dBName) {
        Connection connection = null;
        InputStream propertiesFileStream = null;

        try {
            //load properties file
            propertiesFileStream = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/database_config.properties");

            Properties dBProperties = new Properties();
            dBProperties.load(propertiesFileStream);

            //Registering the Driver
            Class.forName(dBProperties.getProperty("jdbc.package"));
            //Getting the connection
            connection = DriverManager.getConnection(dBProperties.getProperty("mysqlurl") + dBName,
                    dBProperties.getProperty("ethan.user"),
                    dBProperties.getProperty("ethan.password"));
        }
        catch (Exception e) {
//            System.out.println(CONNECTON_FAILED);
            e.printStackTrace();
        }
        //handles closing input stream to the properties file
        finally {
            try {
                if (propertiesFileStream != null)
                    propertiesFileStream.close();
            }
            catch (IOException iOE) {
                iOE.printStackTrace();
            }
        }

//        System.out.println(CONNECTION_ESTABLISHED);
        return connection;
    }

    static void createDatabase(String dBName) {

        Statement statement = null;
        Connection connection = null;

        try {
            connection = connectToSQL();

            statement = connection.createStatement();

            //in case user tries to create a database with a name that is an SQL reserved keyword
            String explicitDBName = "`" + dBName + "`";

            //instructions for creating databse in SQL
            String sqlInstructions = "CREATE DATABASE " + explicitDBName + ";";

            //execute creating new database
            statement.executeUpdate(sqlInstructions);

            System.out.println(dBName + " was created successfully");
        }
        catch (SQLException sQLE) {
            sQLE.printStackTrace();
        }
        //handles closing connection and statement variables
        finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }
            //if statement or connection cannot close cannot close
            catch (SQLException sQLE) {
                sQLE.printStackTrace();
            }
        }

    }

    static void createTable(String tableName, String dBName) {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = connectToPollDB(dBName);

            statement = connection.createStatement();

            //in case the user names the poll with an SQL keyword
            String explicitTableName = "`" + tableName + "`";

            //instructions for creating a table in SQL
            String sqlInstructions = "CREATE TABLE " + dBName + "." + explicitTableName +
                    "(QuestionNumber INT NOT NULL AUTO_INCREMENT, " +
                    " Question VARCHAR(100), " +
                    " Options VARCHAR(150), " +
                    " PRIMARY KEY (QuestionNumber));";

            //execute creating the table
            statement.executeUpdate(sqlInstructions);

            System.out.println(tableName + " was created successfully");
        }
        //if statement cannot be created
        catch (SQLException sQLE) {
            sQLE.printStackTrace();
        }
        //for closing statement and connection
        finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }
            catch (SQLException sQLE) {
                sQLE.printStackTrace();
            }
        }

    }

}
