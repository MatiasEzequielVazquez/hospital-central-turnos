package hospital.reportes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hospital.dominio.Turno;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FiltroTurnos - criterios de busqueda")
class FiltroTurnosTest {

    private final Turno turnoUrgente = new Turno(1, "30111222", "MN2002",
            LocalDate.of(2027, 6, 15), LocalTime.of(10, 0), true, "Arritmia");

    @Test
    void unFiltroVacioAceptaCualquierTurno() {
        // Arrange
        FiltroTurnos sinCriterios = new FiltroTurnos("", "", "", "", false);
        // Act / Assert
        assertTrue(sinCriterios.acepta(turnoUrgente));
    }

    @Test
    void elEstadoSeComparaSinDistinguirMayusculas() {
        // Arrange
        FiltroTurnos pendientes = new FiltroTurnos("", "pendiente", "", "", false);
        FiltroTurnos atendidos = new FiltroTurnos("", "ATENDIDO", "", "", false);
        // Act / Assert
        assertTrue(pendientes.acepta(turnoUrgente));
        assertFalse(atendidos.acepta(turnoUrgente));
    }

    @Test
    void elRangoDeFechasIncluyeAmbosExtremos() {
        // Arrange
        FiltroTurnos mismoDia = new FiltroTurnos("", "", "2027-06-15", "2027-06-15", false);
        FiltroTurnos posterior = new FiltroTurnos("", "", "2027-06-16", "", false);
        // Act / Assert
        assertTrue(mismoDia.acepta(turnoUrgente));
        assertFalse(posterior.acepta(turnoUrgente));
    }

    @Test
    void combinaMatriculaYUrgencia() {
        // Arrange
        FiltroTurnos otraMatricula = new FiltroTurnos("MN1001", "", "", "", true);
        FiltroTurnos mismaMatricula = new FiltroTurnos("MN2002", "", "", "", true);
        // Act / Assert
        assertFalse(otraMatricula.acepta(turnoUrgente));
        assertTrue(mismaMatricula.acepta(turnoUrgente));
    }
}
