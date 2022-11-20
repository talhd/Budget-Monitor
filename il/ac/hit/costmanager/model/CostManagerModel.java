package il.ac.hit.costmanager.model;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class CostManagerModel implements IModel {
    //Maximum character length which the user selects for the password and the user
    private static final int MaximumLengthForUserOrPassword = 10;
    //the driver we will use to connect to the database
    private static final String driver = "com.mysql.jdbc.Driver";
    //Protocol path to use with the driver,if necessary the login details can by change
    private static final String protocol = "jdbc:mysql://localhost:3306/costmanager?user=root&password=root";
    /*
     * Creates an initial connection to DB,must use connectToDatabase()
     * */
    @Override
     public Connection connectToDatabase() throws CostManagerException {
        try
        {
            Class.forName(driver);
            return DriverManager.getConnection(protocol);
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CostManagerException("Failed to connect to server",e);
        }
    }
    /*
    *closes the connection to the DB
    * after using CloseConnectionToServer() to use DB again,use connectToDatabase()
     * */
    @Override
    public void closeConnectionToServer(Connection connection) throws CostManagerException
    {
        if(connection!=null)
            try{
                connection.close();
            }
            catch(SQLException e)
            {
                throw new CostManagerException("unable to close the server connection",e);
            }
    }
    /*
     * Add a new user to DB,as long as:
     * username and password <= 10 characters (MaximumLength)
     * username available for use
     * return -1 if username already in use
     * return 1 if registration completed successfully
     * return 0 if username or password is larger than 10 characters
     * return -2 if username or password is empty
     * */
    @Override
    public int addNewUser(User newUser) throws CostManagerException {
        try {
            Connection connection = connectToDatabase();
            PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO `user_table` values(?,?,?)");
            if(newUser.getUsername().length()==0 || newUser.getPassword().length() == 0){
                closeConnectionToServer(connection);
                //username or password is empty
                return -2;
            }
            if (doesUserNameExist(newUser))
            {
                //username already in use
                closeConnectionToServer(connection);
                return -1;
            }
            else if(newUser.getUsername().length() <= MaximumLengthForUserOrPassword && newUser.getPassword().length() <= MaximumLengthForUserOrPassword)
            {
                int newUserId=newIdNumberForNewUser();
                preparedStatement.setString(1,newUser.getUsername());
                preparedStatement.setString(2,newUser.getPassword());
                preparedStatement.setInt(3,newUserId);
                preparedStatement.executeUpdate();
                newUser.setUserId(newUserId);
                closeConnectionToServer(connection);
                //Registration completed successfully
                return 1;
            }
            else
            {
                //username or password is Larger than 10 characters
                closeConnectionToServer(connection);
                return 0;
            }
        }
        catch (SQLException e){
            throw new CostManagerException("Can not add new user",e);
        }
    }
    @Override
    /*
    * return true if the username and password match to DB data.
    * allow the user to connect to the service.
    * */
    public boolean userValidation(User loginUser) throws CostManagerException {
        try {
            Connection connection = connectToDatabase();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT Username,Passwords,user_id from user_table WHERE Username = '" + loginUser.getUsername() + "' AND Passwords = '"+ loginUser.getPassword() +"'");
            ResultSet result=preparedStatement.executeQuery();
            if(result.next()){
               loginUser.setUserId(result.getInt("user_id"));
                closeConnectionToServer(connection);
                return true;
            }
            else {
                closeConnectionToServer(connection);
                return false;
            }
        }
        catch (SQLException e){
            throw new CostManagerException("failed to test user",e);
        }
    }
    @Override
    /*
    * Check if the user exists in the DB.
    * return true or false respectively
    * */
    public boolean doesUserNameExist(User testedUser) throws CostManagerException {
        try {
            Connection connection = connectToDatabase();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT Username from user_table WHERE Username = '"+ testedUser.getUsername() + "'");
            ResultSet result=preparedStatement.executeQuery();
            boolean savedResult=result.next();
            /*
             * return true if user in DB else return false
             * */
            closeConnectionToServer(connection);
            return savedResult;
        }
        catch (SQLException e){
            throw new CostManagerException("User Existence test failed",e);
        }

    }
    @Override
    /*
    * return the next user id to use(the last number used + 1)
    * to know which user id to give to a new user
    * */
    public int newIdNumberForNewUser() throws CostManagerException {
        try {
            Connection connection = connectToDatabase();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT Username,Passwords,user_id from user_table");
            ResultSet result=preparedStatement.executeQuery();
            int lastNumber=0;
            while(result.next())
            {
                if (result.getInt("user_id") > lastNumber){
                    lastNumber=result.getInt("user_id");
                }
            }
            closeConnectionToServer(connection);
            return lastNumber+1;
        }
        catch (SQLException e){
            throw new CostManagerException("Unable to find last user ID",e);
        }
    }
    @Override
    /*
     * Adds a new category to DB
     * return 0 If the category already exists then there is no need to add nothing
     * return 1 If a new category was added
     * */
    public int addNewCategory(Category newCategory,User user) throws CostManagerException {
       try {
           Connection connection = connectToDatabase();
           int flag=0;
           PreparedStatement preparedStatement=connection.prepareStatement("SELECT categories from categories WHERE user_id ="+user.getUserId());
           ResultSet result=preparedStatement.executeQuery();
           while(result.next())
           {
               if (result.getString("categories").equals(newCategory.getCategory())){
                   flag=1;
               }
           }
           if (flag == 0){
               preparedStatement=connection.prepareStatement("INSERT INTO `categories` values(?,?)");
               preparedStatement.setString(1,newCategory.getCategory());
               preparedStatement.setInt(2,user.getUserId());
               preparedStatement.executeUpdate();
               closeConnectionToServer(connection);
               return 1;
           }
           closeConnectionToServer(connection);
           return 0;
       }
       catch (SQLException e){
           throw new CostManagerException("a new category can not be added",e);
       }
    }
    /*
    * return all the user costs from db as list
    * */
    @Override
    public List<String> returnCostFromDb(User user) throws CostManagerException {
        try {
            Connection connection = connectToDatabase();
            List<String> listRecords=new ArrayList<>();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT amount_of_expense,currency,category,text,date,user_id from record_table");
            ResultSet result=preparedStatement.executeQuery();
            while(result.next())
            {
                if (result.getInt("user_id") == user.getUserId()){
                    listRecords.add(result.getString("amount_of_expense")+" "+result.getString("currency")+" "+
                            result.getString("category")+" "+result.getString("text")+" "+
                            result.getString("date"));
                }
            }
            closeConnectionToServer(connection);
            return listRecords;
        }
        catch (SQLException e){
            throw new CostManagerException("Error retrieving records from DB",e);
        }
    }
    @Override
    /*
    * add a new record to the database.
    * if the data is entered successfully return 1,otherwise returns 0.
    * */
    public int addNewCost(Cost newCost,User currentUser) throws CostManagerException {
        try {
            Connection connection = connectToDatabase();
            int flag=0;
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyy");
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT Username,Passwords,user_id from user_table");
            ResultSet result=preparedStatement.executeQuery();
            while(result.next())
            {
                if (result.getInt("user_id") == newCost.getUserId()){
                    flag=1;
                }
            }
            if (flag == 1){
                preparedStatement=connection.prepareStatement("INSERT INTO `record_table` VALUES (?,?,?,?,?,?)");
                preparedStatement.setString(1,newCost.getAmountOfExpense().toString());
                preparedStatement.setString(2,newCost.getCurrency());
                preparedStatement.setString(3,newCost.getCategory().getCategory());
                preparedStatement.setString(4,newCost.getText());
                preparedStatement.setString(5,newCost.getDate().format(dateFormat));
                preparedStatement.setInt(6,newCost.getUserId());
                preparedStatement.executeUpdate();
                closeConnectionToServer(connection);
                return 1;
            }
            closeConnectionToServer(connection);
            return 0;
        }
        catch (SQLException e){
            throw new CostManagerException("can not add new cost",e);
        }
    }
    @Override
    /*
    * return all user records between Start date and end date.
    * to generate a user-requested report
    * */
    public List<String> returnUserReport(String StartDate,String endDate,User user) throws CostManagerException {
      try {
          Connection connection = connectToDatabase();
          DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyy");
          LocalDate dateFromDb;
          LocalDate start=LocalDate.parse(StartDate, dateFormat);
          LocalDate end=LocalDate.parse(endDate, dateFormat);
          List<String> listRecords=new ArrayList<>();
          PreparedStatement preparedStatement=connection.prepareStatement("SELECT amount_of_expense,currency,category,text,date,user_id from record_table");
          ResultSet result=preparedStatement.executeQuery();
          while(result.next())
          {
              dateFromDb= LocalDate.parse(result.getString("date"), dateFormat);
              if (result.getInt("user_id") == user.getUserId() && (dateFromDb.isEqual(end) ||dateFromDb.isEqual(start) || dateFromDb.isAfter(start) && dateFromDb.isBefore(end))){
                  listRecords.add(result.getString("amount_of_expense")+" "+result.getString("currency")+" "+
                          result.getString("category")+" "+result.getString("text")+" "+
                          result.getString("date"));
              }
          }
          closeConnectionToServer(connection);
          return listRecords;
      }
      catch (SQLException e){
          throw new CostManagerException("Error retrieving data",e);
      }

    }
    @Override
    /*
    * return all the categories in the database belonging to the user.
    * to allow him to use them when a new record needs to be added.
    * */
    public List<String> ReturnAllTheCategoriesInTheDatabase(User user) throws CostManagerException {
        try {
            Connection connection = connectToDatabase();
            List<String> listRecords=new ArrayList<>();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT categories,user_id from categories WHERE user_id ="+user.getUserId());
            ResultSet result=preparedStatement.executeQuery();
            while(result.next())
            {
                listRecords.add(result.getString("Categories"));
            }
            //If there are no categories in DB,3 default categories will be added
            if (listRecords.size() == 0){
                Category defaultCategory1=new Category("household");
                Category defaultCategory2=new Category("transportation");
                Category defaultCategory3=new Category("insurance");
                addNewCategory(defaultCategory1,user);
                addNewCategory(defaultCategory2,user);
                addNewCategory(defaultCategory3,user);
                preparedStatement=connection.prepareStatement("SELECT categories,user_id from categories WHERE user_id ="+user.getUserId());
                result=preparedStatement.executeQuery();
                while(result.next())
                {
                    listRecords.add(result.getString("Categories"));
                }
            }
            closeConnectionToServer(connection);
            return listRecords;
        }
        catch (SQLException e){
            throw new CostManagerException("Error accessing categories in database",e);
        }
    }
}