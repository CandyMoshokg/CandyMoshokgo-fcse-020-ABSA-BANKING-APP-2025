import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Banking System Login");
        
        // Create UI elements
        Label usernameLabel = new Label("Customer ID:");
        usernameField = new TextField();
        
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        
        loginButton = new Button("Login");
        
        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            loginButton
        );
        
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Button action (will add functionality later)
        loginButton.setOnAction(e -> {
            System.out.println("Login clicked - Username: " + usernameField.getText());
        });

        loginButton.setOnAction(e -> {
    // For testing, create a dummy customer
    Customer testCustomer = new Customer("C001", "General", "Motaso", "345 Block 3", "pass");
    DashboardScreen dashboard = new DashboardScreen();
    dashboard.showDashboard(primaryStage, testCustomer);
});
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}