package de.iav.frontend.controller;

import de.iav.frontend.model.Boat;
import de.iav.frontend.service.BoatService;
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
    private BoatService boatService;
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AddBoatController addBoatController = new AddBoatController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("de/iav/frontend/fxml/AddBoat-Scene.fxml"));
        loader.setController(addBoatController);
      //  Parent root = loader.load();
        // Initialisiere den BoatService
        boatService = BoatService.getInstance();
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
        // Erstelle ein neues Boot-Objekt aus den Eingabefeldern
        Boat newBoat = new Boat(boatName.getText(), boatType.getText());

        // Hier kannst du den Speichervorgang in deinem BoatService oder einer anderen Serviceklasse aufrufen
        BoatService boatService = BoatService.getInstance();
        boatService.saveBoat(newBoat);

        // Weitere Aktionen, z.B. zur Hauptseite wechseln oder das Fenster schließen
        // switchToMainScene(event);
        // closeBoatWindow(event);
    }

    /*
    @FXML
    public void cancelBoat(ActionEvent event) {
        closeBoatWindow(event);
    }*/

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