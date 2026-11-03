package hospital;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Red de seguridad para el refactoring (Golden Master / test de caracterizacion).
 * Ejecuta el programa completo con una entrada guionada y devuelve la salida de consola,
 * para compararla contra la salida registrada de la version inicial.
 */
final class CaracterizacionSupport {

    private CaracterizacionSupport() {
    }

    static String ejecutarEscenario(String nombre) throws IOException {
        InputStream entradaOriginal = System.in;
        PrintStream salidaOriginal = System.out;
        Locale localeOriginal = Locale.getDefault();
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try (InputStream entrada = abrir("escenarios/" + nombre + ".in")) {
            Locale.setDefault(Locale.ROOT);
            System.setIn(entrada);
            System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
        } finally {
            System.setIn(entradaOriginal);
            System.setOut(salidaOriginal);
            Locale.setDefault(localeOriginal);
        }
        return normalizar(salida.toString(StandardCharsets.UTF_8));
    }

    static String salidaEsperada(String nombre) throws IOException {
        try (InputStream esperado = abrir("escenarios/" + nombre + ".esperado")) {
            return normalizar(new String(esperado.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    private static InputStream abrir(String recurso) {
        InputStream stream = CaracterizacionSupport.class.getClassLoader().getResourceAsStream(recurso);
        if (stream == null) {
            throw new IllegalStateException("No se encontro el recurso de prueba: " + recurso);
        }
        return stream;
    }

    private static String normalizar(String texto) {
        return texto.replace("\r\n", "\n").strip();
    }
}
