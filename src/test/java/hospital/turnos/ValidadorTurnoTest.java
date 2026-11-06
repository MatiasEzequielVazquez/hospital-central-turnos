package hospital.turnos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hospital.dominio.Turno;
import hospital.dominio.TurnoInvalidoException;
import hospital.repositorio.TurnoRepositorioEnMemoria;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ValidadorTurno - reglas de alta con reloj fijo")
class ValidadorTurnoTest {

    /** "Hoy" fijo para que las pruebas no dependan de la fecha real. */
    private static final Clock HOY_1_DE_JUNIO_2027 =
            Clock.fixed(Instant.parse("2027-06-01T12:00:00Z"), ZoneOffset.UTC);

    private TurnoRepositorioEnMemoria turnos;
    private ValidadorTurno validador;

    @BeforeEach
    void prepararValidador() {
        turnos = new TurnoRepositorioEnMemoria();
        validador = new ValidadorTurno(turnos, HOY_1_DE_JUNIO_2027);
    }

    private Turno turno(int id, String dni, String matricula, String fecha, String hora, boolean urgente) {
        return new Turno(id, dni, matricula, LocalDate.parse(fecha), LocalTime.parse(hora), urgente, "Motivo");
    }

    @Test
    void aceptaUnTurnoParaHoyDentroDelHorario() {
        // Arrange
        Turno nuevo = turno(1, "30111222", "MN1001", "2027-06-01", "19:59", false);
        // Act / Assert
        assertDoesNotThrow(() -> validador.validar(nuevo));
    }

    @Test
    void rechazaUnTurnoParaAyer() {
        // Arrange
        Turno nuevo = turno(1, "30111222", "MN1001", "2027-05-31", "10:00", false);
        // Act
        TurnoInvalidoException error = assertThrows(TurnoInvalidoException.class, () -> validador.validar(nuevo));
        // Assert
        assertEquals("no se pueden dar turnos en fechas pasadas", error.getMessage());
    }

    @Test
    void rechazaLosLimitesFueraDelHorarioDeAtencion() {
        // Arrange
        Turno antesDeAbrir = turno(1, "30111222", "MN1001", "2027-06-02", "07:59", false);
        Turno alCerrar = turno(2, "30111222", "MN1001", "2027-06-02", "20:00", false);
        // Act / Assert
        assertThrows(TurnoInvalidoException.class, () -> validador.validar(antesDeAbrir));
        assertThrows(TurnoInvalidoException.class, () -> validador.validar(alCerrar));
    }

    @Test
    void rechazaSiElProfesionalYaTieneTurnoEnEseHorario() {
        // Arrange
        turnos.guardar(turno(1, "28999888", "MN1001", "2027-06-02", "10:00", false));
        Turno nuevo = turno(2, "30111222", "MN1001", "2027-06-02", "10:00", false);
        // Act
        TurnoInvalidoException error = assertThrows(TurnoInvalidoException.class, () -> validador.validar(nuevo));
        // Assert
        assertEquals("el profesional ya tiene un turno en ese horario", error.getMessage());
    }

    @Test
    void permiteUnSegundoTurnoPendienteSoloSiEsUrgente() {
        // Arrange
        turnos.guardar(turno(1, "30111222", "MN2002", "2027-06-02", "09:00", false));
        Turno comun = turno(2, "30111222", "MN1001", "2027-06-03", "10:00", false);
        Turno urgente = turno(3, "30111222", "MN1001", "2027-06-03", "11:00", true);
        // Act / Assert
        assertThrows(TurnoInvalidoException.class, () -> validador.validar(comun));
        assertDoesNotThrow(() -> validador.validar(urgente));
    }
}
