package guibarao.advsusp.services;

import guibarao.advsusp.domain.TipoDocumento;
import guibarao.advsusp.models.Justificativa;
import lombok.Getter;
import lombok.Setter;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Numberings;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FormatadorService {

    private String nomeAluno;
    private String turmaAluno;
    private TipoDocumento tipoDocumento;
    private LocalDate dataDocumento;
    private LocalDate inicioSuspensao;
    private LocalDate retornoSuspensao;
    private String descricao;
    private List<Justificativa> proibicoes = new ArrayList<>();
    private List<Justificativa> deveres = new ArrayList<>();
    private Map<String, Object> dadosDocx = new HashMap<>();
    private final String CAMINHO_TEMPLATE_DOCX = "/Template.docx";
    private ByteArrayOutputStream templateRenderizado = new ByteArrayOutputStream();

    public void formatarDocx() throws Exception {

        popularDadosDocx();

        this.templateRenderizado = renderizarTemplate();

    }

    private ByteArrayOutputStream renderizarTemplate() throws IOException {

        try(InputStream is = FormatadorService.class.getResourceAsStream(CAMINHO_TEMPLATE_DOCX)) {

            if (is == null) {
                throw new IOException();
            }

            try(var tpl = XWPFTemplate.compile(is).render(dadosDocx)){
                ByteArrayOutputStream template = new ByteArrayOutputStream();
                tpl.write(template);
                return template;
            }
        }

    }

    private boolean todosDadosInseridos() {

        if(deveres.isEmpty() && proibicoes.isEmpty()) {
            return false;
        }

        if(tipoDocumento == null) {
            return false;
        }
        else {
            if(tipoDocumento == TipoDocumento.SUSPENSAO) {
                return nomeAluno != null && turmaAluno != null && dataDocumento != null && descricao != null && retornoSuspensao != null && inicioSuspensao != null;
            }
            else {
                return nomeAluno != null && turmaAluno != null && dataDocumento != null && descricao != null;
            }
        }


    }

    private void popularDadosDocx() throws Exception {

        if(todosDadosInseridos()) {
            dadosDocx.put("dia", this.dataDocumento.getDayOfMonth());
            dadosDocx.put("mes", this.dataDocumento.getMonthValue());
            dadosDocx.put("ano", this.dataDocumento.getYear());
            dadosDocx.put("nomeAluno", this.nomeAluno);
            dadosDocx.put("turmaAluno", this.turmaAluno);
            dadosDocx.put("tipoDocumento", this.tipoDocumento.getNomeTipo());
            dadosDocx.put("justificaComProibicoes", !this.proibicoes.isEmpty());
            dadosDocx.put("justificaComDeveres", !this.deveres.isEmpty());
            dadosDocx.put("descricao", this.descricao);
            dadosDocx.put("deveres", Numberings.create(
                    deveres.stream().map(Justificativa::descricao).toArray(String[]::new)
            ));
            dadosDocx.put("proibicoes", Numberings.create(
                    proibicoes.stream().map(Justificativa::descricao).toArray(String[]::new)
            ));

            dadosDocx.put("documentoSuspensao", this.tipoDocumento == TipoDocumento.SUSPENSAO);

            if(this.tipoDocumento == TipoDocumento.SUSPENSAO) {
                dadosDocx.put("diaRetornoSuspensao", this.retornoSuspensao.getDayOfMonth());
                dadosDocx.put("mesRetornoSuspensao", this.retornoSuspensao.getMonthValue());
                dadosDocx.put("anoRetornoSuspensao", this.retornoSuspensao.getYear());


                //Sábado e domingo contabiliza na conta?
                dadosDocx.put("diasSuspenso", ChronoUnit.DAYS.between(this.inicioSuspensao, this.retornoSuspensao));
            }

        }
        else{
            throw new Exception("Insira todos os dados necessários.");
        }
    }


}
