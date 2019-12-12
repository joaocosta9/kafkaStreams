package Rest;

import org.apache.avro.reflect.Nullable;

import java.sql.*;
import java.util.Scanner;

public class Admin {
    private static Connection myConn;

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/is_proj3";
        String user = "admin";
        String password = "admin";
        try {
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            System.out.println("Success");
            menu();
            String countries = "select * from is_proj3.countries";
            ResultSet rs = myStmt.executeQuery(countries);
            while(rs.next()){
                System.out.println(rs.getString("country_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void menu() {
        while(true){
            System.out.println("1 - Add Countries \n" +
                               "2 - Show Countries");
            Scanner in = new Scanner(System.in);
            String option = in.nextLine();
            switch(option){
                case "1":
                    System.out.println("Name the country you want to add:");
                    String country = in.nextLine();
                    databaseactions("addcountry", country);
                    break;
                case "2":
                    databaseactions("showcountries", null);
                default:
                   // System.out.println("yellow");
            }
        }
    }

    public static void databaseactions(String action, @Nullable String name)  {
        String query;
        int count = 0;
        try {
            Statement myStmt = myConn.createStatement();
            if(action.equals("addcountry")) {
                query = "INSERT INTO is_proj3.countries (country_name) VALUES (?)";
                PreparedStatement preparedStmt = myConn.prepareStatement(query);
                preparedStmt.setString(1,name);
                preparedStmt.execute();
            }else if (action.equals("showcountries")){
                query = "select * from is_proj3.countries";
                ResultSet rs = myStmt.executeQuery(query);
                while(rs.next()){
                    count ++;
                    System.out.println(count + " - " + rs.getString("country_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
