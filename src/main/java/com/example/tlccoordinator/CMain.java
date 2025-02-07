package com.example.tlccoordinator;

import javafx.fxml.FXMLLoader;
//import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CMain extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CMain.class.getResource("login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);        // to remove the name of the application form the top
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
//        scene.setFill(Color.TRANSPARENT);       //to remove the three icons from the top of the pane
//        stage.initStyle(StageStyle.TRANSPARENT);  //to remove the three icons from the top of the pane
// scommentare per mettere le dimensioni dello schermo al massimo
//        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
//        CCacheManager.setScreenHeight(screenBounds.getHeight());
//        CCacheManager.setScreenWidth(screenBounds.getWidth());
        stage.setTitle("TLC Coordinator");
        stage.setScene(scene);
        stage.centerOnScreen();

        // Listen for stage size changes
        stage.widthProperty().addListener((observable, oldValue, newValue) -> centerStage(stage));
        stage.heightProperty().addListener((observable, oldValue, newValue) -> centerStage(stage));

        stage.show();
    }

    private void centerStage(Stage stage) {
        // Get screen bounds
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Calculate new position
        double newX = (screenWidth - stage.getWidth()) / 2;
        double newY = (screenHeight - stage.getHeight()) / 2;

        // Set new stage position
        stage.setX(newX);
        stage.setY(newY);
    }

    public static void main(String[] args) {
        launch();
    }
}