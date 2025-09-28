package guibarao.advsusp;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Scanner;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        ScreenManager screenManager = new ScreenManager(stage);
        screenManager.renderizaTelaInicial();


    }
}

