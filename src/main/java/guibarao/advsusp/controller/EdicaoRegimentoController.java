package guibarao.advsusp.controller;

import guibarao.advsusp.domain.TipoJustificativa;
import guibarao.advsusp.models.Justificativa;
import guibarao.advsusp.services.RegimentoService;
import guibarao.advsusp.util.IconGetter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;


public class EdicaoRegimentoController {

    @FXML
    private Text tituloEdicao;

    @FXML
    private Button botaoAdd;

    @FXML
    private VBox paneRegimento;

    private final RegimentoService regimentoService;

    private TipoJustificativa tipoEdicao = null;

    public EdicaoRegimentoController() {
        this.regimentoService = new RegimentoService();
    }

    public void setTipoEdicao(TipoJustificativa tipoEdicao) {
        this.tipoEdicao = tipoEdicao;
    }

    @FXML
    public void initialize() {}

    public void customInitialize() {

        switch(tipoEdicao) {
            case TipoJustificativa.DEVERES:
                tituloEdicao.setText("Deveres dos alunos");
                botaoAdd.setText("Adicionar novo dever +");
                break;
            case TipoJustificativa.PROIBICAO:
                tituloEdicao.setText("Proibições dos alunos");
                botaoAdd.setText("Adicionar nova proibição +");
                break;
            default:
                throw new IllegalStateException("Tipo justificativa inválida.");
        }

        popularJustificativas();
    }

    private HBox criarCampoEdicao(String textoBase) {
        TextField campoTextual = new TextField(textoBase);

        Button confimarEdicao = createButton(IconGetter.getIconImageView("confirm.png"), null);
        Button cancelarEdicao = createButton(IconGetter.getIconImageView("cancel.png"), null);

        HBox campoEdicao = new HBox(campoTextual, confimarEdicao, cancelarEdicao);

        campoEdicao.getStyleClass().add("item-regimento");

        return campoEdicao;

    }


    private void replacePaneFromParent(Pane oldPane, Pane newPane) {
        Pane parentPane = (Pane) oldPane.getParent();
        int indexOldPane = parentPane.getChildren().indexOf(oldPane);

        parentPane.getChildren().set(indexOldPane, newPane);

    }

    private void exibirEdicaoJustificativa(HBox itemRegimento) {
        Justificativa justificativa = (Justificativa) itemRegimento.getUserData();

        HBox campoEdicao = criarCampoEdicao(justificativa.getDescricao());

        replacePaneFromParent(itemRegimento, campoEdicao);



    }

    private void editarJustificativa(ActionEvent event) {

        HBox itemRegimento = (HBox) ((Button) event.getSource()).getParent();

        exibirEdicaoJustificativa(itemRegimento);

    }

    private Button createButton(ImageView icon, EventHandler<ActionEvent> onAction) {
        Button novoBotao = new Button();
        novoBotao.setGraphic(icon);
        novoBotao.setOnAction(onAction);
        return novoBotao;
    }

    private HBox justificativaToItemRegimento(Justificativa justificativa) {

        Text descricao = new Text(justificativa.getDescricao());
        descricao.getStyleClass().add("descricao-regimento");

        ImageView trashIcon = IconGetter.getIconImageView("trash.png");
        ImageView editIcon = IconGetter.getIconImageView("pencil.png");

        Button excluir = createButton(trashIcon, null);

        Button editar = createButton(editIcon, this::editarJustificativa);

        HBox hBox = new HBox(descricao, editar, excluir);
        hBox.setUserData(justificativa);
        hBox.getStyleClass().add("item-regimento");

        return hBox;
    }


    public void popularJustificativas() {

        List<Justificativa> justificativas = regimentoService.getJustificativas(tipoEdicao);

        List<HBox> itensRegimento = justificativas.stream().map(this::justificativaToItemRegimento).toList();

        paneRegimento.getChildren().addAll(itensRegimento);

    }


}
