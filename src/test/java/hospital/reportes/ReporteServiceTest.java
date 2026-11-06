package hospital.reportes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hospital.dominio.Especialidad;
import hospital.dominio.ObraSocial;
import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import hospital.repositorio.PacienteRepositorioEnMemoria;
import hospital.repositorio.ProfesionalRepositorioEnMemoria;
import hospital.repositorio.TurnoRepositorioEnMemoria;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ReporteService - datos del reporte general")
class ReporteServiceTest {

    private TurnoRepositorioEnMemoria turnos;
    private ReporteService servicio;

    @BeforeEach
    void prepararDatos() {
        PacienteRepositorioEnMemoria pacientes = new PacienteRepositorioEnMemoria();
        pacientes.agregar(new Paciente("1", "Paciente OSDE", ObraSocial.OSDE));
        ProfesionalRepositorioEnMemoria profesionales = new ProfesionalRepositorioEnMemoria();
        profesionales.agregar(new Profesional("MN1", "Clinica", Especialidad.CLINICA));
        profesionales.agregar(new Profesional("MN2", "Cardiologo", Especialidad.CARDIOLOGIA));
        turnos = new TurnoRepositorioEnMemoria();
        servicio = new ReporteService(turnos, pacientes, profesionales, new CalculadoraArancel());
    }

    private Turno nuevoTurno(int id, String matricula) {
        Turno turno = new Turno(id, "1", matricula, LocalDate.of(2027, 6, 1), LocalTime.of(9, id), false, "Motivo");
        turnos.guardar(turno);
        return turno;
    }

    @Test
    void sinTurnosElReporteQuedaEnCero() {
        // Act
        ReporteGeneral reporte = servicio.generar();
        // Assert
        assertEquals(0, reporte.totalTurnos());
        assertEquals(BigDecimal.ZERO, reporte.facturacion().importeTotal());
    }

    @Test
    void cuentaPorEstadoYExcluyeCanceladosDeLaCarga() {
        // Arrange
        nuevoTurno(1, "MN1").atender();
        nuevoTurno(2, "MN1").cancelar();
        nuevoTurno(3, "MN2");
        // Act
        ReporteGeneral reporte = servicio.generar();
        // Assert
        assertEquals(3, reporte.totalTurnos());
        assertEquals(1L, reporte.pendientes());
        assertEquals(1L, reporte.atendidos());
        assertEquals(1L, reporte.cancelados());
        assertEquals(1L, reporte.cargaPorProfesional().get(0).turnosActivos());
        assertEquals(1L, reporte.cargaPorProfesional().get(1).turnosActivos());
    }

    @Test
    void facturaSoloLosTurnosAtendidos() {
        // Arrange
        nuevoTurno(1, "MN1").atender();
        nuevoTurno(2, "MN2");
        // Act
        DetalleFacturacion facturacion = servicio.generar().facturacion();
        // Assert
        assertEquals(new BigDecimal("10000.00"), facturacion.importeTotal());
        assertEquals(new BigDecimal("1000.00"), facturacion.aCargoPaciente());
    }
}
