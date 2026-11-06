package hospital.turnos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hospital.dominio.Especialidad;
import hospital.dominio.EstadoTurno;
import hospital.dominio.ObraSocial;
import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.TurnoInvalidoException;
import hospital.repositorio.PacienteRepositorioEnMemoria;
import hospital.repositorio.ProfesionalRepositorioEnMemoria;
import hospital.repositorio.TurnoRepositorioEnMemoria;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TurnoService - casos de uso del modulo de turnos")
class TurnoServiceTest {

    private TurnoRepositorioEnMemoria turnos;
    private TurnoService servicio;

    @BeforeEach
    void prepararServicio() {
        PacienteRepositorioEnMemoria pacientes = new PacienteRepositorioEnMemoria();
        pacientes.agregar(new Paciente("30111222", "Paciente Prueba", ObraSocial.OSDE));
        ProfesionalRepositorioEnMemoria profesionales = new ProfesionalRepositorioEnMemoria();
        profesionales.agregar(new Profesional("MN1001", "Profesional Prueba", Especialidad.CLINICA));
        turnos = new TurnoRepositorioEnMemoria();
        Clock reloj = Clock.fixed(Instant.parse("2027-06-01T12:00:00Z"), ZoneOffset.UTC);
        servicio = new TurnoService(turnos, pacientes, profesionales, new ValidadorTurno(turnos, reloj));
    }

    private SolicitudTurno solicitud(String dni, String fecha, String hora) {
        return new SolicitudTurno(dni, "MN1001", fecha, hora, false, "Control");
    }

    @Test
    void registrarGuardaElTurnoPendienteConIdCorrelativo() {
        // Arrange
        SolicitudTurno pedido = solicitud("30111222", "2027-06-10", "10:00");
        // Act
        TurnoRegistrado registrado = servicio.registrar(pedido);
        // Assert
        assertEquals(1, registrado.turno().getId());
        assertEquals(EstadoTurno.PENDIENTE, registrado.turno().getEstado());
        assertEquals("Paciente Prueba", registrado.paciente().nombre());
        assertTrue(turnos.buscarPorId(1).isPresent());
    }

    @Test
    void registrarRechazaUnPacienteInexistente() {
        // Arrange
        SolicitudTurno pedido = solicitud("99999999", "2027-06-10", "10:00");
        // Act
        TurnoInvalidoException error = assertThrows(TurnoInvalidoException.class, () -> servicio.registrar(pedido));
        // Assert
        assertEquals("el paciente 99999999 no existe", error.getMessage());
    }

    @Test
    void registrarRechazaFechaMalFormada() {
        // Arrange
        SolicitudTurno pedido = solicitud("30111222", "2027-13-40", "10:00");
        // Act
        TurnoInvalidoException error = assertThrows(TurnoInvalidoException.class, () -> servicio.registrar(pedido));
        // Assert
        assertEquals("fecha u hora invalida", error.getMessage());
    }

    @Test
    void cancelarYAtenderInformanTurnoInexistente() {
        // Act
        TurnoInvalidoException alCancelar = assertThrows(TurnoInvalidoException.class, () -> servicio.cancelar(42));
        TurnoInvalidoException alAtender = assertThrows(TurnoInvalidoException.class, () -> servicio.atender(42));
        // Assert
        assertEquals("no existe el turno #42", alCancelar.getMessage());
        assertEquals("no existe el turno #42", alAtender.getMessage());
    }

    @Test
    void atenderCambiaElEstadoDelTurnoRegistrado() {
        // Arrange
        int id = servicio.registrar(solicitud("30111222", "2027-06-10", "10:00")).turno().getId();
        // Act
        servicio.atender(id);
        // Assert
        assertEquals(EstadoTurno.ATENDIDO, turnos.buscarPorId(id).orElseThrow().getEstado());
    }
}
