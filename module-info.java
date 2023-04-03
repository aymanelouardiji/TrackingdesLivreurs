module com.fstt.projectjava {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens com.fstt.projectjava to javafx.fxml;
    exports com.fstt.projectjava;
}