package hospital.turnos;

import hospital.dominio.Turno;
import hospital.dominio.TurnoInvalidoException;
import java.util.Scanner;

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
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void cancelar() {
        System.out.print("Id de turno: ");
        try {
            int idTurno = Integer.parseInt(entrada.nextLine().trim());
            servicio.cancelar(idTurno);
            System.out.println("Turno #" + idTurno + " cancelado");
        } catch (NumberFormatException e) {
            System.out.println("ERROR: id invalido");
        } catch (TurnoInvalidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void atender() {
        System.out.print("Id de turno: ");
        try {
            int idTurno = Integer.parseInt(entrada.nextLine().trim());
            servicio.atender(idTurno);
            System.out.println("Turno #" + idTurno + " marcado como atendido");
        } catch (NumberFormatException e) {
            System.out.println("ERROR: id invalido");
        } catch (TurnoInvalidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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
