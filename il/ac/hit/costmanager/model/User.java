package il.ac.hit.costmanager.model;
/*
* the class used to create a user object
* each user will be represented by an object,not by simple variables only.
* */
public class User {
    /*
    * username variable to save the username for use
    * */
    private String username=null;
    /*
    * a variable used to store the user's password in the system
    * */
    private String password=null;
    /*
    * a variable used to save user id
    * */
    private int userId=0;
    /*
    * User constructor,to initialize the user information
    * */
    public User(String username,String password){
        this.setUsername(username);
        this.setPassword(password);
        }
    /*
    * set of get methods-are used to get the variables of the object
    * */
    public String getUsername(){
        return username;
        }
    public String getPassword(){
        return password;
        }
    public int getUserId(){
        return userId;
         }
    /*
    * set of set methods-are used to update the variables belonging to a particular object
    * */
    public void setUsername(String username){
        this.username=username;
        }
    public void setPassword(String password){
        this.password=password;
        }
    public void setUserId(int userId){
        this.userId=userId;
        }

}
