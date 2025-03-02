package com.example.tlccoordinator.controllers;

import com.example.tlccoordinator.CCacheManager;
import com.example.tlccoordinator.CDatabaseHandler;
import com.example.tlccoordinator.CMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.tlccoordinator.CDatabaseHandler.*;
import static com.example.tlccoordinator.CDatabaseHandler.eventi_numeroEdizione;


public class CBaseController {

    public enum EventType {
        TLC,
        Zikaron,
        Rinnovamento,
    }

    CDatabaseHandler connector = new CDatabaseHandler();
    public void switchToAnotherScene(String fxmlName, ActionEvent event) throws IOException {
        switchToAnotherScene(fxmlName,event, CCacheManager.getScreenWidth(),CCacheManager.getScreenHeight());
    }
    public void switchToAnotherScene(String fxmlName, ActionEvent event, double width, double length) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CMain.class.getResource(fxmlName));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(),width, length);
        stage.setScene(scene);
        stage.show();
    }
    public void loadPage(String fxmlName, StackPane contentArea)
    {
        try
        {
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(CMain.class.getResource((fxmlName))));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
        }
        catch (IOException e)
        {
            CBaseController.showAlert("Errore","Problema durante il caricamento della nuova pagina",e.getMessage());
        }
    }

    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

    }

    public void modifyCursorOnMouseEntered(MouseEvent event){
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setCursor(Cursor.HAND);
    }
    public void modifyCursorOnMouseExited(MouseEvent event){
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setCursor(Cursor.DEFAULT);
    }

    public String checkNumeroEdizioneEvento(int numeroEdizione, EventType eventType){
        String error = "";
        int id_tipologiaevento =0;
        switch (eventType) {
            case TLC -> {
                id_tipologiaevento=1;
            }
            case Zikaron -> {
                id_tipologiaevento = 3;
            }
            case Rinnovamento -> {
                id_tipologiaevento = 2;
            }
            default -> throw new UnsupportedOperationException("valore non considerato");
        }
        String query = "SELECT "
                + eventi_ID + " "
                +"FROM " + eventi_TABLE + " "
                +"WHERE " + "TRIM(LOWER(" + eventi_id_tipologiaEvento + "))" + " = TRIM(LOWER(?))" +  " "
                +   "AND " + eventi_numeroEdizione + " = ?";
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id_tipologiaevento);
            ps.setInt(2, numeroEdizione);
            ResultSet rs= ps.executeQuery();
            if(!rs.next())
                error = "Numero edizione evento " + eventType.toString() + " non presente nel database";
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_gruppo",e.getMessage());
        }
        return error;
    }
}
