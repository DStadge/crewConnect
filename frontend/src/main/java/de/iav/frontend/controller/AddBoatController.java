package de.iav.frontend.controller;

import de.iav.frontend.model.Boat;
import de.iav.frontend.service.SceneSwitchService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AddBoatController implements Initializable {
    @FXML
    private TextField boatName;
    @FXML
    private TextField boatType;
    private Boat boat;

    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void saveNewBoatButton(Boat boat) {
        this.boat = boat;
        if (boat != null) {
            boatName.setText(boat.boatName());
            boatType.setText(boat.boatType());
        }
    }

    @FXML
    public void saveNewBoatButton(ActionEvent event) {
        if (boat == null) {
            boat = new Boat(boatName.getText(), boatType.getText());
        } else {
            boat = new Boat(boat.boatName(), boat.boatType());
        }
       // switchToMainScene(event);
      // closeBoatWindow(event);
    }

    /*
    @FXML
    public void cancelBoat(ActionEvent event) {
        closeBoatWindow(event);
    }*/

    private void closeBoatWindow(ActionEvent event) {
        Stage stage = (Stage) boatName.getScene().getWindow();
        stage.close();
      //  sceneSwitchService.saveNewBoatSwitchToMainScene(event);
    }

    @FXML
    protected void switchToMainScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/fxml/Main-Scene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Hauptseite");
    }
}