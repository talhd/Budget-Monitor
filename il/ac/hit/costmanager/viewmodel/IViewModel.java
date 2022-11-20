package il.ac.hit.costmanager.viewmodel;

import il.ac.hit.costmanager.model.Category;
import il.ac.hit.costmanager.model.Cost;
import il.ac.hit.costmanager.model.IModel;
import il.ac.hit.costmanager.model.User;
import il.ac.hit.costmanager.view.IView;

public interface IViewModel {
        /*
        * set new view for the viewModel
        * */
        void setView(IView view);
        /*
        * set new model for the viewModel
        * */
        void setModel(IModel model);
        /*
        * used to add a new user to the system
        * */
        void addNewUser(User newUser);
        /*
        * Check if there is a user with the details in the database
        * */
        void userValidation(User loginUser);
        /*
        * Add a new category associated with the user
        * */
        void addNewCategory(Category newCategory,User currentUser);
        /*
        * Returns all user records from the database
        * */
        void returnCostFromDb(User user);
        /*
        * Add a new record under user
        * */
        void addNewCost(Cost newCost,User currentUser);
        /*
        * Return the user report with the required records
        * */
        void returnUserReport(String StartDate,String endDate,User user);
        /*
        * Returns all the user categories
        * */
        void returnAllTheCategories(User currentUser);
}
