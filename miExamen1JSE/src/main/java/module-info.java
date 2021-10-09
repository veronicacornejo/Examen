module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	requires java.sql;
    opens com.miExamen1JSE to javafx.fxml;
    exports com.miExamen1JSE;
}