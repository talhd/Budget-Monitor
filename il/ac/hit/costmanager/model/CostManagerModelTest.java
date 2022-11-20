package il.ac.hit.costmanager.model;
import java.math.BigDecimal;
import java.sql.*;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class CostManagerModelTest {
    private CostManagerModel costManagerModel;

    @org.junit.Before
    public void setUp() throws Exception {
        costManagerModel = new CostManagerModel();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE user_table, categories FROM user_table INNER JOIN categories");
            preparedStatement.executeUpdate();
            costManagerModel.closeConnectionToServer(connection);
        } catch (SQLException e) {
            System.out.println("Could not truncate test_table " + e.getMessage());
        }
        costManagerModel = null;
    }

    @org.junit.Test
    public void connectToDatabase() {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            assertNotNull(connection);
        } catch (Exception e) {
            fail();
        }
    }

    @org.junit.Test
    public void closeConnectionToServer() {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            costManagerModel.closeConnectionToServer(connection);
            assertNotNull(connection);
            assertTrue(connection.isClosed());
        } catch (Exception e) {
            fail();
        }
    }

    @org.junit.Test
    public void addNewUser() {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            User testUser = new User("moshe123", "password1");
            costManagerModel.addNewUser(testUser);
            int actual = costManagerModel.addNewUser(testUser);
            connection.prepareStatement("SELECT Username FROM user_table");
            assertEquals(-1, actual);
        } catch (Exception e) {
            fail();
        }
    }

    @org.junit.Test
    public void userValidation() {
        try {
            costManagerModel.connectToDatabase();
            User testUser1 = new User("moshe123", "password1");
            User testUser2 = new User("moshe123", "password1");
            costManagerModel.addNewUser(testUser1);
            assertTrue(costManagerModel.userValidation(testUser2));
        }
        catch (Exception e){
            fail();
        }
    }

    @org.junit.Test
    public void doesUsernameExist() {
        try {
            costManagerModel.connectToDatabase();
            User testUser1 = new User("moshe1234", "123");
            User testUser2 = new User("moshe1234", "123");
            User testUser3 = new User("moshe12345", "123");

            costManagerModel.addNewUser(testUser1);
            assertTrue(costManagerModel.doesUserNameExist(testUser2));
            assertFalse(costManagerModel.doesUserNameExist(testUser3));
        }
        catch (Exception e){
            fail();
        }
    }

    @org.junit.Test
    public void newIdNumberForNewUser() {
        try {
            costManagerModel.connectToDatabase();
            User testUser1 = new User("moshe123", "123");
            User testUser2 = new User("moshe12", "123");
            int actual = costManagerModel.newIdNumberForNewUser();
            assertEquals(2, actual);

        } catch (CostManagerException e){
            fail();
        }
    }


    @org.junit.Test
    public void addNewCategory() {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            User testUser = new User("moshe123", "123");
            Category category = new Category("newCategory");
            Category category2 = new Category("newCategory");

            costManagerModel.addNewCategory(category, testUser);
            int actual = costManagerModel.addNewCategory(category2, testUser);

            assertEquals(0, actual);

        } catch (CostManagerException e) {
            fail();
        }
    }

    @org.junit.Test
    public void returnCostFromDb() {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            User testUser = new User("moshe123", "123");
            costManagerModel.addNewUser(testUser);
            Category category = new Category("mitsi");
            costManagerModel.addNewCategory(category, testUser);
            BigDecimal sum = new BigDecimal(200);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date= LocalDate.parse("25/12/2021", dateFormat);
            Cost cost = new Cost(sum, "euro", category, "hiiii", date, 1);
            costManagerModel.addNewCost(cost, testUser);
            List<String> costList = costManagerModel.returnCostFromDb(testUser);
            String actual = costList.get(1);
            assertEquals("200 euro mitsi hiiii 25/12/2021", actual);

        } catch (CostManagerException e) {
            fail();
        }
    }

    @org.junit.Test
    public void addNewCost() {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            User testUser = new User("moshe123", "123");
            costManagerModel.addNewUser(testUser);
            Category category =new Category("categorymitzyitdgtt");
            costManagerModel.addNewCategory(category,testUser);
            costManagerModel.addNewCategory(category, testUser);
            BigDecimal sum = new BigDecimal(40);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date= LocalDate.parse("23/03/2012", dateFormat);
            Cost cost = new Cost(sum, "euro", category, "shalom", date, 1);
            int actual = costManagerModel.addNewCost(cost, testUser);
            assertEquals(1, actual);

        } catch (CostManagerException e) {
            fail();
        }
    }

    @org.junit.Test
    public void returnUserReport() {
        try {
            Connection connection = costManagerModel.connectToDatabase();
            User testUser = new User("moshe123", "123");
            costManagerModel.addNewUser(testUser);
            Category category = new Category("category");
            costManagerModel.addNewCategory(category, testUser);
            BigDecimal sum1 = new BigDecimal(40);
            BigDecimal sum2 = new BigDecimal(40);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date1= LocalDate.parse("23/03/2012", dateFormat);
            LocalDate date2= LocalDate.parse("25/05/2020", dateFormat);
            Cost cost = new Cost(sum1, "euro", category, "shalom", date1, 1);
            Cost cost2 = new Cost(sum2, "dollar", category, "hiii", date2, 1);
            costManagerModel.addNewCost(cost, testUser);
            costManagerModel.addNewCost(cost2, testUser);
            List<String> listReport = costManagerModel.returnUserReport("25/05/2020", "25/05/2020", testUser);
            String actual = (listReport.get(0));
            assertEquals("40 dollar category hiii 25/05/2020", actual);

        } catch (CostManagerException e) {
            fail();
        }
    }

    @org.junit.Test
    public void returnAllTheCategoriesInTheDatabase() {
        try{
            Connection connection = costManagerModel.connectToDatabase();
            User testUser = new User("moshe123", "123");
            Category category = new Category("category");
            Category category2 = new Category("category2");
            Category category3 = new Category("category3");

            costManagerModel.addNewCategory(category, testUser);
            costManagerModel.addNewCategory(category2, testUser);
            costManagerModel.addNewCategory(category3, testUser);

            List<String> listOfCategories = costManagerModel.ReturnAllTheCategoriesInTheDatabase(testUser);
            String actual = listOfCategories.get(0);

            assertEquals("category", actual);

        } catch (CostManagerException e) {
            e.printStackTrace();
        }
    }
}