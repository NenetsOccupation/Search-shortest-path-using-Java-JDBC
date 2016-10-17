package graph;

import java.util.Scanner;
import java.sql.*;
import java.util.ArrayList;

public class UserInterface {
    
    public static String tableName;
    static Statement statement;
    
        public static void main(String[] args) {
            
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the magic world of discrete mathematics, "
                + "graphs and SQL! My name is PathFinder 1.0 and I'll be your guide "
                + "here.\n\nAt the beginning we have to establish connection with "
                + "your SQL-server.\nEnter its parameters below (to use default "
                + "just skip it with 'Enter'):");
        boolean flag;
        do{
            flag = false;
            System.out.print("URL: ");
            String url = in.nextLine();
            if (url.isEmpty()) url = "jdbc:oracle:thin:@localhost:1521:xe";
            System.out.print("Username: ");
            String username = in.nextLine();
            if (username.isEmpty()) username = "system";
            System.out.print("Password: ");
            String password = in.nextLine();
            if (password.isEmpty()) password = "111111";
            System.out.println();
            try {
                statement = EstablishConnection.recieveStatement
                                                        (url, username, password);
            } catch (EstablishConnection.CreateTableException e){
                System.out.println("Failed to establish connection with your SQL"
                + "-server. Check url, username and password. Contact your DBA.");
                flag = true;
            }
        } while (flag);
        System.out.println("You successfully connected server using JDBC.\n\n"
                + "Would you like to create new graph or use existing?\n1) " 
                + "Create new table.\n2) Use existing table.");
        graphStPetersburgSubway.createSubwayTable(statement);
        int a = 0;
        boolean b;
        do{
            try{
                a = in.nextInt();
                if ((a == 1) || (a == 2)) {
                    b = true;
                }
                else {
                    b = false;
                    System.out.println("Try again.");
                }
            } catch (java.util.InputMismatchException e) {
                b = false;
                in.nextLine();
                System.out.println("Try again.");
            }
        }while(!b);
        System.out.println(a == 1 ? "\nEnter your table name below:" : 
                                            "\nEnter target table name below:");
        do{
            tableName = in.next();
            if (tableName.chars().allMatch(Character::isLetter)){
                if (a == 1){
                    try{
                        statement.executeQuery("SELECT 1 FROM " + tableName);
                        System.out.println("Table " + tableName + " is already"
                                + " exists. Drop it in your DB or choose"
                                + " another name\n");
                        flag = true;
                    } catch (SQLSyntaxErrorException e){
                        System.out.println("\nYou have to describe your graph now");
                        System.out.println("Using: 'FirstNuber, SecondNuber'"
                                    + " (i.e. enter '4, 8' to insert edge 4"
                                    + " -> 8 or '8, 4' to inser 8 -> 4).\nEnter"
                                    + " empty line to stop");
                        flag = false;
                        String localList;
                        in.nextLine();
                        ArrayList<Integer[]> result = new ArrayList();
                        do{
                            localList = in.nextLine();
                            result = parseString(localList, result);
                        }while((!localList.isEmpty()) || (result.isEmpty()));
                        SQL_API.createTable(statement, tableName, result);
                    } catch (SQLException e){
                        flag = false;
                    }
                }
                else {
                    try{
                        statement.executeQuery("SELECT 1 FROM " + tableName);
                        flag = false;
                    } catch (SQLSyntaxErrorException e){
                        System.out.println("There is no table with name '"
                        + tableName + "'.");
                        flag = true;
                    } catch (SQLException e){
                        System.out.println("\nThere is no table with name '"
                        + tableName + "'.");
                        flag = true;
                    }
                }      
            } else {
                System.out.println("Table name has to consists of Latin " +
                                            "characters only! Choose another.");
                flag = true;
            }
        }while (flag);
        System.out.println("Now you are using table: " + tableName + "\n\n" +
                                                  "Enter the start vertice");
        int startID = 0;
        do{  
            try{
                startID = Integer.parseInt(in.next());
                if (startID < 0){
                    startID = 0;
                    System.out.println("ID have to be more than zero.");
                }
            } catch (java.util.InputMismatchException | java.lang.NumberFormatException e){
                System.out.println("Enter start vertice id (integer).");
                startID = 0;
            }
        } while (startID == 0);
        int targetID = 0;
        System.out.println("Enter the target vertice:");
        do{  
            try{
                targetID = Integer.parseInt(in.next());
                if (targetID < 0){
                    targetID = 0;
                    System.out.println("ID have to be more than zero.");
                }
            } catch (java.util.InputMismatchException | java.lang.NumberFormatException e){
                System.out.println("Enter start vertice id (integer).");
                targetID = 0;
            }
        } while (targetID == 0);
        ArrayList<Integer> resultList = Graphs.getPath(statement, tableName, startID, targetID);
        if (resultList == null){
            System.out.println("There is no path from " + startID + " to "
                                                                    + targetID);
        }
        else {
            System.out.println("\nYour sequence of vertices:\n");
            for(int i = resultList.size()-2; i > 0; i--){
                System.out.print(resultList.get(i) + " -> ");
            }
            System.out.println(targetID);
            try {
                statement.execute("DROP TABLE " + tableName + "_used");
            } catch (SQLException ex) { }
        }
    }
        
        public static ArrayList<Integer[]> parseString(String localList, 
                ArrayList<Integer[]> arr) {
            try{
                    String[] stringList = localList.split(", "); 
                if (stringList.length == 2){
                    Integer[] intList = {Integer.parseInt(stringList[0]),
                                    Integer.parseInt(stringList[1])};
                    if ((intList[0] <= 0)||(intList[1] <= 0)){
                        System.out.println("Variables have to be more than zero");
                    }
                    else{
                        arr.add(intList);
                    }
                }
                else {
                    if (!localList.isEmpty()){
                        System.out.println("Read 'Using' again. You have "
                            + "to enter data as 'FirstNumber, SecondNumber'.");
                    }
                }
            } catch (java.lang.NumberFormatException e){
                System.out.println("Variables have to be integer");
            }
            return arr;
        }
}