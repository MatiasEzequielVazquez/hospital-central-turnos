package hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Caracterizacion - modulo de busqueda y reportes")
class ReportesCaracterizacionTest {

    @Test
    @DisplayName("Busqueda de turnos con cada combinacion de filtros")
    void busquedaConFiltrosConservaSalida() throws Exception {
        // Arrange
        String esperado = CaracterizacionSupport.salidaEsperada("reportes-busqueda");
        // Act
        String obtenido = CaracterizacionSupport.ejecutarEscenario("reportes-busqueda");
        // Assert
        assertEquals(esperado, obtenido);
    }

    @Test
    @DisplayName("Reporte general: conteos y facturacion por obra social y especialidad")
    void reporteGeneralConservaSalida() throws Exception {
        // Arrange
        String esperado = CaracterizacionSupport.salidaEsperada("reportes-general");
        // Act
        String obtenido = CaracterizacionSupport.ejecutarEscenario("reportes-general");
        // Assert
        assertEquals(esperado, obtenido);
    }
}
