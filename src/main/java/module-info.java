module guibarao.advsusp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires static lombok;
    requires poi.tl;
    requires java.desktop;
    exports guibarao.advsusp.models to com.fasterxml.jackson.databind;


    opens guibarao.advsusp to javafx.fxml;
    opens guibarao.advsusp.controller to javafx.fxml;
    exports guibarao.advsusp to javafx.graphics;
    opens guibarao.advsusp.models to javafx.fxml;
}