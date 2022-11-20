package il.ac.hit.costmanager;
import il.ac.hit.costmanager.model.*;
import il.ac.hit.costmanager.view.*;
import il.ac.hit.costmanager.viewmodel.*;
import javax.swing.*;
public class Main {
    static public void main(String [] args){
        IView view = new CostManagerView();
        IViewModel vm = new CostManagerViewModel();
        IModel model = new CostManagerModel();
        SwingUtilities.invokeLater(()->{
        view.init();
        view.start();});
        view.setIViewModel(vm);
        vm.setModel(model);
        vm.setView(view);
    }
}