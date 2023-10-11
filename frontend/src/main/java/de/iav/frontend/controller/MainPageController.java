package de.iav.frontend.controller;

import de.iav.frontend.exception.CustomIOException;
import de.iav.frontend.model.Sailor;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SailorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPageController {

    private Scene scene;
    private Parent root;
    private Stage stage;
    private final SailorService sailorService = SailorService.getInstance();

    @FXML
    public VBox tableView;
    @FXML
    private TableView<Sailor> table;
    @FXML
    private TableColumn<Sailor, String> firstNameColumn;
    @FXML
    private TableColumn<Sailor, String> lastNameColumn;
    @FXML
    private TableColumn<Sailor, String> experienceColumn;
    @FXML
    private TableColumn<Sailor, LocalDate> sailDateColumn;

    private final AuthService authService = AuthService.getInstance();

    public void initialize() {
        List<Sailor> allSailor = sailorService.getSailorList();
        table.getItems().clear();

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        sailDateColumn.setCellValueFactory(new PropertyValueFactory<>("sailDate"));

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
        stage.setTitle("Segler bearbeiten");
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
        stage.setTitle("Login Seite");
    }

    @FXML
    public void switchToUpdateBoatScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/fxml/addSailor-scene.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Boot hinzufuegen");
        stage.show();
    }
}

