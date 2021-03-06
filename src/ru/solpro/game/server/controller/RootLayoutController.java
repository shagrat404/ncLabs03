/*
 * @(#)RootLayoutController.java 1.0 02.01.2017
 */

package ru.solpro.game.server.controller;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ru.solpro.game.server.core.Client;
import ru.solpro.game.server.core.ServerLoader;
import ru.solpro.game.server.model.Battle;
import ru.solpro.game.server.model.StatusPlayer;

import java.io.IOException;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
public class RootLayoutController {

    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private ObservableList<Battle> battles = FXCollections.observableArrayList();

    @FXML
    private Label labelCountPlayer;
    @FXML
    private Label labelServerStatus;
    @FXML
    private Label labelCountBattle;
    @FXML
    private TextArea textLog;
    @FXML
    private Button buttonViewSetting;
    @FXML
    private Button buttonStartServer;
    @FXML
    private Button buttonStopServer;
    @FXML
    private TableView<Client> playerTable;
    @FXML
    private TableColumn<Client, String> nicknamePlayerColumn;
    @FXML
    private TableColumn<Client, StatusPlayer> statusPlayerColumn;

    private Thread serverThread;

    public RootLayoutController() {}

    @FXML
    private void initialize() {
        buttonStopServer.setDisable(true);

        nicknamePlayerColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        statusPlayerColumn.setCellValueFactory(new PropertyValueFactory<>("statusPlayer"));

        playerTable.setItems(clients);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                paintComponent();
            }
        };
        timer.start();
    }

    @FXML
    private void buttonViewSettingAction(ActionEvent actionEvent) {
        //TODO: отображение окна настроек сервера
        try {
            Stage stage = new Stage();
            GridPane root = FXMLLoader.load(getClass().getResource("../view/SettingsModalWindow.fxml"));
            stage.setTitle("Настройки");
            stage.setMinWidth(300);
            stage.setMinHeight(100);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonStartServerAction(ActionEvent actionEvent) {
        if (serverThread == null || !serverThread.isAlive()) {
            ServerLoader.setRootLayoutController(this);
            serverThread = new Thread(new ServerLoader());
            serverThread.setDaemon(true);
            serverThread.start();
        }
    }

    @FXML
    private void buttonStopServerAction(ActionEvent actionEvent) {
        ServerLoader.stop();
    }

    /**
     * Динамическое обновление данных на форме.
     */
    private void paintComponent() {
//        playerTable.setItems(clients);

        labelCountPlayer.setText("Play: " + clients.size());

        if (serverThread != null && serverThread.isAlive()) {
            labelServerStatus.setText("Запущен.");
        } else {
            labelServerStatus.setText("Остановлен.");
        }

        labelCountBattle.setText("Боёв: " + battles.size());
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public TextArea getTextLog() {
        return textLog;
    }

//    public Label getLabelCountPlayer() {
//        return labelCountPlayer;
//    }
//
//    public Label getLabelServerStatus() {
//        return labelServerStatus;
//    }
//
//    public Label getLabelCountBattle() {
//        return labelCountBattle;
//    }
//
//    public TableView<Client> getPlayerTable() {
//        return playerTable;
//    }
//
//    public TableColumn<Client, StatusPlayer> getStatusPlayerColumn() {
//        return statusPlayerColumn;
//    }
//
//    public TableColumn<Client, String> getNicknamePlayerColumn() {
//        return nicknamePlayerColumn;
//    }

    public Button getButtonViewSetting() {
        return buttonViewSetting;
    }

    public Button getButtonStartServer() {
        return buttonStartServer;
    }

    public Button getButtonStopServer() {
        return buttonStopServer;
    }

//    public ObservableList<Battle> getBattles() {
//        return battles;
//    }
}
