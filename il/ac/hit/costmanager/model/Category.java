package il.ac.hit.costmanager.model;
/*
* Class for Category objects.
* when we need to use a category,we will not use a simple string, we will use a class object
* */
public class Category {
    /*
    * First initialize of category
    * */
    private String category =null;
    /*
    * Category constructor
    * */
    public Category(String category){
        this.setCategory(category);
    }
    /*
    * get category-returns the category name
    * */
    public String getCategory(){
        return category;
    }
    /*
    *In addition to the constructor the method allows for rename the category
    * */
    public void setCategory(String category){
        this.category =category;
    }




}
