package hospital.turnos;

import hospital.dominio.Turno;
import hospital.dominio.TurnoInvalidoException;
import java.util.Scanner;
import java.util.function.IntFunction;

/** Interaccion por consola de los casos de uso de turnos (opciones 1 a 3 del menu). */
public class TurnosConsola {

    private final Scanner entrada;
    private final TurnoService servicio;

    public TurnosConsola(Scanner entrada, TurnoService servicio) {
        this.entrada = entrada;
        this.servicio = servicio;
    }

    public void registrar() {
        SolicitudTurno solicitud = leerSolicitud();
        try {
            TurnoRegistrado registrado = servicio.registrar(solicitud);
            Turno turno = registrado.turno();
            System.out.println("Turno #" + turno.getId() + " registrado para " + registrado.paciente().nombre()
                    + " con " + registrado.profesional().nombre() + " el " + turno.getFecha() + " "
                    + turno.getHora() + (turno.isUrgente() ? " [URGENTE]" : ""));
        } catch (TurnoInvalidoException e) {
            informarError(e.getMessage());
        }
    }

    public void cancelar() {
        operarSobreTurno(idTurno -> {
            servicio.cancelar(idTurno);
            return "Turno #" + idTurno + " cancelado";
        });
    }

    public void atender() {
        operarSobreTurno(idTurno -> {
            servicio.atender(idTurno);
            return "Turno #" + idTurno + " marcado como atendido";
        });
    }

    /** Pide un id, ejecuta la operacion e informa el resultado o el error. */
    private void operarSobreTurno(IntFunction<String> operacion) {
        String texto = preguntar("Id de turno: ");
        try {
            System.out.println(operacion.apply(Integer.parseInt(texto)));
        } catch (NumberFormatException e) {
            informarError("id invalido");
        } catch (TurnoInvalidoException e) {
            informarError(e.getMessage());
        }
    }

    private void informarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }

    private SolicitudTurno leerSolicitud() {
        String dni = preguntar("DNI paciente: ");
        String matricula = preguntar("Matricula profesional: ").toUpperCase();
        String fecha = preguntar("Fecha (AAAA-MM-DD): ");
        String hora = preguntar("Hora (HH:MM): ");
        boolean urgente = preguntar("Urgente (s/n): ").equalsIgnoreCase("s");
        String motivo = preguntar("Motivo: ");
        return new SolicitudTurno(dni, matricula, fecha, hora, urgente, motivo);
    }

    private String preguntar(String etiqueta) {
        System.out.print(etiqueta);
        return entrada.nextLine().trim();
    }
}
