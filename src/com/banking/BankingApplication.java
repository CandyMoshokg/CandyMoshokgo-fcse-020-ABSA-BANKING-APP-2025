package com.banking;

import com.banking.database.DatabaseManager;
import com.banking.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;
//this is the main application class for the banking system
public class BankingApplication extends Application {
    //starts the JavaFX application
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Absa");
            
            System.out.println("Init");
            DatabaseManager dbManager = DatabaseManager.getInstance();
            dbManager.initializeDatabase();
            
            System.out.println("Loading.");
            SampleDataLoader.loadSampleData();
            
            System.out.println("Database ready!");
            System.out.println("===============================\n");
            
            LoginView loginView = new LoginView();
            loginView.start(primaryStage);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //handles application shutdown
    @Override
    public void stop() {
        System.out.println("\n Shutting down.");
        System.out.println("ERROR!");
    }
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════╗");
        System.out.println("║   ABSA BANKING    ║");
        System.out.println("╚═══════════════════╝\n");
        
        launch(args);
    }
}
