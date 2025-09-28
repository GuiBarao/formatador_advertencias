package guibarao.advsusp.controller;

import guibarao.advsusp.domain.TipoJustificativa;
import guibarao.advsusp.models.Justificativa;
import guibarao.advsusp.services.RegimentoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.List;
import java.util.Objects;


public class EdicaoRegimentoController {

    @FXML
    private Text tituloEdicao;

    @FXML
    private Button botaoAdd;

    @FXML
    private VBox regimento;

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

    private void editarJustificativa(ActionEvent event) {

        HBox boxJustificativa = (HBox) ((Button) event.getSource()).getParent();

        Text texto = (Text) boxJustificativa.getChildren().stream().filter(node -> node instanceof Text).findFirst().get();

        boxJustificativa.getChildren().remove(texto);


        Justificativa justificativa = (Justificativa) boxJustificativa.getUserData();

        TextField campoEdicao = new TextField(justificativa.descricao());

        ImageView confirmIcon = new ImageView(new Image(Objects.requireNonNull(getClass().
                getResourceAsStream("/icons/confirm.png"))));
        Button confirmar = new Button();
        confirmar.setGraphic(confirmIcon);

        ImageView cancelIcon = new ImageView(new Image(Objects.requireNonNull(getClass().
                getResourceAsStream("/icons/cancel.png"))));
        Button cancelar = new Button();
        cancelar.setGraphic(cancelIcon);

        HBox boxEdicao = new HBox(campoEdicao, confirmar, cancelar);

        Pane paiBox = (Pane)  boxJustificativa.getParent();

        int idBoxJustificativa = paiBox.getChildren().indexOf(boxJustificativa);

        paiBox.getChildren().set(idBoxJustificativa, boxEdicao);

    }

    private List<HBox> getItensRegimento(List<Justificativa> justificativas) {

        return justificativas.stream().map((j) -> {
            Text text = new Text(j.descricao());
            text.setFont(new Font(16));


            ImageView trashIcon = new ImageView(new Image(Objects.requireNonNull(getClass().
                    getResourceAsStream("/icons/trash.png"))));

            ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().
                    getResourceAsStream("/icons/pencil.png"))));

            Button excluir = new Button();
            excluir.setGraphic(trashIcon);


            Button editar = new Button();
            editar.setGraphic(editIcon);
            editar.setOnAction(this::editarJustificativa);

            HBox hBox = new HBox(text, editar, excluir);

            hBox.setUserData(j);

            hBox.setSpacing(10);
            hBox.setPadding(new Insets(10));

            hBox.setBorder(new Border( new BorderStroke(Color.GRAY,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(6),
                    new BorderWidths(1)) ));

            return hBox;
        }).toList();


    }

    public void popularJustificativas() {

        List<Justificativa> justificativas = regimentoService.getJustificativas(tipoEdicao);

        List<HBox> itensRegimento = getItensRegimento(justificativas);

        regimento.getChildren().addAll(itensRegimento);

    }


}
