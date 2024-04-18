package com.example;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author revat
 */

import java.sql.*;
import java.util.Scanner;

public class Search {
    static final String DB_URL = "jdbc:mysql://localhost:3306/stackoverflow_survey";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Scanner in = new Scanner(System.in)) {

            System.out.println("Welcome to the Database Query Application!");
            System.out.println("Please enter 'login' to login, 'register' to register, or 'exit' to quit.");

            UserLogin userLogin = new UserLogin(conn, in);
            UserRegistration userRegistration = new UserRegistration(conn, in);
            SearchFunctionality searchFunctionality = new SearchFunctionality(conn, in);

            while (true) {
                System.out.println("Enter your choice:");
                String action = in.nextLine().trim().toLowerCase();

                if (action.equals("exit")) {
                    System.out.println("Exiting...");
                    return; // Stop execution
                }

                switch (action) {
                    case "login":
                        userLogin.handleLogin();
                        break;
                    case "register":
                        userRegistration.handleRegistration();
                        break;
                    default:
                        System.out.println("Invalid action. Please try again.");
                        break;
                }
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
