package guibarao.advsusp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import guibarao.advsusp.models.Justificativa;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class RegimentoService {

        private List<Justificativa> deveres;
        private List<Justificativa> proibicoes;

        public RegimentoService () {
            try{
                String CAMINHO_JSON_PROIBICOES = "src/main/resources/proibicoes.json";
                String CAMINHO_JSON_DEVERES = "src/main/resources/deveres.json";

                this.proibicoes = getDadosJustificativasJSON(CAMINHO_JSON_PROIBICOES);
                this.deveres = getDadosJustificativasJSON(CAMINHO_JSON_DEVERES);
            } catch (IOException e) {
                System.out.println("Erro ao ler os arquivos de proibições e deveres.");
                throw new RuntimeException(e);
            }

        }

        private List<Justificativa> getDadosJustificativasJSON(String caminhoJson) throws IOException {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(new File(caminhoJson), new TypeReference<List<Justificativa>>(){});
        }




}
