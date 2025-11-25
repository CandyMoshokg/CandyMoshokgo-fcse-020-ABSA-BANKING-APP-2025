package com.banking.view;

import com.banking.controller.AccountController;
import com.banking.controller.CustomerController;
import com.banking.model.Customer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
//provides a JavaFX-based user interface for managing bank accounts
public class AccountManagementView extends Application {
    private AccountController accountController;
    private CustomerController customerController;
    private TextField customerIdField;
    private ComboBox<String> accountTypeCombo;
    private TextField initialBalanceField;
    private TextField branchField;
    private TextField companyNameField;
    private TextField companyAddressField;
    private VBox employmentBox;
    private Label messageLabel;
    private Label minBalanceLabel;
    private Stage primaryStage;
    //constructor initializes the view with controllers for account and customer management
    public AccountManagementView(AccountController accountController, CustomerController customerController) {
        this.accountController = accountController;
        this.customerController = customerController;
    }
    //starts the JavaFX application
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");
        root.setPadding(new Insets(20));
        
        VBox formBox = createAccountForm();
        root.setCenter(formBox);
        
        Scene scene = new Scene(root, 550, 700);
        primaryStage.setTitle("Open New Account");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //creates the account opening form UI
    private VBox createAccountForm() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(30));
        container.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        container.setMaxWidth(500);
        
        Label titleLabel = new Label("Open Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2196F3"));
        
        Separator separator = new Separator();
        
        Label customerIdLabel = new Label("Customer ID *");
        customerIdLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        HBox customerIdBox = new HBox(10);
        customerIdField = new TextField();
        customerIdField.setPromptText("Enter customer ID");
        styleTextField(customerIdField);
        HBox.setHgrow(customerIdField, Priority.ALWAYS);
        
        Button searchButton = new Button("🔍 Search");
        searchButton.setStyle(
            "-fx-background-color: #2196F3;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 15;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        searchButton.setOnAction(e -> searchCustomer());
        
        customerIdBox.getChildren().addAll(customerIdField, searchButton);
        
        Label accountTypeLabel = new Label("Account Type *");
        accountTypeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        accountTypeCombo = new ComboBox<>();
        accountTypeCombo.getItems().addAll("Savings Account", "Investment Account", "Cheque Account");
        accountTypeCombo.setPromptText("Select account type");
        accountTypeCombo.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(accountTypeCombo);
        accountTypeCombo.setOnAction(e -> handleAccountTypeChange());
        
        Label balanceLabel = new Label("Initial Balance (BWP) *");
        balanceLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        initialBalanceField = new TextField();
        initialBalanceField.setPromptText("Enter initial deposit amount");
        styleTextField(initialBalanceField);
        
        minBalanceLabel = new Label();
        minBalanceLabel.setFont(Font.font("Arial", 11));
        minBalanceLabel.setTextFill(Color.web("#FF9800"));
        
        Label branchLabel = new Label("Branch *");
        branchLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        branchField = new TextField();
        branchField.setText("Tsetsebjwe Main");
        branchField.setPromptText("Enter branch name");
        styleTextField(branchField);
        
        employmentBox = new VBox(10);
        employmentBox.setVisible(false);
        employmentBox.setManaged(false);
        
        Label companyNameLabel = new Label("Company Name *");
        companyNameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        companyNameField = new TextField();
        companyNameField.setPromptText("Employer's company name");
        styleTextField(companyNameField);
        
        Label companyAddressLabel = new Label("Company Address *");
        companyAddressLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        companyAddressField = new TextField();
        companyAddressField.setPromptText("Employer's address");
        styleTextField(companyAddressField);
        
        employmentBox.getChildren().addAll(
            companyNameLabel, companyNameField,
            companyAddressLabel, companyAddressField
        );
        
        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(450);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button openButton = new Button("Open Account");
        openButton.setStyle(
            "-fx-background-color: #2196F3;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 30;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        openButton.setOnAction(e -> handleOpenAccount());
        
        Button clearButton = new Button("Clear");
        clearButton.setStyle(
            "-fx-background-color: #f44336;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 30;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        clearButton.setOnAction(e -> clearForm());
        
        Button closeButton = new Button("Close");
        closeButton.setStyle(
            "-fx-background-color: #9E9E9E;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 30;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> primaryStage.close());
        
        buttonBox.getChildren().addAll(openButton, clearButton, closeButton);
        
        container.getChildren().addAll(
            titleLabel,
            separator,
            new VBox(5, customerIdLabel, customerIdBox),
            new VBox(5, accountTypeLabel, accountTypeCombo),
            new VBox(5, balanceLabel, initialBalanceField, minBalanceLabel),
            new VBox(5, branchLabel, branchField),
            employmentBox,
            messageLabel,
            buttonBox
        );
        
        return container;
    }
    private void styleTextField(TextField field) {
    field.setStyle(
        "-fx-padding: 10;" +
        "-fx-background-radius: 5;" +
        "-fx-border-color: #ddd;" +
        "-fx-border-radius: 5;"
    );
    field.setFont(Font.font("Arial", 13));
}
//styles the combo box UI element
private void styleComboBox(ComboBox<?> comboBox) {
    comboBox.setStyle(
        "-fx-padding: 10;" +
        "-fx-background-radius: 5;" +
        "-fx-border-color: #ddd;" +
        "-fx-border-radius: 5;" +
        "-fx-font-size: 13;" +
        "-fx-font-family: Arial;"
    );
}
    //
    private void handleAccountTypeChange() {
        String selectedType = accountTypeCombo.getValue();
        
        if (selectedType == null) return;
        
        boolean isCheque = selectedType.equals("Cheque Account");
        employmentBox.setVisible(isCheque);
        employmentBox.setManaged(isCheque);
        
        if (selectedType.equals("Investment Account")) {
            minBalanceLabel.setText("Minimum balance: P 500.00");
        } else {
            minBalanceLabel.setText("");
        }
    }
    //searches for a customer by ID and displays the result
    private void searchCustomer() {
        String customerId = customerIdField.getText().trim();
        
        if (customerId.isEmpty()) {
            showError("Please enter a customer ID");
            return;
        }
        
        var result = customerController.getCustomer(customerId);
        
        if (result.isSuccess()) {
            Customer customer = result.getCustomer();
            showSuccess("Customer found: " + customer.getFirstName() + " " + customer.getSurname());
        } else {
            showError(result.getMessage());
        }
    }
    //handles the account opening process when the user clicks the button
    private void handleOpenAccount() {
        messageLabel.setText("");
        
        String customerId = customerIdField.getText().trim();
        String accountType = accountTypeCombo.getValue();
        String balanceStr = initialBalanceField.getText().trim();
        String branch = branchField.getText().trim();
        
        if (customerId.isEmpty() || accountType == null || balanceStr.isEmpty() || branch.isEmpty()) {
            showError("Please fill everything");
            return;
        }
        
        double initialBalance;
        try {
            initialBalance = Double.parseDouble(balanceStr);
        } catch (NumberFormatException e) {
            showError("Invalid balance amount. Please enter a valid number");
            return;
        }
        
        var result = switch (accountType) {
            case "Savings Account" -> 
                accountController.openSavingsAccount(customerId, initialBalance, branch);
            case "Investment Account" -> 
                accountController.openInvestmentAccount(customerId, initialBalance, branch);
            case "Cheque Account" -> {
                String companyName = companyNameField.getText().trim();
                String companyAddress = companyAddressField.getText().trim();
                
                if (companyName.isEmpty() || companyAddress.isEmpty()) {
                    showError("Company name and address are required for Cheque accounts");
                    yield null;
                }
                
                yield accountController.openChequeAccount(customerId, initialBalance, branch, 
                    companyName, companyAddress);
            }
            default -> null;
        };
        
        if (result != null && result.isSuccess()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Account Opened Successfully!");
            alert.setContentText(
                "Account Number: " + result.getAccount().getAccountNumber() + "\n" +
                "Account Type: " + result.getAccount().getAccountType() + "\n" +
                "Initial Balance: BWP " + String.format("%.2f", result.getAccount().getBalance())
            );
            alert.showAndWait();
            
            clearForm();
        } else if (result != null) {
            showError(result.getMessage());
        }
    }
    //clears the form fields
    private void clearForm() {
        customerIdField.clear();
        accountTypeCombo.setValue(null);
        initialBalanceField.clear();
        branchField.setText("Gaborone Main");
        companyNameField.clear();
        companyAddressField.clear();
        messageLabel.setText("");
        minBalanceLabel.setText("");
        employmentBox.setVisible(false);
        employmentBox.setManaged(false);
    }
    //displays an error message to the user
    private void showError(String message) {
        messageLabel.setText("❌ " + message);
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    }
    //displays a success message to the user
    private void showSuccess(String message) {
        messageLabel.setText("✓ " + message);
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    }
    //launches the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}