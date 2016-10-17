package graph;

import java.sql.*;
import java.util.Locale;

public class EstablishConnection {
    
    public static class CreateTableException extends Exception{
        public CreateTableException() {
            super();
        }
        public CreateTableException(String message) {
            super(message);
        }
        public CreateTableException(String message, Throwable cause) {
            super(message, cause);
        } 
        public CreateTableException(Throwable cause) {
            super(cause);
        }
    }
    
    public static Statement recieveStatement(String url, String username,
                 String password) throws CreateTableException{ 
        Connection conn = null;
        try {
            Locale.setDefault(Locale.ENGLISH);
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new CreateTableException();
        }
        if (conn != null) {
            try {
                Statement statement  = conn.createStatement();
                return statement;
            } catch (SQLException e) {
                throw new CreateTableException();
            }
        }
        else {
            throw new CreateTableException();
        }
    }
}