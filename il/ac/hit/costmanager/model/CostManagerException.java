package il.ac.hit.costmanager.model;
/*
* The purpose of the class is to show to the user error message and exception
* */
public class CostManagerException extends Exception {
    /*
    *the method prints the error for future use
    * */
    CostManagerException(String msg, Exception e) {
        super(msg,e);
        System.out.println(msg);
    }
}
