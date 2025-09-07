package guibarao.advsusp.domain;

import lombok.Getter;

@Getter
public enum TipoDocumento {
    ADVERTENCIA("Advertência"),
    SUSPENSAO("Suspensão");

    private final String nomeTipo;

    TipoDocumento(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

}
