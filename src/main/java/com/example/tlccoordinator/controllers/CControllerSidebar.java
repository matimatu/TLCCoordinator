package com.example.tlccoordinator.controllers;

import com.example.tlccoordinator.CCacheManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CControllerSidebar extends CBaseController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private StackPane contentArea;

    public void backToLoginButtonOnAction(ActionEvent event) throws IOException {
        switchToAnotherScene("login.fxml",event,520,400);
    }
    public void initialize() {   //questo metodo viene automaticamente chiamato quando la scena viene visualizzata
        welcomeLabel.setText("Benvenuto " + CCacheManager.getLabelText() +"!");
    }

    public void importazioniButtonOnAction(ActionEvent event)  {
        loadPage("importazioni.fxml",contentArea);
    }
    public void exitOnMouseClicked(MouseEvent event) {
        System.exit(0);
    }

    public void preparazioneEquipeButtonOnAction(MouseEvent event)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void quadranteButtonOnAction(ActionEvent event) {
        loadPage("quadrante.fxml",contentArea);
    }
}