package de.iav.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CrewConnectFrontendApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CrewConnectFrontendApplication.class.getResource("/de/iav/frontend/fxml/login-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login Seite");
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(780);
        stage.setMaxWidth(780);
        stage.setMinHeight(780);
        stage.setMaxHeight(780);

    }

    public static void main(String[] args) {
        launch(args);
    }
}