package hospital;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import hospital.dominio.TurnoInvalidoException;
import hospital.repositorio.PacienteRepositorio;
import hospital.repositorio.ProfesionalRepositorio;
import hospital.repositorio.TurnoRepositorio;
import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoManager {

    private final TurnoRepositorio turnos;
    private final PacienteRepositorio pacientes;
    private final ProfesionalRepositorio profesionales;

    public TurnoManager(TurnoRepositorio turnos, PacienteRepositorio pacientes,
                        ProfesionalRepositorio profesionales) {
        this.turnos = turnos;
        this.pacientes = pacientes;
        this.profesionales = profesionales;
    }

    // registra un turno nuevo
    public boolean agregarTurno(String dni, String mat, String fecha, String hora, boolean urg, String motivo) {
        // validar paciente
        Paciente p = pacientes.buscarPorDni(dni).orElse(null);
        if (p == null) {
            System.out.println("ERROR: el paciente " + dni + " no existe");
            return false;
        }
        // validar profesional
        Profesional pr = profesionales.buscarPorMatricula(mat).orElse(null);
        if (pr == null) {
            System.out.println("ERROR: el profesional " + mat + " no existe");
            return false;
        }
        // validar fecha y hora
        LocalDate f;
        LocalTime hr;
        try {
            f = LocalDate.parse(fecha);
            hr = LocalTime.parse(hora);
        } catch (Exception e) {
            System.out.println("ERROR: fecha u hora invalida");
            return false;
        }
        if (f.isBefore(LocalDate.now())) {
            System.out.println("ERROR: no se pueden dar turnos en fechas pasadas");
            return false;
        }
        int h = hr.getHour();
        if (h < 8 || h >= 20) {
            System.out.println("ERROR: fuera del horario de atencion (08 a 20)");
            return false;
        }
        // superposicion
        for (Turno t : turnos.listarTodos()) {
            if (t.ocupaHorario(mat, f, hr)) {
                System.out.println("ERROR: el profesional ya tiene un turno en ese horario");
                return false;
            }
        }
        // un paciente no puede tener dos turnos pendientes salvo urgencia
        if (urg == false) {
            for (Turno t : turnos.listarTodos()) {
                if (t.getDniPaciente().equals(dni) && t.estaPendiente()) {
                    System.out.println("ERROR: el paciente ya tiene un turno pendiente (solo se permite otro si es urgente)");
                    return false;
                }
            }
        }
        Turno t = new Turno(turnos.siguienteId(), dni, mat, f, hr, urg, motivo);
        turnos.guardar(t);
        System.out.println("Turno #" + t.getId() + " registrado para " + p.nombre() + " con " + pr.nombre()
                + " el " + t.getFecha() + " " + t.getHora() + (urg ? " [URGENTE]" : ""));
        return true;
    }

    public void cancelar(int id) {
        Turno x = turnos.buscarPorId(id).orElse(null);
        if (x == null) {
            System.out.println("ERROR: no existe el turno #" + id);
            return;
        }
        try {
            x.cancelar();
            System.out.println("Turno #" + id + " cancelado");
        } catch (TurnoInvalidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void atender(int id) {
        Turno x = turnos.buscarPorId(id).orElse(null);
        if (x == null) {
            System.out.println("ERROR: no existe el turno #" + id);
            return;
        }
        try {
            x.atender();
            System.out.println("Turno #" + id + " marcado como atendido");
        } catch (TurnoInvalidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
