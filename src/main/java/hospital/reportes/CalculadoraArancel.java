package hospital.reportes;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Calcula cuanto cuesta un turno y como se reparte entre paciente y obra social. */
public class CalculadoraArancel {

    public DetalleFacturacion calcular(Turno turno, Paciente paciente, Profesional profesional) {
        BigDecimal importe = profesional.especialidad().arancelBase();
        if (turno.isUrgente()) {
            importe = importe.multiply(new BigDecimal("1.20"));
        }
        importe = importe.setScale(2, RoundingMode.HALF_UP);
        BigDecimal aCargoPaciente = paciente.obraSocial().coseguroSobre(importe);
        return new DetalleFacturacion(importe, aCargoPaciente, importe.subtract(aCargoPaciente));
    }
}
