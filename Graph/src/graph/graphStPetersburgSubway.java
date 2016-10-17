package graph;

import java.sql.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.List.*;

public class graphStPetersburgSubway {
    public static Integer[][] getStationsList(){
        Integer[][] list = new Integer[151][2];
            int summ = 0;
            int [] numberOfStations = {19, 18, 10, 8, 12};
            for (int n : numberOfStations){
                for (int i = summ; i <  n + summ - 1; i++){
                    int halfSumm = summ >> 1;
                    list[i][0] = i-halfSumm+1;
                    list[i][1] = i-halfSumm+2;
                    list[i+n-1][0] = i-halfSumm+2;
                    list[i+n-1][1] = i-halfSumm+1;
                }
                summ += 2*(n);
            } 
            int[][] extraStations = {{10, 41}, {11, 49}, {12, 63}, {13, 30}, {28, 40},
                    {29, 48}, {29, 62}, {42, 51}, {48, 62}};
            for (int i = 0; i < extraStations.length; i++){
               list[i+133][0] = extraStations[i][0];
               list[i+133][1] = extraStations[i][1];
               list[i+133+extraStations.length][0] = extraStations[i][1];
               list[i+133+extraStations.length][1] = extraStations[i][0];
            }
        return list;
    }
    
    public static void createSubwayTable(Statement statement){
        Integer list[][] = getStationsList();
        ArrayList<Integer[]> newArray = new ArrayList();
        newArray.addAll(Arrays.asList(list));    
        SQL_API.createTable(statement, "SUBWAY", newArray);
    }
    
}