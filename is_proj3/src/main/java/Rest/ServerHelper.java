package Rest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/admin")
public class ServerHelper {
    private static Connection myConn;
    String url = "jdbc:mysql://localhost:3306/test";
    String user = "admin";
    String password = "admin";

    @Path("getcountries")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getcountries(){
        List<String> countries = new ArrayList<>();
        try {
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            System.out.println("Success");
            String getcountries = "select * from test.countries";
            ResultSet rs = myStmt.executeQuery(getcountries);
            while(rs.next()){
                countries.add(rs.getString("country_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    @Path("addcountries")
    @POST
    public void addcountries(String country){
       try{
        myConn = DriverManager.getConnection(url,user,password);
        //Statement myStmt = myConn.createStatement();
        System.out.println("Success");
        String getcountries =  "INSERT INTO test.countries (country_name) VALUES (?)";
        PreparedStatement preparedStmt = myConn.prepareStatement(getcountries);
        preparedStmt.setString(1,country);
        preparedStmt.execute();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @Path("additems")
    @POST
    public void additems(String item){
        try{
            myConn = DriverManager.getConnection(url,user,password);
            //Statement myStmt = myConn.createStatement();
            System.out.println("Success");
            String additems =  "INSERT INTO test.items (item_name) VALUES (?)";
            PreparedStatement preparedStmt = myConn.prepareStatement(additems);
            preparedStmt.setString(1,item);
            preparedStmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Path("getitems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getitems(){
        List<String> items = new ArrayList<>();
        try {
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            System.out.println("Success");
            String getitems = "select * from test.items";
            ResultSet rs = myStmt.executeQuery(getitems);
            while(rs.next()){
                items.add(rs.getString("item_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Path("getrevenuebyitem")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer , Double> getrevenuebyitem(){
        Map<Integer , Double> result = new HashMap<Integer, Double>();
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String revenueitem =  "select * from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(revenueitem);
            while(rs.next()){
                result.put( rs.getInt("item_id") ,rs.getDouble("revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getexpensesbyitem")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer , Double> getexpensesbyitem(){
        Map<Integer , Double> result = new HashMap<Integer, Double>();
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String revenueitem =  "select * from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(revenueitem);
            while(rs.next()){
                result.put( rs.getInt("item_id") ,rs.getDouble("expense"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getprofitbyitem")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer , Double> getprofitbyitem(){
        Map<Integer , Double> result = new HashMap<Integer, Double>();
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String revenueitem =  "select * from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(revenueitem);
            while(rs.next()){
                result.put( rs.getInt("item_id"),rs.getDouble("profit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("gettotalrevenues")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Double gettotalrevenues(){
        Double result = 0.0;
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String revenueitem =  "select SUM(revenue) AS SUMR from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(revenueitem);
            rs.next();
            result = rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("gettotalexpenses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Double gettotalexpenses(){
        Double result = 0.0;
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String expensesitem =  "select SUM(expense) AS SUMR from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(expensesitem);
            rs.next();
            result = rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("gettotalprofit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Double gettotalprofit(){
        Double result = 0.0;
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String profits =  "select SUM(profit) AS SUMR from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(profits);
            rs.next();
            result = rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getaveragetotal")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Double getaveragetotal(){
        Double result = 0.0;
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String profits =  "select SUM(average) AS SUMR from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(profits);
            rs.next();
            result = rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getaverageitem")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer , Double>  getaverageitem(){
        Map<Integer , Double> result = new HashMap<Integer, Double>();
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String revenueitem =  "select item_id, average from test.itemsprofit " ;
            ResultSet rs = myStmt.executeQuery(revenueitem);
            while(rs.next()){
                result.put( rs.getInt("item_id"),rs.getDouble("average"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getitemhighestprofit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer , Double>  getitemhighestprofit(){
        Map<Integer , Double> result = new HashMap<Integer, Double>();
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String revenueitem =  "select * from test.itemsprofit ORDER BY profit DESC LIMIT 1" ;
            ResultSet rs = myStmt.executeQuery(revenueitem);
            rs.next();
            result.put( rs.getInt("item_id"),rs.getDouble("profit"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getrevenueslasthour")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Double getrevenueslasthour(){
        Double result = 0.0;
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String revenues =  "select SUM(revenue) AS SUMR from test.itemslast " ;
            ResultSet rs = myStmt.executeQuery(revenues);
            rs.next();
            result = rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getexpenseslasthour")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Double getexpenseslasthour(){
        Double result = 0.0;
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String expensesitem =  "select SUM(expense) AS SUMR from test.itemslast " ;
            ResultSet rs = myStmt.executeQuery(expensesitem);
            rs.next();
            result = rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Path("getprofitlasthour")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Double getprofitlasthour(){
        Double result = 0.0;
        try{
            myConn = DriverManager.getConnection(url,user,password);
            Statement myStmt = myConn.createStatement();
            String expensesitem =  "select SUM(revenue), SUM(expense) AS SUMR from test.itemslast " ;
            ResultSet rs = myStmt.executeQuery(expensesitem);
            rs.next();
            result = rs.getDouble(1) - rs.getDouble(2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
