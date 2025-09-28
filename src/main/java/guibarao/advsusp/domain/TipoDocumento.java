package guibarao.advsusp.domain;



public enum TipoDocumento {
    ADVERTENCIA("Advertência"),
    SUSPENSAO("Suspensão");

    private final String nomeTipo;

    TipoDocumento(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

}
