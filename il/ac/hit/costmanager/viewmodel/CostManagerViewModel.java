package il.ac.hit.costmanager.viewmodel;
import il.ac.hit.costmanager.model.*;
import il.ac.hit.costmanager.view.IView;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class CostManagerViewModel implements IViewModel {
        /*
        * Model-the specific object which ViewModel use
        * */
        private IModel Model;
        /*
        *view-the View object which ViewModel use
         * */
        private IView view;
        /*
        * service-use ExecutorService interface to execute tasks on threads asynchronously
        * */
        private final ExecutorService service;
        /*
        *Constructor used for initialization service,creates thread Pool of 5 thread
        * */
        public CostManagerViewModel(){
            this.service = Executors.newFixedThreadPool(5);
        }
        /*
        * a method used to initialize an view object
        * */
        public void setView(IView view){
            this.view=view;
        }
        /*
        * a method used to initialize an model object
         * */
        public void setModel(IModel model){
            this.Model=model;
        }
    @Override
    /*
    * Gets the details from the view and trying to register a new user with model method.
    * if there is an error, the user is given the information he needs to fix the problem.
    * */
    public void addNewUser(User newUser) {
        service.submit(() -> {
            try {
                int result=Model.addNewUser(newUser);
                SwingUtilities.invokeLater(() -> {
                    if(result==1){
                        view.openTheMainScreen();
                    }
                    if (result==-1){
                        view.showMessage("username already in use,please select another username");
                    }
                    if (result==0){
                        view.showMessage("username or password is longer than 10 characters");
                    }
                    if(result==-2){
                        view.showMessage("You must provide both a username and a password");
                    }
                }
                );
            }
             catch (CostManagerException e) {
                 SwingUtilities.invokeLater(() -> view.showMessage(e.getMessage()));
        }

}) ;}
    @Override
    /*
    * Checking the user to determine whether to log in or not.
    * if there is an error, the user is given the information he needs to fix the problem.
    * */
    public void userValidation(User loginUser) {
        service.submit(() -> {
            try {
                boolean result=Model.userValidation(loginUser);
                SwingUtilities.invokeLater(() -> {
                    if (result){
                        view.openTheMainScreen();
                        //Authentication was successful, the main screen can be opened for the user
                    }
                    else {
                        view.showMessage("There is no user with the above details");
                        //Displays an error message to the user
                    }
                }
                );

            }
            catch (CostManagerException e) {
                //return to user the problem
                SwingUtilities.invokeLater(() -> view.showMessage(e.getMessage()));

            }
        });

    }
    /*
    * add a new category associated with the user
    * if there is an error, the user is given the information he needs to fix the problem.
    * and if the category was successfully added,notify the user.
    * */
    @Override
    public void addNewCategory(Category newCategory,User user) {
        service.submit(() -> {
            try {
                int result=Model.addNewCategory(newCategory,user);
                SwingUtilities.invokeLater(() -> {
                    if(result==0){
                        view.showMessage("The category already exists");
                    }
                    else {
                        view.showMessage("The category was successfully added");
                    }
                });
            }
            catch (CostManagerException e) {
                SwingUtilities.invokeLater(() -> view.showMessage(e.getMessage()));
            }
        });
    }
    /*
    * Returns all user records from the database.
    * if there is an error, the user is given the information he needs to fix the problem.
    * */
    @Override
    public void returnCostFromDb(User user) {
        service.submit(() -> {
            try {
                List <String> AllRecordsOfUserFromDb=Model.returnCostFromDb(user);
                SwingUtilities.invokeLater(() -> {
                    if(AllRecordsOfUserFromDb.size() != 0){
                        view.showItems(AllRecordsOfUserFromDb);
                    }
                });

            }
            catch (CostManagerException e) {
                SwingUtilities.invokeLater(() -> view.showMessage(e.getMessage()));
            }
        });
    }
    /*
    * Add a new record under user
    * if there is an error, the user is given the information he needs to fix the problem.
    * */
    @Override
    public void addNewCost(Cost newCost,User user) {
        service.submit(() -> {
            try {
                int result=Model.addNewCost(newCost,user);
                SwingUtilities.invokeLater(() -> {
                    if (result==1){
                        returnCostFromDb(user);
                    }
                    else {
                        view.showMessage("User does not exist");
                    }
                });

            }
            catch (CostManagerException e) {
                SwingUtilities.invokeLater(() -> view.showMessage(e.getMessage()));
            }
        });
    }
    /*
    * Return the user report with the required records
    * if there is an error, the user is given the information he needs to fix the problem.
    * */
    @Override
    public void returnUserReport(String StartDate, String endDate, User user) {
        service.submit(() -> {
            try {
                List <String> ReportResult=Model.returnUserReport(StartDate,endDate,user);
                SwingUtilities.invokeLater(() -> {
                    if(ReportResult.size() != 0){
                        view.openReportScreen(ReportResult);
                    }
                    else {
                        view.showMessage("There are no records between the dates");
                    }
                });
            }
            catch (CostManagerException e) {
                SwingUtilities.invokeLater(() -> view.showMessage(e.getMessage()));
            }
        });
    }
    @Override
    /*
    * Returns all the user categories
    * if there is an error, the user is given the information he needs to fix the problem.
    * */
    public void returnAllTheCategories(User user){
        service.submit(() -> {
            try {
                List <String> Categories=Model.ReturnAllTheCategoriesInTheDatabase(user);
                SwingUtilities.invokeLater(() -> {
                    if (Categories.size() != 0){
                        view.getAllCategories(Categories);
                    }
                    else {
                        view.showMessage("There are no categories to display");
                    }
                });
            }
            catch (CostManagerException e) {
                SwingUtilities.invokeLater(() -> view.showMessage(e.getMessage()));
            }
        });
    }
}
