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
    //"jdbc:mysql://localhost:3306/";
    //jdbc:mysql://localhost:3306/?user=root
    /**
     * Take input for customer id
     * @return integer value of customer id
     */
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
    /**
     * Establish the connection with database
     */
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
    /**
     * Searches records for a particular customer id
     * @return result set containing the result
     */
    public static ResultSet searchByCustomerID(){
        /*Given the id of a user, fetch all orders (Id, Order Date, Order Total) of that user which are in shipped state.
         * Orders should be sorted by order date column in chronological order.
         */
        try{
            PreparedStatement statement = null;
            statement = connection.prepareStatement("SELECT o.order_id, o.order_date_time, o.order_amount FROM orders o"
                    + " JOIN customer c WHERE c.customer_id=? AND o.order_status='shipped' ORDER BY ");
            int costumer_id = takeUserInputForCustomerID();
            statement.setInt(1, costumer_id);
            ResultSet result = statement.executeQuery();

            return result;
        }
        catch(Exception e){
            System.out.println("Invalid query execution");
            ResultSet result1 = null;
            return result1;
        }
    }
    /**
     * Insert images in product table
     * @throws SQLException in case of any incorrect query
     */
    public static void insertImagesInBatch() throws SQLException{
        String queryForInsertion = "INSERT INTO image (product_id, img) VALUES(?, ?)";
        PreparedStatement prepareStatementForImagesInsertion = connection.prepareStatement(queryForInsertion);

        ArrayList<String> imagesUrl = new ArrayList<String>();
        imagesUrl.add("https://picsum.photos/200/300");
        imagesUrl.add("https://picsum.photos/200/300");
        imagesUrl.add("https://picsum.photos/200/300");
        imagesUrl.add("https://picsum.photos/200/300");
        imagesUrl.add("https://picsum.photos/200/300");
        imagesUrl.add("https://picsum.photos/200/300");
        imagesUrl.add("https://picsum.photos/200/300");
        try{
            for(int i = 0; i < 6; i++){
                prepareStatementForImagesInsertion.setInt(1, i + 101 );
                prepareStatementForImagesInsertion.setString(2, imagesUrl.get(i));
                prepareStatementForImagesInsertion.addBatch();
                if(i % 3 == 0)
                    prepareStatementForImagesInsertion.executeBatch();
            }
            prepareStatementForImagesInsertion.executeBatch();
            System.out.println("Query executed successfully");
            prepareStatementForImagesInsertion.close();
        }
        catch(SQLException se){
            System.out.println("Query failed" + se);
        }
    }
    /**
     * Calculates number of child sub categories for each parent category
     * @return ResultSet which contains Parent Category Name and count of number of sub categories for each parent category
     * @throws SQLException In case of incorrect category
     */
    public static ResultSet displayCategories() throws SQLException{
        String queryForCategory = "SELECT category_name AS category, count(sc.product_id) AS total FROM category c "
                + "join subcategory sc WHERE c.product_id=sc.product_id GROUP BY c.product_id;";
        PreparedStatement preparedStatementForCateogory = connection.prepareStatement(queryForCategory);
        ResultSet result = preparedStatementForCateogory.executeQuery();
        return result;
    }
    /**
     * Finds the products that are not ordered in the last 1 year
     * @throws SQLException In case of incorrect query
     */
    public static void deleteProduct() throws SQLException{
        String query ="DELETE FROM Products WHERE product_id NOT IN "
                + "(SELECT o.product_id FROM orders o WHERE DATEDIFF(CURDATE(),o.order_date_time)<=90)" ;
        PreparedStatement statementForProducts = connection.prepareStatement(query);
        ResultSet result = statementForProducts.executeQuery();
        if(!result.next()){
            System.out.println("No row fetched");
        }
        else{
            System.out.println("Product Id    Order Date");
            System.out.println(result.getInt(1) + "         " +result.getString(2));
            while(result.next()){
                System.out.println(result.getInt(1) + "        " +result.getString(2));
            }
        }
    }

    public static void main(String[] args) throws Exception {

        // register Oracle thin driver with DriverManager service
        // It is optional for JDBC4.x version
        establishConnection();
        System.out.println("Please select from the following options"
                + "\n1.Given the id of a user, fetch all orders (Id, Order Date, Order Total) of that user which are in shipped state. "
                + "Orders should be sorted by order date column in chronological order"
                + "\n2.Insert five or more images of a product using batch insert technique."
                + "\n3.Delete all those products which were not ordered by any Shopper in last 1 year. Return the number of products deleted."
                + "\n4.Select and display the category title of all top parent categories sorted alphabetically and the count of their child categories."
                + "\n5.Exit");
        while(true){
            boolean checkLoop = false;
            System.out.println("Please enter your choice");
            int choice;
            try{
                choice = Integer.parseInt(scanner.nextLine());
            }
            catch(Exception e){
                System.out.println("Please enter the correct choice");
                continue;
            }
            switch (choice) {
                case 1:
                    ResultSet result = searchByCustomerID();
                    if(result == null || result.next() == false){
                        System.out.println("No results fetched for the given customer id");
                        continue;
                    }
                    else{
                        System.out.println("---------------------------------------------------------------------------------");
                        System.out.println();
                        System.out.printf("%20s %20s %20s %20s", "Customer Id", "Order Id", "Order Date", "Total Price");
                        System.out.printf("%20d %20d %20s %20d", result.getInt("CUST_ID"),result.getInt("ORDER_ID"),result.getTimestamp("ORDER_DATE"),result.getInt("TOTAL_PRICE"));
                        while(result.next()){
                            System.out.println();
                            System.out.printf("%20d %20d %20s %20d", result.getInt("CUST_ID"),result.getInt("ORDER_ID"),result.getTimestamp("ORDER_DATE"),result.getInt("TOTAL_PRICE"));
                        }
                        System.out.println();
                        System.out.println("---------------------------------------------------------------------------------");
                    }
                    break;
                case 2:
                    try{
                        insertImagesInBatch();
                    }
                    catch (SQLException e) {
                        System.out.println("Query Failed" + e);
                    }
                    break;
                case 3:
                    try{
                        deleteProduct();
                        System.out.println("Successfully executed the required operation");
                    }
                    catch (SQLException e) {
                        System.out.println("Incorrect Query" + e);
                    }
                    break;
                case 4:
                    ResultSet result1;
                    try{
                        result1 = displayCategories();
                    }
                    catch(SQLException sq){
                        System.out.println(sq);
                        continue;
                    }
                    System.out.printf("%20s %20s", "Category Name","Number of child categories");
                    System.out.println();
                    if(result1.next() == false){
                        System.out.println("No categories to display");
                        break;
                    }
                    else{
                        while(result1.next()){
                            System.out.printf("%20s %20d", result1.getString(1), result1.getInt(2));
                            System.out.println();
                        }
                        break;
                    }
                case 5:
                    checkLoop = true;
                    break;
                default:
                    System.out.println("Please enter the correct choice");
                    break;
            }
            if(checkLoop)
                break;
        }
    }
}