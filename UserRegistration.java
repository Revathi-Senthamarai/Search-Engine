package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UserRegistration {
    private Connection conn;
    private Scanner in;

    public UserRegistration(Connection conn, Scanner in) {
        this.conn = conn;
        this.in = in;
    }

    public void handleRegistration() {
        System.out.println("Enter a new username:");
        String newUsername = in.nextLine();
        System.out.println("Enter a password:");
        String newPassword = in.nextLine();

        try {
            if (registerUser(newUsername, newPassword)) {
                System.out.println("User registered successfully! Please login.");
            } else {
                System.out.println("Failed to register user. Try again with a different username.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean registerUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        }
    }
}
