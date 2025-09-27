package guibarao.advsusp.controller;

import guibarao.advsusp.models.Justificativa;
import guibarao.advsusp.services.FormatadorService;
import guibarao.advsusp.services.RegimentoService;
import guibarao.advsusp.domain.TipoDocumento;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class DocumentoController implements Initializable {

    private RegimentoService regimentoService;
    private FormatadorService formatadorService;

    public DocumentoController() {}

    private void setService(RegimentoService service) {
        this.regimentoService = service;
    }

    private void setService(FormatadorService service) {
        this.formatadorService = service;
    }

    @FXML
    private BorderPane telaRaiz;

    @FXML
    private VBox dados_suspensão;

    @FXML
    private DatePicker select_data;

    @FXML
    private TextField descricao_fatos;

    @FXML
    private VBox deveres;

    @FXML
    private TextField input_nome_aluno;

    @FXML
    private TextField input_turma_aluno;

    @FXML
    private Label label_data;

    @FXML
    private Label label_sancao;

    @FXML
    private Menu menu_arquivo;

    @FXML
    private Menu menu_configuracao;

    @FXML
    private CheckBox opcao_advertencia;

    @FXML
    private CheckBox opcao_suspensao;

    @FXML
    private VBox proibicoes;

    @FXML
    private DatePicker retorno_suspensao_data;

    @FXML
    private DatePicker inicio_suspensao_data;

    @FXML
    private HBox datas_suspensao;

    @FXML
    private HBox tipo_documento;

    @FXML
    private VBox tipo_sancao;

    @FXML
    private MenuItem exportarDocx;

    @FXML
    private ScrollPane scrollDeveres;

    @FXML
    private ScrollPane scrollProibicoes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setService(new RegimentoService());
        setService(new FormatadorService());

        opcao_advertencia.setSelected(true);
        showDatasSuspensao(false);
        inicio_suspensao_data.setValue(LocalDate.now());
        select_data.setValue(LocalDate.now());

        popularDeveres();
        popularProibicoes();

    }


    private List<CheckBox> listaCheckBoxes(List<Justificativa> justificativas) {
        return justificativas.stream().map(j -> {
            CheckBox checkBox = new CheckBox(j.descricao());
            checkBox.setFont(Font.font(16));
            checkBox.setWrapText(true);
            checkBox.setMnemonicParsing(false);
            checkBox.setUserData(j);

            checkBox.setMaxWidth(Double.MAX_VALUE);
            checkBox.setMinHeight(Region.USE_PREF_SIZE);
            checkBox.setMaxHeight(Double.MAX_VALUE);

            checkBox.prefWidthProperty().bind(
                    deveres.widthProperty().subtract(
                            Bindings.createDoubleBinding(() -> {
                                Insets p = deveres.getPadding();
                                return p == null ? 0 : (p.getLeft() + p.getRight());
                            }, deveres.paddingProperty())
                    )
            );

            return checkBox;
        }).toList();
    }



    private void popularDeveres() {
        List<Justificativa> deveresModel = regimentoService.getDeveres();

        List<CheckBox> checkBoxes = listaCheckBoxes(deveresModel);

        deveres.getChildren().addAll(checkBoxes);



    }

    private void popularProibicoes() {
        List<Justificativa> proibicoesModel = regimentoService.getProibicoes();

        List<CheckBox> checkBoxes = listaCheckBoxes(proibicoesModel);

        proibicoes.getChildren().addAll(checkBoxes);

    }

    private void showDatasSuspensao(boolean estado) {
        datas_suspensao.setVisible(estado);
        datas_suspensao.setManaged(estado);
    }

    @FXML
    void escolheuSuspensao(ActionEvent event) {
        if(opcao_suspensao.isSelected()) {
            opcao_advertencia.setSelected(false);
            showDatasSuspensao(true);
        }
    }

    @FXML
    void escolheuAdvertencia(ActionEvent event) {
        if(opcao_advertencia.isSelected()) {
            opcao_suspensao.setSelected(false);
            showDatasSuspensao(false);
        }

    }

    @FXML
    void onExportarDocx(ActionEvent event) {

        alimentarFormatadorService();

        try {
            formatadorService.formatarDocx();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar documento");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word (*.docx)", "*.docx"));
            fileChooser.setInitialFileName("documento.docx");

            File arquivoSelecionado = fileChooser.showSaveDialog(getStageAtual());


            if(arquivoSelecionado != null) {
                try {
                    Files.write(arquivoSelecionado.toPath(), formatadorService.getTemplateRenderizado().toByteArray());
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (Exception e) {
            System.out.println(2);
            System.out.println(e.getCause());
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }





    }

    private Stage getStageAtual() {
        return (Stage) telaRaiz.getScene().getWindow();
    }

    private void alimentarFormatadorService() {
        formatadorService.setDataDocumento(select_data.getValue());
        formatadorService.setDescricao(descricao_fatos.getText());
        formatadorService.setNomeAluno(input_nome_aluno.getText());
        formatadorService.setTurmaAluno(input_turma_aluno.getText());
        formatadorService.setDeveres(getDeveresSelecionados());
        formatadorService.setProibicoes(getProibicoesSelecionadas());

        if(opcao_advertencia.isSelected()) {
            formatadorService.setTipoDocumento(TipoDocumento.ADVERTENCIA);
        }
        else if(opcao_suspensao.isSelected()) {
            formatadorService.setTipoDocumento(TipoDocumento.SUSPENSAO);
            formatadorService.setInicioSuspensao(inicio_suspensao_data.getValue());
            formatadorService.setRetornoSuspensao(retorno_suspensao_data.getValue());
        }



    }

    private List<Justificativa> getProibicoesSelecionadas() {
        return proibicoes.getChildren().stream().filter(   (Object elemento) ->
                                                    (elemento instanceof CheckBox && ((CheckBox) elemento).isSelected())
                                                ).map(checkBox -> (Justificativa) checkBox.getUserData()).toList();
    }

    private List<Justificativa> getDeveresSelecionados() {
        return deveres.getChildren().stream().filter(   (Object elemento) ->
                                                    (elemento instanceof CheckBox && ((CheckBox) elemento).isSelected())
                                                ).map(checkBox -> (Justificativa) checkBox.getUserData()).toList();
    }





}
