package Rest;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.Map.Entry;

public class ClientAdmin {
    private static WebTarget webTarget;
    private static Client client2;
    public static void main(String[] args) {
        client2 = ClientBuilder.newClient();
        menu();
    }

    private static void menu() {
        String option;
        String some;
        while (true) {
            System.out.println("1 - Add countries\n" + "2 - List all countries\n" + "3 - Add items\n"
                    + "4 - List items\n" + "5 - Get revenue item\n" + "6 - Get expenses item\n" + "7 - Get profit item\n"
                    + "8 - Get total revenue\n" + "9 - Get total expenses\n" + "10 - Get total profits\n" +
                    "11 - Average amount spent in each purchase (per item)\n" +  "12 - Average amount spent in each purchase (per total)\n" +
                    "13 - Get Item with highest profit\n" + "14 - Total revenues last hour\n" + "15 - Total expenses last hour\n" +
                    "16 - Total profit last hour");
            Scanner in = new Scanner(System.in);
            option = in.nextLine();
            switch (option) {
                case "1":
                    some = in.nextLine();
                    addcountries(some);
                    break;
                case "2":
                    listcountries();
                    break;
                case "3":
                    some = in.nextLine();
                    additems(some);
                    break;
                case "4":
                    listitems();
                    break;
                case "5":
                    getrevenueitem();
                    break;
                case "6":
                    getexpensesitem();
                    break;
                case "7":
                    getprofititem();
                    break;
                case "8":
                    gettotalrevenues();
                    break;
                case "9":
                    gettotalexpenses();
                    break;
                case "10":
                    gettotalprofit();
                    break;
                case "11":
                    getaverageitem();
                    break;
                case "12":
                    getaveragetotal();
                    break;
                case "13":
                     getitemhighestprofit();
                     break;
                case "14":
                     getrevenueslasthour();
                     break;
                case "15":
                     getexpenseslasthour();
                     break;
                case "16":
                    getprofitslasthour();
                    break;
                default:
                    System.out.println("Option not reconized");

            }
        }
    }


    private static void listcountries(){
        webTarget = client2.target("http://localhost:9998/admin/getcountries");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        List<String> response = invocationBuilder.get(List.class);
        for(int i = 1; i <= response.size(); i ++){
            System.out.println(i + " - " + response.get(i-1));
        }

    }

    private static  void addcountries(String some){
        webTarget = client2.target("http://localhost:9998/admin/addcountries");
        webTarget.request().post(Entity.entity(some, MediaType.TEXT_PLAIN));

    }

    private static void additems(String item){
        webTarget = client2.target("http://localhost:9998/admin/additems");
        webTarget.request().post(Entity.entity(item, MediaType.TEXT_PLAIN));
    }

    private static void listitems(){
        webTarget = client2.target("http://localhost:9998/admin/getitems");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        List<String> response = invocationBuilder.get(List.class);
        for(int i = 1; i <= response.size(); i ++){
            System.out.println(i + " - " + response.get(i-1));
        }
        //response.forEach(System.out::println);
    }

    private static void getrevenueitem() {
        webTarget = client2.target("http://localhost:9998/admin/getrevenuebyitem");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Map<Integer , Double> response = invocationBuilder.get(Map.class);
        Set<Entry<Integer, Double >> set = response.entrySet();
        Iterator it = set.iterator();
        System.out.println("Item_id\t\tRevenue");

        //getKey() - recupera a chave do mapa
        //getValue() - recupera o valor do mapa

        while(it.hasNext()){
            Entry<Integer, Double> entry = (Entry)it.next();
            System.out.println(entry.getKey() + "\t\t"+entry.getValue());
        }

    }

    private static void getexpensesitem(){

        webTarget = client2.target("http://localhost:9998/admin/getexpensesbyitem");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Map<Integer , Double> response = invocationBuilder.get(Map.class);
        Set<Entry<Integer, Double >> set = response.entrySet();
        Iterator it = set.iterator();
        System.out.println("Item_id\t\tExpenses");

        //getKey() - recupera a chave do mapa
        //getValue() - recupera o valor do mapa

        while(it.hasNext()){
            Entry<Integer, Double> entry = (Entry)it.next();
            System.out.println(entry.getKey() + "\t\t"+entry.getValue());
        }

    }

    private static void getprofititem(){

        webTarget = client2.target("http://localhost:9998/admin/getprofitbyitem");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Map<Integer , Double> response = invocationBuilder.get(Map.class);
        Set<Entry<Integer, Double >> set = response.entrySet();
        Iterator it = set.iterator();
        System.out.println("Item_id\t\tProfit");

        while(it.hasNext()){
            Entry<Integer, Double> entry = (Entry)it.next();
            System.out.println(entry.getKey() + "\t\t"+entry.getValue());
        }
    }

    private static void gettotalrevenues(){
        webTarget = client2.target("http://localhost:9998/admin/gettotalrevenues");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Double response = invocationBuilder.get(Double.class);
        System.out.println(response);
    }

    private static void gettotalexpenses(){
        webTarget = client2.target("http://localhost:9998/admin/gettotalexpenses");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Double response = invocationBuilder.get(Double.class);
        System.out.println(response);
    }

    private static void gettotalprofit(){
        webTarget = client2.target("http://localhost:9998/admin/gettotalprofit");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Double response = invocationBuilder.get(Double.class);
        System.out.println(response);
    }

    private static void getaverageitem(){
        webTarget = client2.target("http://localhost:9998/admin/getaverageitem");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Map<Integer , Double> response = invocationBuilder.get(Map.class);
        Set<Entry<Integer, Double >> set = response.entrySet();
        Iterator it = set.iterator();
        System.out.println("Item_id\t\tExpense");


        while(it.hasNext()){
            Entry<Integer, Double> entry = (Entry)it.next();
            System.out.println(entry.getKey() + "\t\t"+entry.getValue());
        }
    }

    private static void getaveragetotal(){
        webTarget = client2.target("http://localhost:9998/admin/getaveragetotal");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Double response = invocationBuilder.get(Double.class);
        System.out.println(response);
    }

    private static void getitemhighestprofit(){
        webTarget = client2.target("http://localhost:9998/admin/getitemhighestprofit");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Map<Integer , Double> response = invocationBuilder.get(Map.class);
        Set<Entry<Integer, Double >> set = response.entrySet();
        Iterator it = set.iterator();
        System.out.println("Item_id\t\tHighest profit");


        while(it.hasNext()){
            Entry<Integer, Double> entry = (Entry)it.next();
            System.out.println(entry.getKey() + "\t\t"+entry.getValue());
        }
    }

    private static void getrevenueslasthour(){
        webTarget = client2.target("http://localhost:9998/admin/getrevenueslasthour");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Double response = invocationBuilder.get(Double.class);
        System.out.println(response);
    }

    private static void getexpenseslasthour(){
        webTarget = client2.target("http://localhost:9998/admin/getexpenseslasthour");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Double response = invocationBuilder.get(Double.class);
        System.out.println(response);
    }

    private static void getprofitslasthour(){
        webTarget = client2.target("http://localhost:9998/admin/getprofitlasthour");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Double response = invocationBuilder.get(Double.class);
        System.out.println(response);
    }
}
