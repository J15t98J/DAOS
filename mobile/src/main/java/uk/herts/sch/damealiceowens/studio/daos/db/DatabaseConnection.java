package uk.herts.sch.damealiceowens.studio.daos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    Connection conn = null;
    Statement st = null;

    public DatabaseConnection(String pathToDB) {
        connect(pathToDB);
    }

    /**
     * Open a connection to the DB.
     * @param pathToDB path to the database file
     * @return success
     */
    public boolean connect(String pathToDB) {
            try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dev.db");
            return true;
        } catch(ClassNotFoundException e) {
            System.out.println("JDBC library not installed!");
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute a read operation (e.g. SELECT * FROM <> WHERE <>=<>)
     * @param statement operation to perform
     * @return results of the query
     */
    public ResultSet executeQuery(String statement) {
        try {
            Statement st = conn.createStatement();
            return st.executeQuery(statement);
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(st != null) {
                    st.close();
                    st = null;
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Execute a write operation (e.g. CREATE TABLE <>())
     * @param statement operation to perform
     * @return success
     */
    public boolean executeUpdate(String statement) {
        try {
            Statement st = conn.createStatement();
            return st.executeUpdate(statement) == 1;
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                st = null;
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
