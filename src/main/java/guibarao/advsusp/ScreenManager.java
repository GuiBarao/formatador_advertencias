package guibarao.advsusp;

import guibarao.advsusp.controller.FormatadorController;
import guibarao.advsusp.controller.EdicaoRegimentoController;
import guibarao.advsusp.domain.TipoJustificativa;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class ScreenManager {


    private final Stage window;

    public ScreenManager(Stage window) {
        this.window = window;
    }


    private void setStyleSheet(Scene scene, String fileName) {
        scene.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/stylesSheets/".concat(fileName))).toExternalForm());
    }

    private FXMLLoader getFXMLOADER(String nomeView) {
        return new FXMLLoader(Objects.requireNonNull(getClass().
                getResource("/views/".concat(nomeView))));
    }

    public void renderizaTelaInicial() {

        try {
            FXMLLoader telaLoader = getFXMLOADER("Formatador.fxml");

            Parent tela = telaLoader.load();

            ((FormatadorController) telaLoader.getController()).setScreenManager(this);

            Scene scene = new Scene(tela);
            setStyleSheet(scene, "formatadorStyle.css");

            window.setScene(scene);

            window.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void renderizaTelaEdicaoRegimento(TipoJustificativa tipo) {
        try {

            FXMLLoader loaderTela = getFXMLOADER("EdicaoRegimento.fxml");

            Parent telaEdicao = loaderTela.load();

            EdicaoRegimentoController regimentoController = loaderTela.getController();

            regimentoController.setTipoEdicao(tipo);
            regimentoController.customInitialize();

            Stage edicaoWindow = new Stage();

            Scene sceneEdicao = new Scene(telaEdicao);

            setStyleSheet(sceneEdicao, "edicaoRegimentoStyle.css");

            edicaoWindow.setScene(sceneEdicao);
            edicaoWindow.centerOnScreen();
            edicaoWindow.resizableProperty().setValue(Boolean.TRUE);
            edicaoWindow.initOwner(window);

            edicaoWindow.show();

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }


}
