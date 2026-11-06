package hospital.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Cobertura de cada obra social: porcentaje del arancel que paga el paciente. */
public enum ObraSocial {
    OSDE(new BigDecimal("0.10")),
    SWISS(new BigDecimal("0.10")),
    PAMI(BigDecimal.ZERO),
    PARTICULAR(BigDecimal.ONE);

    private final BigDecimal porcentajeCoseguro;

    ObraSocial(BigDecimal porcentajeCoseguro) {
        this.porcentajeCoseguro = porcentajeCoseguro;
    }

    public BigDecimal coseguroSobre(BigDecimal importe) {
        return importe.multiply(porcentajeCoseguro).setScale(2, RoundingMode.HALF_UP);
    }
}
