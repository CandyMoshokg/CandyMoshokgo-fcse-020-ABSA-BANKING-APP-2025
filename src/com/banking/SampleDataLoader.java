package com.banking;

import com.banking.dao.*;
import com.banking.model.*;

public class SampleDataLoader {
    //loads sample data into the database for testing purposes
    public static void loadSampleData() {
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        UserDAO userDAO = new UserDAO();
        
        if (customerDAO.findAll().size() > 0) {
            System.out.println("Skipping.");
            return;
        }
        
        System.out.println("Loading data.");
       //creates a bank instance to register customers and open accounts 
    Bank bank = new Bank("Bank", "BK01");
    
        try {
            createSampleUsers(userDAO);
            
            createSampleCustomer1(bank, customerDAO, accountDAO);
            createSampleCustomer2(bank, customerDAO, accountDAO);
            createSampleCustomer3(bank, customerDAO, accountDAO);
            createSampleCustomer4(bank, customerDAO, accountDAO);
            createSampleCustomer5(bank, customerDAO, accountDAO);
            createSampleCustomer6(bank, customerDAO, accountDAO);
            createSampleCustomer7(bank, customerDAO, accountDAO);
            createSampleCustomer8(bank, customerDAO, accountDAO);
            createSampleCustomer9(bank, customerDAO, accountDAO);
            createSampleCustomer10(bank, customerDAO, accountDAO);
            
            System.out.println("Loading successful!");
            
        } catch (Exception e) {
            System.err.println("Error loading: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //creates sample users with specified roles
    private static void createSampleUsers(UserDAO userDAO) {
        if (!userDAO.exists("teller01")) {
            userDAO.saveWithPassword("teller01", "Jane Smith", "teller123", "TELLER");
        }
        
        if (!userDAO.exists("manager01")) {
            userDAO.saveWithPassword("manager01", "Michael Johnson", "manager123", "MANAGER");
        }
    }
    //creates sample customer 1 with a savings account
    private static void createSampleCustomer1(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("General", "May", "Block 7, Gaborone");
        c.setPhoneNumber("72110011");
        c.setEmail("general.may@email.bw");
        customerDAO.save(c);
        
        SavingsAccount s = bank.openSavingsAccount(c.getCustomerId(), 6000, "Gaborone Main");
        accountDAO.save(s);
    }
    //creates sample customer 2 with an investment account
    private static void createSampleCustomer2(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Pako", "May", "Phase 2, Gaborone");
        c.setPhoneNumber("73777711");
        c.setEmail("pako.may@email.bw");
        customerDAO.save(c);
        
        InvestmentAccount i = bank.openInvestmentAccount(c.getCustomerId(), 12000, "Gaborone Main");
        accountDAO.save(i);
    }
    //creates sample customer 3 with a cheque account
    private static void createSampleCustomer3(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Candy", "Moshokgo", "Selebi-Phikwe");
        c.setPhoneNumber("74223344");
        c.setEmail("candy.moshokgo@email.bw");
        customerDAO.save(c);
        
        ChequeAccount ch = bank.openChequeAccount(c.getCustomerId(), 4500, "Gaborone Main",
            "Letshego Holdings", "Gaborone CBD");
        accountDAO.save(ch);
    }
    //creates sample customer 4 with savings and investment accounts
    private static void createSampleCustomer4(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Motaso", "Omphile", "Lobatse");
        c.setPhoneNumber("76881234");
        customerDAO.save(c);
        
        SavingsAccount s = bank.openSavingsAccount(c.getCustomerId(), 3000, "Gaborone Main");
        InvestmentAccount i = bank.openInvestmentAccount(c.getCustomerId(), 7000, "Gaborone Main");
        accountDAO.save(s);
        accountDAO.save(i);
    }
    //creates sample customer 5 with a savings account
    private static void createSampleCustomer5(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Thapelo", "Keagakwa", "Serowe");
        c.setPhoneNumber("73334455");
        c.setEmail("thapelo.k@email.bw");
        customerDAO.save(c);
        
        SavingsAccount s = bank.openSavingsAccount(c.getCustomerId(), 9000, "Gaborone Main");
        accountDAO.save(s);
    }
    //creates sample customer 6 with an investment account
    private static void createSampleCustomer6(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Mpho", "Garekwe", "Maun");
        c.setPhoneNumber("75556677");
        customerDAO.save(c);
        
        InvestmentAccount i = bank.openInvestmentAccount(c.getCustomerId(), 18000, "Gaborone Main");
        accountDAO.save(i);
    }
    //creates sample customer 7 with a cheque account
    private static void createSampleCustomer7(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Lorato", "Rakhudu", "Palapye");
        c.setPhoneNumber("72009988");
        c.setEmail("lorato.r@email.bw");
        customerDAO.save(c);
        
        ChequeAccount ch = bank.openChequeAccount(c.getCustomerId(), 6500, "Gaborone Main",
            "Stanbic Bank", "Francistown");
        accountDAO.save(ch);
    }
    //creates sample customer 8 with savings, investment, and cheque accounts
    private static void createSampleCustomer8(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Ofentse", "Diphoko", "Jwaneng");
        c.setPhoneNumber("73445566");
        customerDAO.save(c);
        
        SavingsAccount s = bank.openSavingsAccount(c.getCustomerId(), 2500, "Gaborone Main");
        InvestmentAccount i = bank.openInvestmentAccount(c.getCustomerId(), 1500, "Gaborone Main");
        ChequeAccount ch = bank.openChequeAccount(c.getCustomerId(), 5000, "Gaborone Main",
            "Debswana Mining", "Jwaneng");
        accountDAO.save(s);
        accountDAO.save(i);
        accountDAO.save(ch);
    }
    //creates sample customer 9 with a savings account
    private static void createSampleCustomer9(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Naledi", "Moalusi", "Ramotswa");
        c.setPhoneNumber("72664433");
        c.setEmail("naledi.m@email.bw");
        customerDAO.save(c);
        
        SavingsAccount s = bank.openSavingsAccount(c.getCustomerId(), 8200, "Gaborone Main");
        accountDAO.save(s);
    }
    //creates sample customer 10 with investment and cheque accounts
    private static void createSampleCustomer10(Bank bank, CustomerDAO customerDAO, AccountDAO accountDAO) {
        Customer c = bank.registerCustomer("Boitshepo", "Ranko", "Mochudi");
        c.setPhoneNumber("77441122");
        customerDAO.save(c);
        
        InvestmentAccount i = bank.openInvestmentAccount(c.getCustomerId(), 13000, "Gaborone Main");
        ChequeAccount ch = bank.openChequeAccount(c.getCustomerId(), 5500, "Gaborone Main",
            "Water Utilities Corporation", "Mochudi");
        accountDAO.save(i);
        accountDAO.save(ch);
    }
}
