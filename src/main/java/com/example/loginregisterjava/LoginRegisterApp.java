
package com.example.loginregisterjava;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;

public class LoginRegisterApp extends Application {

    private final String FILE_NAME = "users.txt";

    @Override
    public void start(Stage stage) {

        // ================= LOGIN PAGE =================
        Label loginHeader = new Label("Login");
        loginHeader.setFont(Font.font(20));

        TextField loginUsername = new TextField();
        loginUsername.setPromptText("Username");

        PasswordField loginPassword = new PasswordField();
        loginPassword.setPromptText("Password");

        Label loginMessage = new Label();

        Button loginButton = new Button("Log In");
        Button goRegisterButton = new Button("Register");

        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(
                loginHeader, loginUsername, loginPassword,
                loginButton, goRegisterButton, loginMessage
        );

        Scene loginScene = new Scene(loginLayout, 400, 400);

        // ================= REGISTER PAGE =================
        Label registerHeader = new Label("Register");
        registerHeader.setFont(Font.font(20));

        TextField regUsername = new TextField();
        regUsername.setPromptText("Username");

        TextField regEmail = new TextField();
        regEmail.setPromptText("Email");

        PasswordField regPassword = new PasswordField();
        regPassword.setPromptText("Password");

        PasswordField regConfirmPassword = new PasswordField();
        regConfirmPassword.setPromptText("Confirm Password");

        Label registerMessage = new Label();

        Button registerButton = new Button("Register");
        Button goLoginButton = new Button("Back to Login");

        VBox registerLayout = new VBox(10);
        registerLayout.setPadding(new Insets(20));
        registerLayout.setAlignment(Pos.CENTER);
        registerLayout.getChildren().addAll(
                registerHeader,
                regUsername,
                regEmail,
                regPassword,
                regConfirmPassword,
                registerButton,
                goLoginButton,
                registerMessage
        );

        Scene registerScene = new Scene(registerLayout, 400, 450);

        // ================= WELCOME PAGE ================= //
        Label welcomeLabel = new Label();
        welcomeLabel.setFont(Font.font(20));

        Button logoutButton = new Button("Log Out");

        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.getChildren().addAll(welcomeLabel, logoutButton);

        Scene welcomeScene = new Scene(welcomeLayout, 400, 300);

        // ================= BUTTON ACTIONS ================= //


        goRegisterButton.setOnAction(e -> {
            loginMessage.setText("");
            stage.setScene(registerScene);
        });


        goLoginButton.setOnAction(e -> {
            registerMessage.setText("");
            stage.setScene(loginScene);
        });


        registerButton.setOnAction(e -> {
            String username = regUsername.getText();
            String email = regEmail.getText();
            String password = regPassword.getText();
            String confirmPassword = regConfirmPassword.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                registerMessage.setText("All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                registerMessage.setText("Passwords do not match.");
                return;
            }

            if (userExists(username, email)) {
                registerMessage.setText("Username or Email already exists.");
                return;
            }

            saveUser(username, password, email);
            registerMessage.setText("Registration successful!");

            regUsername.clear();
            regEmail.clear();
            regPassword.clear();
            regConfirmPassword.clear();

            stage.setScene(loginScene);
        });

        // Login action
        loginButton.setOnAction(e -> {
            String username = loginUsername.getText();
            String password = loginPassword.getText();

            if (validateLogin(username, password)) {
                welcomeLabel.setText("Welcome, " + username + "!");
                loginMessage.setText("");
                loginUsername.clear();
                loginPassword.clear();
                stage.setScene(welcomeScene);
            } else {
                loginMessage.setText("Incorrect username or password.");
            }
        });


        logoutButton.setOnAction(e -> stage.setScene(loginScene));


        stage.setTitle("Login Registration System");
        stage.setScene(loginScene);
        stage.show();
    }

    // ================= SAVE USER =================
    private void saveUser(String username, String password, String email) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + "|" + password + "|" + email);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= CHECK USER EXISTS ================= //
    private boolean userExists(String username, String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split("\\|");
                if (userData[0].equals(username) || userData[2].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {

        }
        return false;
    }

    // ================= VALIDATE LOGIN ================= //
    private boolean validateLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split("\\|");
                if (userData[0].equals(username) && userData[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

