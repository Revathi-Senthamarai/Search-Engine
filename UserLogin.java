package com.example;

import com.example.SearchFunctionality;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserLogin {
    private Connection conn;
    private Scanner in;

    public UserLogin(Connection conn, Scanner in) {
        this.conn = conn;
        this.in = in;
    }

    public void handleLogin() {
        System.out.println("Enter your username:");
        String username = in.nextLine();
        System.out.println("Enter your password:");
        String password = in.nextLine();

        try {
            int failedAttempts = 0;
            boolean passwordChanged = false;

            while (failedAttempts < 3) {
                if (validateUser(username, password)) {
                    System.out.println("Login successful!");
                    if (passwordChanged) {
                        new SearchFunctionality(conn, in).executeQueryLoop(username);
                    } else {
                        if (failedAttempts == 0) {
                            new SearchFunctionality(conn, in).executeQueryLoop(username);
                        } else {
                            System.out.println("Please change your password.");
                            changePassword(username);
                            passwordChanged = true;
                        }
                    }
                    return;
                } else {
                    failedAttempts++;
                    System.out.println("Invalid username or password. Please try again.");
                    System.out.println("Attempts left: " + (3 - failedAttempts));
                    password = in.nextLine();
                }
            }

            if (failedAttempts == 3) {
                System.out.println("You have reached the maximum number of failed attempts.");
                System.out.println("Would you like to change your password? (yes/no)");
                String changePasswordChoice = in.nextLine().toLowerCase();
                if (changePasswordChoice.equals("yes")) {
                    changePassword(username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void changePassword(String username) throws SQLException {
        System.out.println("Enter your new password:");
        String newPassword = in.nextLine();

        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Failed to change password.");
            }
        }
    }
}
