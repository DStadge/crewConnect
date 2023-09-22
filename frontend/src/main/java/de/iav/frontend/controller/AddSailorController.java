package de.iav.frontend.controller;


import de.iav.frontend.model.SailorWithoutId;
import de.iav.frontend.service.SailorService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.model.Sailor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class AddSailorController implements Initializable {
    //
    private final SailorService sailorService = SailorService.getInstance();

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private ChoiceBox<String> experienceChoiceBox = new ChoiceBox<>();
    @FXML
    private DatePicker sailDate;

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

    private void showCustomPopup(String title, String message) {
        Stage popupStage = new Stage();
        popupStage.initOwner(firstName.getScene().getWindow()); // Setzen Sie das Elternfenster
        popupStage.initModality(Modality.WINDOW_MODAL); // Stellen Sie sicher, dass es nicht-modal ist

        VBox popupContent = new VBox(new Label(message));
        Scene popupScene = new Scene(popupContent, 300, 100);

        popupStage.setScene(popupScene);
        popupStage.setTitle(title);
        popupStage.show();

        // Schließen Sie das Popup-Fenster nach einer Verzögerung von 2 Sekunden
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            popupStage.close();
                        });
                    }
                },
                8000
        );
    }

    public void popup(){
        LocalDate currentDate = LocalDate.now();
        LocalDate sailDateValue = sailDate.getValue();
        long daysDifference = ChronoUnit.DAYS.between(currentDate, sailDateValue);

        showCustomPopup("Tage bis zum Segelerlebnis", "Nur noch: " + daysDifference + " Tage bis zu  deinem Segelerlebnis.");
    }

    @FXML
    public void saveNewSailorButton(ActionEvent event) throws IOException {
        if (sailDate.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warnung");
            alert.setHeaderText("Das Datum liegt in der Vergangenheit");
            alert.setContentText("Bitte gib ein Datum in der Zukunft ein.");
            alert.showAndWait();
        } else if (firstName.getText() == null || firstName.getText().isEmpty() ||
                lastName.getText() == null || lastName.getText().isEmpty() ||
                experienceChoiceBox.getValue() == null || experienceChoiceBox.getValue().isEmpty() ||
                sailDate.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warnung");
            alert.setHeaderText("Bitte fülle alle Felder aus");
            alert.showAndWait();
        } else {
            if (sailorId == null) {
                SailorWithoutId newSailor = new SailorWithoutId(
                        firstName.getText(),
                        lastName.getText(),
                        experienceChoiceBox.getValue(),
                        sailDate.getValue());
                sailorService.addSailor(newSailor);
                popup();
            } else {
                Sailor sailorData = new Sailor(
                        sailorId,
                        firstName.getText(),
                        lastName.getText(),
                        experienceChoiceBox.getSelectionModel().getSelectedItem(),
                        sailDate.getValue());
                sailorService.updateSailorById(sailorId, sailorData);
               popup();
            }
            sceneSwitchService.saveNewSailorSwitchToMainScene(event);
        }
    }

    @FXML
    public void setSelectedSailor(Sailor selectedSailor) {
        this.sailorId = selectedSailor.sailorId();
        firstName.setText(selectedSailor.firstName());
        lastName.setText(selectedSailor.lastName());
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
        stage.setTitle("Hauptseite");
    }
}