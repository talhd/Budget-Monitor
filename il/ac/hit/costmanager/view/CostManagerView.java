package il.ac.hit.costmanager.view;
import il.ac.hit.costmanager.model.*;
import il.ac.hit.costmanager.viewmodel.IViewModel;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class CostManagerView implements IView
{
    /*
    All private variables
    * */
    private JLabel category, sum, currency, description, date, startDate, endDate, lblCategory;
    private JButton btLogin, btCreateAccount, btAddNewCost, btCreateReport, btCreateReport2, btAddCategory, btCreateCategory2, btnGoBack, btLogout;
    private JTextField tfPassword,tfUsername, tfSum, tfCurrency, tfDescription, tfDate, tfStartDate, tfEndDate, tfAddCategory;
    private JFrame frame, mainFrame, reportFrame, categoryFrame, reportFrame2;
    private JPanel panelSouth, reportPanelNorth, reportPanelCenter, reportPanelSouth, categoryPanelNorth, categoryPanelCenter, panelNorth, panelSouth2, openReportScreenPanel, openReportScreenPanelSouth, panelCenter;
    private JTextArea taShowsUserCosts, taShowsUserReportCosts;
    private IViewModel vm;
    private static User user;
    private Cost cost;
    private Category categoryInput;
    private LocalDate dateInput;
    private static int userId;
    private Category newCategory;
    private static JFrame generalFrame;
    private static JList<String> listOfCategories;

    /*
    Creating a view model object
    * */
    @Override
    public void setIViewModel(IViewModel vm)
    {
        this.vm = vm;
    }
    /*
     First initialization of the program view
    * */
    @Override
    public void init()
    {
        panelCenter = new JPanel();
        panelNorth = new JPanel();
        panelSouth2 = new JPanel();
        frame = new JFrame();
        generalFrame = new JFrame();
        tfUsername = new JTextField(10);
        tfPassword = new JTextField(10);
    }

    /*
     Creating the login screen
     * */
    @Override
    public void start()
    {
        JLabel password1, label;
        panelCenter.setLayout(new FlowLayout());
        panelNorth.setLayout(new FlowLayout());
        panelSouth2.setLayout(new FlowLayout());
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Login page");
        frame.setSize(new Dimension(400, 250));
        frame.add(panelNorth, BorderLayout.NORTH);
        frame.add(panelCenter, BorderLayout.CENTER);
        frame.add(panelSouth2, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        generalFrame = frame;
        label = new JLabel("Username");
        panelNorth.add(label, BorderLayout.CENTER);
        panelNorth.add(tfUsername, BorderLayout.CENTER);
        password1 = new JLabel("Password");
        panelCenter.add(password1, BorderLayout.CENTER);
        panelCenter.add(tfPassword, BorderLayout.CENTER);
        btLogin = new JButton("Login");
        panelSouth2.add(btLogin, BorderLayout.CENTER);
        btCreateAccount = new JButton("Create account");
        panelSouth2.add(btCreateAccount, BorderLayout.CENTER);
        frame.setVisible(true);

        /*
        Creates a user obj and sends the login info to the vm for login
        * */
        btLogin.addActionListener(e -> {
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            user = new User(username, password);
            vm.userValidation(user);
        });

        /*
        Creates a user obj and sends the new info to the vm to create an account
        * */
        btCreateAccount.addActionListener(e -> {
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            user = new User(username, password);
            vm.addNewUser(user);
        });
    }

    /*
    Creating and entering the main screen
    * */
    @Override
    public void openTheMainScreen()
    {

        frame.dispose();
        vm.returnCostFromDb(user);
        listOfCategories = new JList<>();
        vm.returnAllTheCategories(user);
        listOfCategories.setSelectedIndex(0);
        listOfCategories.setVisibleRowCount(4);
        listOfCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainFrame = new JFrame();
        btAddNewCost = new JButton("Add new cost");
        btCreateReport = new JButton("Create report");
        btAddCategory = new JButton("Add new category");
        btLogout = new JButton("Logout");
        taShowsUserCosts = new JTextArea();
        panelSouth = new JPanel();
        tfSum = new JTextField(10);
        tfCurrency = new JTextField(10);
        tfDescription = new JTextField(10);
        tfDate = new JTextField(10);
        category = new JLabel("Category:");
        sum = new JLabel("Sum:");
        currency = new JLabel("Currency:");
        description = new JLabel("Description:");
        date = new JLabel("Date(xx/xx/xxxx):");
        taShowsUserCosts.setFont(new Font("Arial", Font.PLAIN, 25));
        taShowsUserCosts.setEditable(false);
        panelSouth.setLayout(new FlowLayout());
        panelSouth.add(category);
        panelSouth.add(new JScrollPane(listOfCategories));
        panelSouth.add(sum);
        panelSouth.add(tfSum);
        panelSouth.add(currency);
        panelSouth.add(tfCurrency);
        panelSouth.add(description);
        panelSouth.add(tfDescription);
        panelSouth.add(date);
        panelSouth.add(tfDate);
        panelSouth.add(btAddNewCost);
        panelSouth.add(btCreateReport);
        panelSouth.add(btAddCategory);
        panelSouth.add(btLogout);

        /*
        Sends all the cost details to the vm
        * */
        btAddNewCost.addActionListener(e -> {
            try {
                String userCategoryInput = listOfCategories.getSelectedValue();
                categoryInput = new Category(userCategoryInput);
                BigDecimal sumInput = new BigDecimal(tfSum.getText());
                String currencyInput = tfCurrency.getText();
                String descriptionInput = tfDescription.getText();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dateInput = LocalDate.parse(tfDate.getText(), dateFormat);
                userId = user.getUserId();
                cost = new Cost(sumInput, currencyInput, categoryInput, descriptionInput, dateInput, userId);
                vm.addNewCost(cost,user);
            }
            catch (NumberFormatException a){
                showMessage("The amount must be a number and not a text");
            }
            catch(DateTimeException b){
                showMessage("Date does not match the template xx/xx/xxxx");
            }
        });
        /*
        Entering the create report screen
        * */
        btCreateReport.addActionListener(e -> {
            mainFrame.dispose();
            reportScreen();
        });
        /*
        Entering the add category screen
        * */
        btAddCategory.addActionListener(e -> {
            mainFrame.dispose();
            addCategoryScreen();
        });
        /*
        Returning to the log in screen
        * */
        btLogout.addActionListener(e -> {
            mainFrame.dispose();
            init();
            start();
        });

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(panelSouth,BorderLayout.SOUTH);
        mainFrame.add(new JScrollPane(taShowsUserCosts),BorderLayout.CENTER);
        mainFrame.setTitle("Cost manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(1500, 600));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        generalFrame = mainFrame;
    }

    /*
    Showing string on the main screen
    * */
    @Override
    public void showItems(List<String> list)
    {
        StringBuilder sb = new StringBuilder();

        for(String item : list)
        {
            sb.append(item).append("\n").append("----------------------------------------------").append("\n");
        }

        taShowsUserCosts.setText(sb.toString());
    }
    /*
    Creating the create report screen
    * */
    @Override
    public void reportScreen()
    {
        reportFrame = new JFrame();
        reportFrame.setLayout(new BorderLayout());

        reportPanelNorth = new JPanel();
        reportPanelNorth.setLayout(new FlowLayout());
        reportPanelCenter = new JPanel();
        reportPanelCenter.setLayout(new FlowLayout());
        reportPanelSouth = new JPanel();
        reportPanelSouth.setLayout(new FlowLayout());

        reportFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        reportFrame.setTitle("Create a report");
        reportFrame.setSize(new Dimension(400, 250));
        reportFrame.add(reportPanelNorth, BorderLayout.NORTH);
        reportFrame.add(reportPanelCenter, BorderLayout.CENTER);
        reportFrame.add(reportPanelSouth, BorderLayout.SOUTH);

        tfStartDate = new JTextField(10);
        tfEndDate = new JTextField(10);

        generalFrame = reportFrame;

        startDate = new JLabel("Start date:");
        reportPanelNorth.add(startDate);
        reportPanelNorth.add(tfStartDate);

        endDate = new JLabel("End date:");
        reportPanelCenter.add(endDate);
        reportPanelCenter.add(tfEndDate);

        btCreateReport2 = new JButton("Create report");
        reportPanelSouth.add(btCreateReport2);

        btnGoBack = new JButton("Go back");
        reportPanelSouth.add(btnGoBack);
        /*
        Sends the report info to the vm
        * */
        btCreateReport2.addActionListener(e -> {
            String startDate = tfStartDate.getText();
            String endDate = tfEndDate.getText();
            reportFrame.dispose();
            vm.returnUserReport(startDate, endDate, user);
        });
        /*
        Returns to the main screen
        * */
        btnGoBack.addActionListener(e -> {
            reportFrame.dispose();
            openTheMainScreen();
        });
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setVisible(true);
    }
    /*
    Creating the screen where you add a new category
    * */
    @Override
    public void addCategoryScreen()
    {
        categoryFrame = new JFrame();
        categoryFrame.setLayout(new BorderLayout());
        categoryPanelNorth = new JPanel();
        categoryPanelCenter = new JPanel();
        tfAddCategory = new JTextField(10);
        categoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        categoryFrame.setTitle("Add a category");
        categoryFrame.setSize(new Dimension(400, 250));
        categoryFrame.add(categoryPanelNorth, BorderLayout.NORTH);
        categoryFrame.add(categoryPanelCenter, BorderLayout.CENTER);
        generalFrame = categoryFrame;
        lblCategory = new JLabel("New category:");
        categoryPanelNorth.add(lblCategory);
        categoryPanelNorth.add(tfAddCategory);
        btCreateCategory2 = new JButton("Add category");
        categoryPanelCenter.add(btCreateCategory2);
        btnGoBack = new JButton("Go back");
        categoryPanelCenter.add(btnGoBack);
        /*
        Sends the new category name to the vm
        * */
        btCreateCategory2.addActionListener(e -> {
            newCategory = new Category(tfAddCategory.getText());
            vm.addNewCategory(newCategory,user);
        });
        /*
        Returning to the main screen
        * */
        btnGoBack.addActionListener(e -> {
            categoryFrame.dispose();
            openTheMainScreen();
        });
        categoryFrame.setLocationRelativeTo(null);
        categoryFrame.setVisible(true);
    }

    /*
    Creates a popup with a message inside
    * */
    @Override
    public void showMessage(String message)
    {
        JOptionPane.showMessageDialog(generalFrame, message);
    }

    /*
    Creating the report screen
    * */
    @Override
    public void openReportScreen(List <String> records)
    {
        taShowsUserReportCosts = new JTextArea();
        reportFrame2 = new JFrame();
        btnGoBack = new JButton("Go back");
        openReportScreenPanel = new JPanel();
        openReportScreenPanelSouth = new JPanel();
        openReportScreenPanel.add(taShowsUserReportCosts);
        openReportScreenPanelSouth.add(btnGoBack);

        /*
        Showing the costs details on screen
        * */
        StringBuilder sb = new StringBuilder();
        for(String item : records)
        {
            sb.append(item).append("\n").append("----------------------------------------------").append("\n");
        }

        taShowsUserReportCosts.setEditable(false);
        taShowsUserReportCosts.setText(sb.toString());
        /*
        Returning to main screen
        * */
        btnGoBack.addActionListener(e -> {
            reportFrame2.dispose();
            openTheMainScreen();
        });

        reportFrame2.setTitle("User's report");
        reportFrame2.setLayout(new BorderLayout());
        reportFrame2.add(openReportScreenPanel, BorderLayout.CENTER);
        reportFrame2.add(openReportScreenPanelSouth, BorderLayout.SOUTH);
        reportFrame2.setSize(500,400);
        reportFrame2.setLocationRelativeTo(null);
        reportFrame2.setVisible(true);
    }

    /*
    Gets all the categories from the database
    * */
    @Override
    public void getAllCategories(List<String> categories)
    {
        listOfCategories.setListData(categories.toArray(new String[0]));
    }
}