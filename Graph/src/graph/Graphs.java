package graph;

import java.util.*;
import java.sql.*;

public class Graphs {
    
     public static void createUsedTable(Statement statement, String tableName, int startID,
             int targetID) throws EstablishConnection.CreateTableException{
        try {
        statement.execute("CREATE TABLE " + tableName + "_used (" +
                    "FIRST_VERTICE_ID NUMBER(3) NOT NULL," +
                    "SECOND_VERTICE_ID NUMBER(3) NOT NULL," +
                    "WEIGHT NUMBER(3)," +
                    "PRIMARY KEY (FIRST_VERTICE_ID, SECOND_VERTICE_ID))");
        statement.execute("INSERT INTO " + tableName + "_used" +
                    "(FIRST_VERTICE_ID, SECOND_VERTICE_ID, WEIGHT) values" + 
                    "(" + startID + ", -1, 0)");
        } catch (SQLException e) {
            //e.printStackTrace();
            //System.out.println("Failed to create table");
        }
        ArrayList<Integer> localList;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startID);
        int currentID;
        for (int i = 0; !(queue.isEmpty()); i++){
            currentID = queue.remove();
            localList = SQL_API.getNeighboringVerts(statement, tableName, currentID);
            for (int nextID : localList)
            try {
                ResultSet Weight = statement.executeQuery("SELECT WEIGHT" +
                    " FROM " + tableName +"_used WHERE FIRST_VERTICE_ID = " + 
                    currentID);
                Weight.next();
                int weight = Weight.getInt("WEIGHT") + 1;
                statement.executeUpdate("INSERT INTO " + tableName + "_used "+
                    " (FIRST_VERTICE_ID, SECOND_VERTICE_ID, WEIGHT) VALUES " +
                    "(" + nextID + ", " + currentID + ", " + weight + ")");
                queue.add(nextID);
            } catch (SQLException e){
                //e.printStackTrace();
                //System.out.println("Error during DB processing;");
            }
        }
    }
     
     public static ArrayList<Integer> getPath(Statement connection, String tableName, int startID, int targetID){
        try {
            createUsedTable(connection, tableName, startID, targetID);
        } catch (EstablishConnection.CreateTableException e){
            
        }
        int parent = targetID;
        ArrayList<Integer> parentList = new ArrayList<>();
        do{
            ResultSet parentTable;

            try{
                parentTable = connection.executeQuery("SELECT *" +
                    " FROM " + tableName +"_used WHERE (FIRST_VERTICE_ID = " + 
                    parent + ") ORDER BY WEIGHT");
                parentTable.next();
                parent = parentTable.getInt("SECOND_VERTICE_ID");
                parentList.add(parent);
            } catch (SQLException e) {
                return null;
            }
        }while(parent != -1);
        return parentList;
     }
}