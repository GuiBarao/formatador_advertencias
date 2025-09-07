package guibarao.advsusp;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {


    Stage window;

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;

        Parent tela = FXMLLoader.load(Objects.requireNonNull(getClass().
                getResource("telas/Formatador.fxml")));

        Scene scene = new Scene(tela);
        window.setScene(scene);

        window.show();

    }
}

