import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardScreen {
    private ListView<String> accountsList;
    private Label welcomeLabel;
    private Button depositButton;
    private Button logoutButton;
    
    public void showDashboard(Stage stage, Customer customer) {
        stage.setTitle("Banking Dashboard");
        
        welcomeLabel = new Label("Welcome, " + customer.getFirstName() + " " + customer.getSurname());
        accountsList = new ListView<>();
        depositButton = new Button("Make Deposit");
        logoutButton = new Button("Logout");
        
        // myLayout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            welcomeLabel,
            new Label("Your Accounts:"),
            accountsList,
            depositButton,
            logoutButton
        );
        
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        
        // what does button does action by the way
        logoutButton.setOnAction(e -> {
            // Go back to login
            LoginScreen login = new LoginScreen();
            login.start(stage);
        });
    }
}