package il.ac.hit.costmanager.view;
import il.ac.hit.costmanager.viewmodel.IViewModel;
import java.util.List;
public interface IView {
    /*
     * set new viewmodel for the view
     * */
    void setIViewModel(IViewModel vm);
    /*
     * Initializing the program view
     * */
    void init();
    /*
     * Creating the login screen
     * */
    void start();
    /*
     * Showing string on the main screen
     * */
    void showItems(List<String> list);
    /*
     * Creating and entering the main screen
     * */
    void openTheMainScreen();
    /*
     * Creates a popup with a message inside
     * */
    void showMessage(String Message);
    /*
     * Creating the report screen
     * */
    void openReportScreen(List <String> records);
    /*
     * Gets all the categories from the database
     * */
    void getAllCategories(List <String> categories);
    /*
     * Creating the screen where you add a new category
     * */
    void addCategoryScreen();
    /*
     * Creating the create report screen
     * */
    void reportScreen();
}