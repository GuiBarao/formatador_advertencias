package guibarao.advsusp.controller;

import guibarao.advsusp.ScreenManager;
import guibarao.advsusp.domain.TipoJustificativa;
import guibarao.advsusp.models.Justificativa;
import guibarao.advsusp.services.FormatadorService;
import guibarao.advsusp.services.RegimentoService;
import guibarao.advsusp.domain.TipoDocumento;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class FormatadorController {

    private RegimentoService regimentoService = null;
    private FormatadorService formatadorService = null;
    private ScreenManager screenManager = null;


    @FXML
    private BorderPane telaRaiz;

    @FXML
    private DatePicker select_data;

    @FXML
    private TextField descricao_fatos;

    @FXML
    private VBox paneDeveres;

    @FXML
    private VBox paneProibicoes;

    @FXML
    private TextField input_nome_aluno;

    @FXML
    private TextField input_turma_aluno;

    @FXML
    private CheckBox opcao_advertencia;

    @FXML
    private CheckBox opcao_suspensao;

    @FXML
    private DatePicker retorno_suspensao_data;

    @FXML
    private DatePicker inicio_suspensao_data;

    @FXML
    private HBox datas_suspensao;

    private final EventHandler<ActionEvent> editarRegimento = event -> {
        String idMenuItem = ((MenuItem) event.getSource()).getId();
        this.screenManager.renderizaTelaEdicaoRegimento(
                idMenuItem.equals("abrirEdicaoDeveres") ? TipoJustificativa.DEVERES : TipoJustificativa.PROIBICAO
        );
    };


    public FormatadorController() {}

    @FXML
    public void initialize() {

        setService(new RegimentoService());
        setService(new FormatadorService());

        setConfiguracoesIniciais();

        popularJustificativas(this.paneDeveres, TipoJustificativa.DEVERES);
        popularJustificativas(this.paneProibicoes, TipoJustificativa.PROIBICAO);

    }

    private void setConfiguracoesIniciais() {
        opcao_advertencia.setSelected(true);
        showDatasSuspensao(false);
        inicio_suspensao_data.setValue(LocalDate.now());
        select_data.setValue(LocalDate.now());
    }

    private void setService(RegimentoService service) {
        this.regimentoService = service;
    }

    private void setService(FormatadorService service) {
        this.formatadorService = service;
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @FXML
    private void editarRegimento(ActionEvent event) {
        editarRegimento.handle(event);
    }


    private List<CheckBox> listaCheckBoxes(List<Justificativa> justificativas) {
        return justificativas.stream().map(j -> {
            CheckBox checkBox = new CheckBox(j.getDescricao());

            checkBox.getStyleClass().add("checkBox-justificativa");

            checkBox.setMnemonicParsing(false);
            checkBox.setUserData(j);

            checkBox.prefWidthProperty().bind(
                    paneDeveres.widthProperty().subtract(
                            Bindings.createDoubleBinding(() -> {
                                Insets p = paneDeveres.getPadding();
                                return p == null ? 0 : (p.getLeft() + p.getRight());
                            }, paneDeveres.paddingProperty())
                    )
            );

            return checkBox;
        }).toList();
    }

    private void popularJustificativas(Pane painelVisualizacao, TipoJustificativa tipo) {
        List<Justificativa> justificativas = regimentoService.getJustificativas(tipo);

        List<CheckBox> checkBoxes = listaCheckBoxes(justificativas);

        painelVisualizacao.getChildren().addAll(checkBoxes);

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
        formatadorService.setDeveres(getJustificativasSelecionadas(TipoJustificativa.DEVERES));
        formatadorService.setProibicoes(getJustificativasSelecionadas(TipoJustificativa.PROIBICAO));

        if (opcao_advertencia.isSelected()) {
            formatadorService.setTipoDocumento(TipoDocumento.ADVERTENCIA);
        }
        else if(opcao_suspensao.isSelected()) {
            formatadorService.setTipoDocumento(TipoDocumento.SUSPENSAO);
            formatadorService.setInicioSuspensao(inicio_suspensao_data.getValue());
            formatadorService.setRetornoSuspensao(retorno_suspensao_data.getValue());
        }



    }

    public Pane getPaneJustificativas(TipoJustificativa tipo) {
        return switch (tipo) {
            case TipoJustificativa.DEVERES -> paneDeveres;
            case TipoJustificativa.PROIBICAO -> paneProibicoes;
        };
    }

    private List<Justificativa> getJustificativasSelecionadas(TipoJustificativa tipo) {

        Pane pane = getPaneJustificativas(tipo);

        return pane.getChildren().stream().filter(   (Object elemento) ->
                                                    (elemento instanceof CheckBox && ((CheckBox) elemento).isSelected())
                                                ).map(checkBox -> (Justificativa) checkBox.getUserData()).toList();
    }






}
