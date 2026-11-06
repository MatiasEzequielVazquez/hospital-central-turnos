package hospital.reportes;

import java.math.BigDecimal;

/** Importe de uno o varios turnos, discriminado por quien lo paga. */
public record DetalleFacturacion(BigDecimal importeTotal,
                                 BigDecimal aCargoPaciente,
                                 BigDecimal aCargoObraSocial) {

    public static final DetalleFacturacion SIN_IMPORTES =
            new DetalleFacturacion(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    public DetalleFacturacion sumar(DetalleFacturacion otro) {
        return new DetalleFacturacion(
                importeTotal.add(otro.importeTotal),
                aCargoPaciente.add(otro.aCargoPaciente),
                aCargoObraSocial.add(otro.aCargoObraSocial));
    }
}
