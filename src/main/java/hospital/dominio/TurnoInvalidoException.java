package hospital.dominio;

/** Se lanza cuando una operacion viola una regla de negocio de los turnos. */
public class TurnoInvalidoException extends RuntimeException {

    public TurnoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
