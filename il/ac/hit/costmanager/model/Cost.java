package il.ac.hit.costmanager.model;
import java.math.BigDecimal;
import java.time.LocalDate;
/*
* All the class objects used to represent user records(cost)
* */
public class Cost {
    //used to hold the amount spent by the user
    private BigDecimal amountOfExpense;
    //type of currency used
    private String currency=null;
    //selected category for record(Category object)
    private Category category=null;
    //text that the user adds to describe the record
    private String text=null;
    //record date
    private LocalDate date=null;
    private int userId=0;
    /*
    * constructor-uses to initialize a cost object.
    * must have all the argument a record needs
    * */
    public Cost(BigDecimal amountOfExpense, String currency, Category category, String text, LocalDate date, int userId) {
        this.setAmountOfExpense(amountOfExpense);
        this.setCurrency(currency);
        this.setCategory(category);
        this.setText(text);
        this.setDate(date);
        this.setUserId(userId);
    }
/*
* Set methods-are used to initialize the class variables,each variable has a suitable method.
* */
    public void setAmountOfExpense(BigDecimal amountOfExpense) {
        this.amountOfExpense = amountOfExpense;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
/*
* Set of get methods-are used to return class variables,each variable has a method respectively.
* */
    public BigDecimal getAmountOfExpense() {
        return amountOfExpense;
    }

    public String getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }


}
