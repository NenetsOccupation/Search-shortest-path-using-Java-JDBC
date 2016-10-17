package graph;

import java.sql.*;
import java.util.ArrayList;

public class SQL_API {
    static Statement statement;
    
    public static void createTable(Statement statement, String tableName, 
            ArrayList<Integer[]> list) {
        try {
            statement.execute("CREATE TABLE " + tableName + " (" +
                    "FIRST_VERTICE_ID NUMBER(3) NOT NULL," +
                    "SECOND_VERTICE_ID NUMBER(3) NOT NULL," +
                    "PRIMARY KEY (FIRST_VERTICE_ID, SECOND_VERTICE_ID))");
            // TODO: List of vertices' names
        } catch (SQLException e) {
            if (e.getErrorCode() != 955){
                System.out.println("Failed to create table. Probably, smth " +
                    "wrong with its name.");
            }
            else { }
        }
        for (int i = 0; i <= list.size(); i++){
            try {
                if (i != 0) { statement.executeUpdate("INSERT INTO " + tableName + 
                        " (FIRST_VERTICE_ID, SECOND_VERTICE_ID) VALUES " +
                        "(" + list.get(i-1)[0] + " , " + list.get(i-1)[1] + " )");
                }
            } catch (SQLException e) {
            }
        }
    }
    public static ArrayList<Integer> getNeighboringVerts
                        (Statement statement, String tableName, int verticeId) throws 
                        EstablishConnection.CreateTableException{
        try {
            ResultSet rs = statement.executeQuery("SELECT SECOND_VERTICE_ID "
                    + "FROM " + tableName + " WHERE FIRST_VERTICE_ID = "
                    + verticeId);
            
            ArrayList<Integer> al = new ArrayList<>();
            while (rs.next()) {
                al.add(rs.getInt("SECOND_VERTICE_ID"));
            }
        return al;
        } catch (SQLException e) {
            System.out.println("Failed to insert values.");
        }
        return null;
    }
    
}