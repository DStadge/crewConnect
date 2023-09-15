package de.iav.frontend.controller;


import de.iav.frontend.model.SailorWithoutId;
import de.iav.frontend.service.SailorService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.model.Sailor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddSailorController implements Initializable {

    private final SailorService sailorService = SailorService.getInstance();

    @FXML
    private Button backToMainSceneButton;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private ChoiceBox<String> experienceChoiceBox = new ChoiceBox<>();
    @FXML
    private DatePicker sailDate;
    @FXML
    private Button saveButton;

    private String sailorId;

    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        experienceChoiceBox.getItems().add("Anfaenger");
        experienceChoiceBox.getItems().add("Fortgeschritten");
        experienceChoiceBox.getItems().add("Experte");
    }

    public int getIndexOfExperienceChoiceBoxItem(ChoiceBox<String> choiceBox, Sailor sailorToUpdate) {
        // Find the index of the specified Sailor object in the ChoiceBox.
        int selectedIndex = -1; // Default value if the item is not found.
        for (int i = 0; i < choiceBox.getItems().size(); i++) {
            String element = choiceBox.getItems().get(i);
            if (element.equals(sailorToUpdate.experience())) {
                selectedIndex = i;
                break;
            }
        }
        return selectedIndex;
    }

    @FXML
    public void saveNewSailorButton(ActionEvent event) throws IOException {
        if (sailorId == null) {
            SailorWithoutId newSailor = new SailorWithoutId(firstName.getText(), lastName.getText(), experienceChoiceBox.getValue(), sailDate.getValue());
            //getSelectionModel().getSelectedItem()
            sailorService.addSailor(newSailor);
        } else {
            Sailor sailorData = new Sailor(
                    sailorId,
                    firstName.getText(),
                    lastName.getText(),
                    experienceChoiceBox.getSelectionModel().getSelectedItem(),
                    sailDate.getValue());
            sailorService.updateSailorById(sailorId, sailorData);
        }
        sceneSwitchService.saveNewSailorSwitchToMainScene(event);
    }

    @FXML
    public void setSelectedSailor(Sailor selectedSailor) {
        this.sailorId = selectedSailor.sailorId();
        firstName.setText(selectedSailor.firstname());
        lastName.setText(selectedSailor.lastname());
        experienceChoiceBox.getSelectionModel().select(getIndexOfExperienceChoiceBoxItem(experienceChoiceBox, selectedSailor));
        sailDate.setValue(selectedSailor.sailDate());
    }

    @FXML
    protected void switchToMainScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/fxml/main-scene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}