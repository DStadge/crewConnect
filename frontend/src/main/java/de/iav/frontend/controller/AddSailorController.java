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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AddSailorController implements Initializable {

    private static final String FONT_NAME = "Comic Sans MS";
    private static final Font customFont = Font.font(FONT_NAME, FontWeight.BOLD, 16);

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

        Tooltip firstNameTooltip = new Tooltip("Gib deinen Vornamen ein");
        Font nameTooltipFont = Font.font(FONT_NAME, FontWeight.BOLD, 16);
        firstNameTooltip.setFont(nameTooltipFont);
        firstName.setTooltip(firstNameTooltip);

        Tooltip lastNameTooltip = new Tooltip("Gib deinen Nachnamen ein");
        lastNameTooltip.setFont(nameTooltipFont);
        lastName.setTooltip(lastNameTooltip);

        Tooltip choiceBoxTooltip = new Tooltip("Anfänger <10 Segeltage\n" +
                "Fortgeschritten >20 Segeltage\n" +
                "Experte >5 Regatten als Steuermann\n");
        Font tooltipFont = Font.font(FONT_NAME, FontWeight.BOLD, 16);
        choiceBoxTooltip.setFont(tooltipFont);
        experienceChoiceBox.setTooltip(choiceBoxTooltip);
        choiceBoxTooltip.setShowDuration(Duration.millis(5000));

        Tooltip dateTooltip = new Tooltip("Wähle das Segeldatum aus");
        Font dateTooltipFont = Font.font(FONT_NAME, FontWeight.BOLD, 16);
        dateTooltip.setFont(dateTooltipFont);
        sailDate.setTooltip(dateTooltip);
        dateTooltip.setShowDuration(Duration.millis(3000));

    }

    public int getIndexOfExperienceChoiceBoxItem(ChoiceBox<String> choiceBox, Sailor sailorToUpdate) {
        int selectedIndex = -1;
        for (int i = 0; i < choiceBox.getItems().size(); i++) {
            String element = choiceBox.getItems().get(i);
            if (element.equals(sailorToUpdate.experience())) {
                selectedIndex = i;
                break;
            }
        }
        return selectedIndex;
    }

    public void popup() {
        LocalDate currentDate = LocalDate.now();
        LocalDate sailDateValue = sailDate.getValue();
        long daysDifference = ChronoUnit.DAYS.between(currentDate, sailDateValue);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tage bis zum Segelerlebnis");
        alert.setHeaderText(null);
        alert.getDialogPane().setStyle("-fx-font: " + customFont.getSize() + "px '" + FONT_NAME + "';");
        if (daysDifference == 0) {
            alert.setContentText("Heute ist dein Segelerlebnis! Viel Spaß dabei!");
        } else if (daysDifference == 1) {
            alert.setContentText("Viel Spaß für Dein Segelerlebnis morgen!");
        } else {
            alert.setContentText("Nur noch: " + daysDifference + " Tage bis zu deinem Segelerlebnis.");
        }
        alert.showAndWait();
    }

    @FXML
    public void saveNewSailorButton(ActionEvent event) throws IOException {
        if (firstName.getText() == null || firstName.getText().isEmpty() ||
                lastName.getText() == null || lastName.getText().isEmpty() ||
                experienceChoiceBox.getValue() == null || experienceChoiceBox.getValue().isEmpty() ||
                sailDate.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warnung");
            alert.setHeaderText("Bitte fülle alle Felder aus");
            alert.getDialogPane().setStyle("-fx-font: " + customFont.getSize() + "px '" + FONT_NAME + "';");
            alert.showAndWait();
        } else if (sailDate.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warnung");
            alert.setHeaderText("Das Datum liegt in der Vergangenheit!");
            alert.setContentText("Bitte gib ein Datum in der Zukunft ein.");
            alert.getDialogPane().setStyle("-fx-font: " + customFont.getSize() + "px '" + FONT_NAME + "';");
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
                        sailDate.getValue()
                );
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/fxml/Main-Scene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Hauptseite");
    }
}