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


    public void renderizaTelaInicial() {

        try {
            FXMLLoader telaLoader = new FXMLLoader(Objects.requireNonNull(getClass().
                    getResource("/telas/Formatador.fxml")));

            Parent tela = telaLoader.load();

            ((FormatadorController) telaLoader.getController()).setScreenManager(this);

            Scene scene = new Scene(tela);

            window.setScene(scene);

            window.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void renderizaTelaEdicaoRegimento(TipoJustificativa tipo) {
        try {

            FXMLLoader loaderTela = new FXMLLoader(Objects.requireNonNull(getClass().
                    getResource("/telas/EdicaoRegimento.fxml")));

            Parent telaEdicao = loaderTela.load();

            EdicaoRegimentoController regimentoController = loaderTela.getController();

            regimentoController.setTipoEdicao(tipo);
            regimentoController.customInitialize();

            Stage edicaoWindow = new Stage();
            edicaoWindow.setScene(new Scene(telaEdicao));
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
