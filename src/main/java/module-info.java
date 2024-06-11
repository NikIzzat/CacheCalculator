module org.nick.cachecalculator1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.nick.cachecalculator1 to javafx.fxml;
    exports org.nick.cachecalculator1;
}