module com.example.tlccoordinator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.tlccoordinator to javafx.fxml;
    exports com.example.tlccoordinator;
    exports com.example.tlccoordinator.controllers;
    opens com.example.tlccoordinator.controllers to javafx.fxml;
}