package de.iav.frontend.controller;

import de.iav.frontend.CrewConnectFrontendApplication;
import de.iav.frontend.exception.CustomIOException;
import de.iav.frontend.security.AppUserRequest;
import de.iav.frontend.security.AuthService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class RegisterController {
    @FXML
    public TextField usernameInput;
    @FXML
    public TextField emailInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public Label errorLabel;

/*
    @FXML
    protected void onClickBackButton(ActionEvent event) {
        // Erstelle einen FXMLLoader, um die FXML-Datei für die Login-Szene zu laden
        FXMLLoader loader = new FXMLLoader(CrewConnectFrontendApplication.class.getResource("/de/iav/frontend/fxml/login-scene.fxml"));
        Parent root;
        try {
            // Lade die FXML-Datei und erstelle ein Parent-Objekt daraus
            root = loader.load();
        } catch (IOException e) {
            // Behandle eine mögliche IOException
            throw new CustomIOException(e.toString());
        }

        // Erhalte die aktuelle Bühne (Stage) des Buttons
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Erstelle eine neue Szene mit dem geladenen Root (Login-Seite)
        Scene scene = new Scene(root);

        // Erstelle eine FadeTransition, um einen Fade-Out-Effekt für die aktuelle Szene zu erstellen
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), stage.getScene().getRoot());
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Setze den EventHandler, um die neue Szene nach dem Fade-Out zu setzen
        fadeOutTransition.setOnFinished(e -> {
            stage.setScene(scene); // Wechsle zur neuen Szene
            stage.setTitle("Login Seite");

            // Erstelle eine FadeTransition für den Fade-In-Effekt der neuen Szene
            FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(1), stage.getScene().getRoot());
            fadeInTransition.setFromValue(0.0);
            fadeInTransition.setToValue(1.0);
            fadeInTransition.play(); // Starte den Fade-In-Effekt
        });

        fadeOutTransition.play(); // Starte den Fade-Out-Effekt
    }
*/


    @FXML
    protected void onClickBackButton(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(CrewConnectFrontendApplication.class.getResource("/de/iav/frontend/fxml/login-scene.fxml"));
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

        AppUserRequest appUserRequest = new AppUserRequest(
                usernameInput.getText(),
                emailInput.getText(),
                passwordInput.getText()
        );

        if (authService.registerAppUser(appUserRequest)) {
            FXMLLoader fxmlLoader = new FXMLLoader(CrewConnectFrontendApplication.class.getResource("/de/iav/frontend/fxml/login-scene.fxml"));
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
            errorLabel.setText(authService.errorMessage());
        }
    }
}

