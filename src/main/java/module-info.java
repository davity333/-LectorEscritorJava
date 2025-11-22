module org.example.lectorescritor {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.lectorescritor to javafx.fxml;
    exports org.example.lectorescritor;
    exports org.example.lectorescritor.Controllers;
    opens org.example.lectorescritor.Controllers to javafx.fxml;
}