package hospital;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import hospital.dominio.TurnoInvalidoException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoManager {

    // registra un turno nuevo
    public boolean agregarTurno(String dni, String mat, String fecha, String hora, boolean urg, String motivo) {
        // validar paciente
        Paciente p = null;
        for (int i = 0; i < Datos.pacientes.size(); i++) {
            if (Datos.pacientes.get(i).dni().equals(dni)) {
                p = Datos.pacientes.get(i);
            }
        }
        if (p == null) {
            System.out.println("ERROR: el paciente " + dni + " no existe");
            return false;
        }
        // validar profesional
        Profesional pr = null;
        for (int i = 0; i < Datos.profesionales.size(); i++) {
            if (Datos.profesionales.get(i).matricula().equals(mat)) {
                pr = Datos.profesionales.get(i);
            }
        }
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
        for (Turno t : Datos.turnos) {
            if (t.ocupaHorario(mat, f, hr)) {
                System.out.println("ERROR: el profesional ya tiene un turno en ese horario");
                return false;
            }
        }
        // un paciente no puede tener dos turnos pendientes salvo urgencia
        if (urg == false) {
            for (Turno t : Datos.turnos) {
                if (t.getDniPaciente().equals(dni) && t.estaPendiente()) {
                    System.out.println("ERROR: el paciente ya tiene un turno pendiente (solo se permite otro si es urgente)");
                    return false;
                }
            }
        }
        Datos.ultimoId++;
        Turno t = new Turno(Datos.ultimoId, dni, mat, f, hr, urg, motivo);
        Datos.turnos.add(t);
        System.out.println("Turno #" + t.getId() + " registrado para " + p.nombre() + " con " + pr.nombre()
                + " el " + t.getFecha() + " " + t.getHora() + (urg ? " [URGENTE]" : ""));
        return true;
    }

    public void cancelar(int id) {
        Turno x = null;
        for (Turno t : Datos.turnos) {
            if (t.getId() == id) {
                x = t;
            }
        }
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
        Turno x = null;
        for (Turno t : Datos.turnos) {
            if (t.getId() == id) {
                x = t;
            }
        }
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
