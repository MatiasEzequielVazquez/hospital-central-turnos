package hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Caracterizacion - modulo de turnos")
class TurnosCaracterizacionTest {

    @Test
    @DisplayName("Registro de turnos: alta valida y todas las validaciones")
    void registroDeTurnosConservaSalida() throws Exception {
        // Arrange
        String esperado = CaracterizacionSupport.salidaEsperada("turnos-registro");
        // Act
        String obtenido = CaracterizacionSupport.ejecutarEscenario("turnos-registro");
        // Assert
        assertEquals(esperado, obtenido);
    }

    @Test
    @DisplayName("Cambios de estado: cancelar y atender con sus reglas")
    void cambiosDeEstadoConservanSalida() throws Exception {
        // Arrange
        String esperado = CaracterizacionSupport.salidaEsperada("turnos-estados");
        // Act
        String obtenido = CaracterizacionSupport.ejecutarEscenario("turnos-estados");
        // Assert
        assertEquals(esperado, obtenido);
    }
}
