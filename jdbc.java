import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
public class Jdbc {
    static Connection connection;
    static String user_name;
    static String password;
    static Scanner scanner = new Scanner(System.in);
    static final String url = "jdbc:mysql://localhost:3306/information_schema";

    public static int takeUserInputForCustomerID(){
        int costumer_id;
        while(true){
            System.out.println("Enter the customer id");
            try{
                costumer_id = Integer.parseInt(scanner.nextLine());
                break;
            }
            catch(Exception e){
                System.out.println("Enter the correct customer id");
            }
        }
        return costumer_id;
    }

    public static void establishConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(Exception e){
            System.out.println("Driver class not found.");
            System.exit(0);
        }
        while(true){
            System.out.println("Please enter the user name");
            user_name = scanner.nextLine();
            System.out.println("Please enter the password");
            password = scanner.nextLine();
            // establish the connection
            try{
                connection = DriverManager.getConnection(url, user_name, password);
                break;
            }
            catch(Exception e){
                System.out.println("Please enter correct credentials");
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
        if(connection == null){
            System.out.println("Connection is not established");
            System.exit(0);
        }
        else{
            System.out.println("Connection successfully established");
        }
    }
    public static ResultSet searchByCustomerID()
    {
   
    }
    public static void insertImagesInBatch() throws SQLException
    {
  
    }
    public static void deleteProduct() throws SQLException
    {
      
    }
    public static void main(String[] args) throws Exception
    {
        establishConnection();
    }
}
