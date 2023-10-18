package de.iav.frontend.controller;

import de.iav.frontend.CrewConnectFrontendApplication;
import de.iav.frontend.exception.CustomIOException;
import de.iav.frontend.security.AppUserRequest;
import de.iav.frontend.security.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML
    public TextField usernameInput;
    @FXML
    public TextField emailInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public Label errorLabel;

    @FXML
    protected void onClickBackButton(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(CrewConnectFrontendApplication.class.getResource("/de/iav/frontend/fxml/Login-Scene.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new CustomIOException(e.toString());
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Login Seite");
    }

    @FXML
    protected void onRegisterClick() {
        register();
    }

    private final AuthService authService = AuthService.getInstance();

    public void register() {
        if (usernameInput.getText().isEmpty() || emailInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warnung");
            alert.setHeaderText("Bitte fülle alle Felder aus");
            Font customFont = Font.font("Comic Sans MS", FontWeight.BOLD, 16);
            alert.getDialogPane().setStyle("-fx-font: " + customFont.getSize() + "px 'Comic Sans MS';");
            alert.showAndWait();
        } else {
            String email = emailInput.getText();
            if (!isValidEmail(email)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warnung");
                alert.setHeaderText("Ungültige E-Mail-Adresse");
                Font customFont = Font.font("Comic Sans MS", FontWeight.BOLD, 16);
                alert.getDialogPane().setStyle("-fx-font: " + customFont.getSize() + "px 'Comic Sans MS';");
                alert.showAndWait();
                return;
            }

            AppUserRequest appUserRequest = new AppUserRequest(
                    usernameInput.getText(),
                    email,
                    passwordInput.getText()
            );

            if (authService.registerAppUser(appUserRequest)) {
                FXMLLoader fxmlLoader = new FXMLLoader(CrewConnectFrontendApplication.class.getResource("/de/iav/frontend/fxml/Login-Scene.fxml"));
                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    throw new CustomIOException(e.toString());
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) emailInput.getScene().getWindow();
                stage.setScene(scene);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warnung");
                alert.setHeaderText(authService.errorMessage());
                Font customFont = Font.font("Comic Sans MS", FontWeight.BOLD, 16);
                alert.getDialogPane().setStyle("-fx-font: " + customFont.getSize() + "px 'Comic Sans MS';");
                alert.showAndWait();
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]{2,}+@[A-Za-z0-9.-]{2,}\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}