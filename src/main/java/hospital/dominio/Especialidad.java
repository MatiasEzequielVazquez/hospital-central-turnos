package hospital.dominio;

import java.math.BigDecimal;

/** Especialidades medicas con su arancel base de consulta. */
public enum Especialidad {
    CLINICA(new BigDecimal("10000")),
    CARDIOLOGIA(new BigDecimal("15000")),
    PEDIATRIA(new BigDecimal("12000"));

    private final BigDecimal arancelBase;

    Especialidad(BigDecimal arancelBase) {
        this.arancelBase = arancelBase;
    }

    public BigDecimal arancelBase() {
        return arancelBase;
    }
}
