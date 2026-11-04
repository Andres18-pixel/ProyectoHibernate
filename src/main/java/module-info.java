module com.tuempresa.proyectohibernate {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuempresa.proyectohibernate to javafx.fxml;
    exports com.tuempresa.proyectohibernate;
}