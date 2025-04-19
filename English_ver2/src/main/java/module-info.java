module com.example.test {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires freetts;
    requires java.desktop;
    requires json.simple;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;


    opens View to javafx.fxml, com.google.gson;
    opens Service;
    exports View;
    exports View.Controller;
    opens View.Controller to javafx.fxml;


}