package il.ac.hit.costmanager.model;
import java.sql.Connection;
import java.util.List;
public interface IModel {
     /*
     * establish a connection to a database.
     * if necessary throw CostManagerException,which helps us understand what the problem is.
     * */
     Connection connectToDatabase() throws CostManagerException;
     /*
     * closing the connection to the database
     * */
     void closeConnectionToServer(Connection statement)throws CostManagerException;
     /*
     * adding a new user to the database
     * */
     int addNewUser(User newUser) throws CostManagerException;
     /*
     * checks the user and password to log in
     * */
     boolean userValidation(User loginUser) throws CostManagerException;
     /*
     * checks if the username is already in use
     * */
     boolean doesUserNameExist(User testedUser) throws CostManagerException;
     /*
     * returns the ID a new user should receive
     * */
     int newIdNumberForNewUser() throws CostManagerException;
     /*
     used to add a new category
     * */
     int addNewCategory(Category newCategory,User currentUser) throws CostManagerException;
     /*
     * returns all user records in the database
     * */
     List<String> returnCostFromDb(User user) throws CostManagerException;
     /*
     * used to add a new record under a specific user
     * */
     int addNewCost(Cost newCost,User currentUser) throws CostManagerException;
     /*
     * return the user report with the required records
     * */
     List<String> returnUserReport(String StartDate,String endDate,User user) throws CostManagerException;
     /*
     * returns all the user categories
     * */
     List<String> ReturnAllTheCategoriesInTheDatabase(User currentUser) throws CostManagerException;




}
