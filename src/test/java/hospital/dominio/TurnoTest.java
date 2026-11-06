package hospital.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Turno - reglas de transicion de estado")
class TurnoTest {

    private static final LocalDate FECHA = LocalDate.of(2027, 6, 10);
    private static final LocalTime HORA = LocalTime.of(10, 0);

    private Turno turnoPendiente() {
        return new Turno(1, "30111222", "MN1001", FECHA, HORA, false, "Control");
    }

    @Test
    void unTurnoNuevoNacePendiente() {
        // Arrange / Act
        Turno turno = turnoPendiente();
        // Assert
        assertEquals(EstadoTurno.PENDIENTE, turno.getEstado());
        assertTrue(turno.estaPendiente());
    }

    @Test
    void cancelarUnTurnoPendienteLoDejaCancelado() {
        // Arrange
        Turno turno = turnoPendiente();
        // Act
        turno.cancelar();
        // Assert
        assertEquals(EstadoTurno.CANCELADO, turno.getEstado());
        assertFalse(turno.estaActivo());
    }

    @Test
    void noSePuedeCancelarUnTurnoAtendido() {
        // Arrange
        Turno turno = turnoPendiente();
        turno.atender();
        // Act
        TurnoInvalidoException error = assertThrows(TurnoInvalidoException.class, turno::cancelar);
        // Assert
        assertEquals("no se puede cancelar un turno ya atendido", error.getMessage());
    }

    @Test
    void noSePuedeAtenderUnTurnoCancelado() {
        // Arrange
        Turno turno = turnoPendiente();
        turno.cancelar();
        // Act
        TurnoInvalidoException error = assertThrows(TurnoInvalidoException.class, turno::atender);
        // Assert
        assertEquals("no se puede atender un turno cancelado", error.getMessage());
    }

    @Test
    void unTurnoCanceladoNoOcupaElHorarioDelProfesional() {
        // Arrange
        Turno turno = turnoPendiente();
        turno.cancelar();
        // Act
        boolean ocupa = turno.ocupaHorario("MN1001", FECHA, HORA);
        // Assert
        assertFalse(ocupa);
    }
}
