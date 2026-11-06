package hospital.reportes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hospital.dominio.Especialidad;
import hospital.dominio.ObraSocial;
import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CalculadoraArancel - arancel y coseguro")
class CalculadoraArancelTest {

    private final CalculadoraArancel calculadora = new CalculadoraArancel();

    private DetalleFacturacion calcular(ObraSocial obraSocial, Especialidad especialidad, boolean urgente) {
        Turno turno = new Turno(1, "1", "MN1", LocalDate.of(2027, 6, 1), LocalTime.of(10, 0), urgente, "Motivo");
        return calculadora.calcular(turno, new Paciente("1", "Paciente", obraSocial),
                new Profesional("MN1", "Profesional", especialidad));
    }

    @Test
    void osdePagaNoventaPorCientoDeUnaConsultaClinica() {
        // Act
        DetalleFacturacion detalle = calcular(ObraSocial.OSDE, Especialidad.CLINICA, false);
        // Assert
        assertEquals(new BigDecimal("10000.00"), detalle.importeTotal());
        assertEquals(new BigDecimal("1000.00"), detalle.aCargoPaciente());
        assertEquals(new BigDecimal("9000.00"), detalle.aCargoObraSocial());
    }

    @Test
    void laUrgenciaRecargaVeintePorCientoSinErroresDeRedondeo() {
        // Act
        DetalleFacturacion detalle = calcular(ObraSocial.SWISS, Especialidad.CARDIOLOGIA, true);
        // Assert
        assertEquals(new BigDecimal("18000.00"), detalle.importeTotal());
        assertEquals(new BigDecimal("1800.00"), detalle.aCargoPaciente());
    }

    @Test
    void pamiCubreElTotalYParticularPagaElTotal() {
        // Act
        DetalleFacturacion pami = calcular(ObraSocial.PAMI, Especialidad.PEDIATRIA, false);
        DetalleFacturacion particular = calcular(ObraSocial.PARTICULAR, Especialidad.PEDIATRIA, false);
        // Assert
        assertEquals(new BigDecimal("0.00"), pami.aCargoPaciente());
        assertEquals(new BigDecimal("12000.00"), particular.aCargoPaciente());
        assertEquals(new BigDecimal("0.00"), particular.aCargoObraSocial());
    }
}
