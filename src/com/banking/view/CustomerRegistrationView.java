package com.banking.view;

import com.banking.controller.CustomerController;
import com.banking.controller.CustomerController.CustomerResult;
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
//this class provides a JavaFX UI for registering new customers
public class CustomerRegistrationView extends Application {
    private CustomerController customerController;
    private TextField firstNameField;
    private TextField surnameField;
    private TextArea addressArea;
    private TextField phoneField;
    private TextField emailField;
    private Label messageLabel;
    private Stage primaryStage;
    //constructor initializes the view with the customer controller
    public CustomerRegistrationView(CustomerController customerController) {
        this.customerController = customerController;
    }
    //starts the JavaFX application
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");
        root.setPadding(new Insets(20));
        
        VBox formBox = createRegistrationForm();
        root.setCenter(formBox);
        
        Scene scene = new Scene(root, 500, 650);
        primaryStage.setTitle("Register New Customer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //creates the registration form layout
    private VBox createRegistrationForm() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(30));
        container.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        container.setMaxWidth(450);
        
        Label titleLabel = new Label("Register New Customer");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#4CAF50"));
        //creates a visual separator
        Separator separator = new Separator();
        
        Label firstNameLabel = new Label("First Name *");
        firstNameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");
        styleTextField(firstNameField);
        
        Label surnameLabel = new Label("Surname *");
        surnameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        surnameField = new TextField();
        surnameField.setPromptText("Enter surname");
        styleTextField(surnameField);
        
        Label addressLabel = new Label("Address *");
        addressLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        addressArea = new TextArea();
        addressArea.setPromptText("Enter physical address");
        addressArea.setPrefRowCount(3);
        addressArea.setWrapText(true);
        styleTextField(addressArea);
        
        Label phoneLabel = new Label("Phone Number");
        phoneLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        phoneField = new TextField();
        phoneField.setPromptText("e.g., 76007107)");
        styleTextField(phoneField);
        
        Label emailLabel = new Label("Email Address");
        emailLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        emailField = new TextField();
        emailField.setPromptText("e.g., candy@test.bw");
        styleTextField(emailField);
        
        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button registerButton = new Button("Register Customer");
        registerButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 30;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        registerButton.setOnAction(e -> handleRegistration());
        
        Button clearButton = new Button("Clear Form");
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
        
        buttonBox.getChildren().addAll(registerButton, clearButton, closeButton);
        
        Label requiredLabel = new Label("* Required fields");
        requiredLabel.setFont(Font.font("Arial", 10));
        requiredLabel.setTextFill(Color.GRAY);
        
        container.getChildren().addAll(
            titleLabel,
            separator,
            new VBox(5, firstNameLabel, firstNameField),
            new VBox(5, surnameLabel, surnameField),
            new VBox(5, addressLabel, addressArea),
            new VBox(5, phoneLabel, phoneField),
            new VBox(5, emailLabel, emailField),
            requiredLabel,
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
private void styleTextField(TextArea field) {
    field.setStyle(
        "-fx-padding: 10;" +
        "-fx-background-radius: 5;" +
        "-fx-border-color: #ddd;" +
        "-fx-border-radius: 5;" +
        "-fx-font-family: 'Arial'; -fx-font-size: 13px;"
    );
}

    //handles the registration logic when the register button is clicked
    private void handleRegistration() {
        messageLabel.setText("");
        
        String firstName = firstNameField.getText().trim();
        String surname = surnameField.getText().trim();
        String address = addressArea.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        
        CustomerResult result = customerController.registerCustomer(
            firstName, surname, address, phone, email
        );
        
        if (result.isSuccess()) {
            showSuccess(result.getMessage());
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Customer Registered Successfully!");
            alert.setContentText(
                "Customer ID: " + result.getCustomer().getCustomerId() + "\n" +
                "Name: " + result.getCustomer().getFirstName() + " " + result.getCustomer().getSurname()
            );
            alert.showAndWait();
            
            clearForm();
            
        } else {
            showError(result.getMessage());
        }
    }
    //clears the form fields
    private void clearForm() {
        firstNameField.clear();
        surnameField.clear();
        addressArea.clear();
        phoneField.clear();
        emailField.clear();
        messageLabel.setText("");
        firstNameField.requestFocus();
    }
    //displays an error message in the message label
    private void showError(String message) {
        messageLabel.setText("❌ " + message);
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    }
    //displays a success message in the message label
    private void showSuccess(String message) {
        messageLabel.setText("✓ " + message);
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}