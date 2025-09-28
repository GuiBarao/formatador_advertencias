package guibarao.advsusp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import guibarao.advsusp.domain.TipoJustificativa;
import guibarao.advsusp.models.Justificativa;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class RegimentoService {

        private final String CAMINHO_JSON_PROIBICOES = "/regimento/proibicoes.json";
        private final String CAMINHO_JSON_DEVERES = "/regimento/deveres.json";


        private List<Justificativa> deveres;
        private List<Justificativa> proibicoes;

        public RegimentoService () {
            try{
                this.proibicoes = getDadosJustificativasJSON(TipoJustificativa.PROIBICAO);
                this.deveres = getDadosJustificativasJSON(TipoJustificativa.DEVERES);
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

        private String getPathJustificativasJSON(TipoJustificativa tipo) {
            return switch (tipo) {
                case TipoJustificativa.DEVERES -> this.CAMINHO_JSON_DEVERES;
                case TipoJustificativa.PROIBICAO -> this.CAMINHO_JSON_PROIBICOES;
            };
        }

        private List<Justificativa> getDadosJustificativasJSON(TipoJustificativa tipo) throws IOException {

            String caminhoJson = getPathJustificativasJSON(tipo);

            if(caminhoJson == null) return null;

            try(InputStream is = RegimentoService.class.getResourceAsStream(caminhoJson)){
                ObjectMapper mapper = new ObjectMapper();

                List<Justificativa> desserializados = mapper.readValue(is,
                        new TypeReference<List<Justificativa>>(){});

                desserializados.forEach(j -> j.setTipo(tipo));

                return desserializados;
            }

        }





}
