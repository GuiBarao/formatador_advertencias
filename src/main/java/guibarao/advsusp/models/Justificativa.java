package guibarao.advsusp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import guibarao.advsusp.domain.TipoJustificativa;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Justificativa {
    private int id;
    private String descricao;
    private TipoJustificativa tipo;

    public Justificativa() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoJustificativa getTipo() {
        return tipo;
    }

    public void setTipo(TipoJustificativa tipo) {
        this.tipo = tipo;
    }
}
