package de.iav.frontend.controller;

import de.iav.frontend.exception.CustomIOException;
import de.iav.frontend.model.Sailor;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SailorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MainPageController {

    private Scene scene;
    private Parent root;
    private Stage stage;
    private final SailorService sailorService = SailorService.getInstance();


    @FXML
    private TableView<Sailor> table;
    @FXML
    private TableColumn<Sailor, String> firstNameColumn;
    @FXML
    private TableColumn<Sailor, String> lastNameColumn;
    @FXML
    private TableColumn<Sailor, String> expirationColumn;
    @FXML
    private TableColumn<Sailor, String> dateColumn;

    private final AuthService authService = AuthService.getInstance();

    public void initialize() {
        List<Sailor> allSailor = sailorService.getSailorList();
        table.getItems().clear();

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("Vorname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("Nachname"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("Erfahrung"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("SegelDatum"));

        table.getItems().addAll(allSailor);
    }

    @FXML
    public void switchToAddSailorScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/fxml/addSailor-scene.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Segler hinzufuegen");
        stage.show();
    }

    @FXML
    public void deleteSailorById(ActionEvent event) {
        sailorService.deleteSailorById(table.getSelectionModel().getSelectedItem().sailorId(), table);
    }

    @FXML
    public void switchToUpdateSailorScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/fxml/addSailor-scene.fxml"));
        root = loader.load();

        Sailor sailorToUpdate = table.getSelectionModel().getSelectedItem();
        AddSailorController addsailorController = loader.getController();
        addsailorController.setSelectedSailor(sailorToUpdate);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Segler hinzufuegen");
        stage.show();
    }

    @FXML
    private void logout(ActionEvent event) {
        authService.logout();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/iav/frontend/fxml/login-scene.fxml"));
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (Exception e) {
            throw new CustomIOException(e.toString());
        }
        scene = new Scene(parent);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}

