package guibarao.advsusp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import guibarao.advsusp.domain.TipoJustificativa;
import guibarao.advsusp.models.Justificativa;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Getter
@Setter
public class RegimentoService {

        private List<Justificativa> deveres;
        private List<Justificativa> proibicoes;

        public RegimentoService () {
            try{
                String CAMINHO_JSON_PROIBICOES = "/proibicoes.json";
                String CAMINHO_JSON_DEVERES = "/deveres.json";

                this.proibicoes = getDadosJustificativasJSON(CAMINHO_JSON_PROIBICOES);
                this.deveres = getDadosJustificativasJSON(CAMINHO_JSON_DEVERES);
            } catch (IOException e) {
                System.out.println("Erro ao ler os arquivos de proibições e deveres.");
                throw new RuntimeException(e);
            }

        }

        public List<Justificativa> getJustificativas (TipoJustificativa tipo) {
            return switch(tipo) {
                case TipoJustificativa.DEVERES -> this.deveres;
                case TipoJustificativa.PROIBICAO -> this.proibicoes;
            };
        }

        private List<Justificativa> getDadosJustificativasJSON(String caminhoJson) throws IOException {

            try(InputStream is = RegimentoService.class.getResourceAsStream(caminhoJson)){
                ObjectMapper mapper = new ObjectMapper();

                return mapper.readValue(is, new TypeReference<List<Justificativa>>(){});
            }

        }





}
