package hospital;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoManager {

    // registra un turno nuevo
    public boolean agregarTurno(String dni, String mat, String fecha, String hora, boolean urg, String motivo) {
        // validar paciente
        Paciente p = null;
        for (int i = 0; i < Datos.pacientes.size(); i++) {
            if (Datos.pacientes.get(i).dni.equals(dni)) {
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
            if (Datos.profesionales.get(i).matricula.equals(mat)) {
                pr = Datos.profesionales.get(i);
            }
        }
        if (pr == null) {
            System.out.println("ERROR: el profesional " + mat + " no existe");
            return false;
        }
        // validar fecha y hora
        LocalDate f;
        try {
            f = LocalDate.parse(fecha);
            LocalTime.parse(hora);
        } catch (Exception e) {
            System.out.println("ERROR: fecha u hora invalida");
            return false;
        }
        if (f.isBefore(LocalDate.now())) {
            System.out.println("ERROR: no se pueden dar turnos en fechas pasadas");
            return false;
        }
        int h = Integer.parseInt(hora.substring(0, 2));
        if (h < 8 || h >= 20) {
            System.out.println("ERROR: fuera del horario de atencion (08 a 20)");
            return false;
        }
        // superposicion
        for (Turno t : Datos.turnos) {
            if (t.mat.equals(mat) && t.fecha.equals(fecha) && t.hora.equals(hora) && t.estado != EstadoTurno.CANCELADO) {
                System.out.println("ERROR: el profesional ya tiene un turno en ese horario");
                return false;
            }
        }
        // un paciente no puede tener dos turnos pendientes salvo urgencia
        if (urg == false) {
            for (Turno t : Datos.turnos) {
                if (t.dni.equals(dni) && t.estado == EstadoTurno.PENDIENTE) {
                    System.out.println("ERROR: el paciente ya tiene un turno pendiente (solo se permite otro si es urgente)");
                    return false;
                }
            }
        }
        Datos.ultimoId++;
        Turno t = new Turno();
        t.id = Datos.ultimoId;
        t.dni = dni;
        t.mat = mat;
        t.fecha = fecha;
        t.hora = hora;
        t.estado = EstadoTurno.PENDIENTE;
        t.urg = urg;
        t.motivo = motivo;
        Datos.turnos.add(t);
        System.out.println("Turno #" + t.id + " registrado para " + p.nombre + " con " + pr.nombre
                + " el " + fecha + " " + hora + (urg ? " [URGENTE]" : ""));
        return true;
    }

    public void cancelar(int id) {
        Turno x = null;
        for (Turno t : Datos.turnos) {
            if (t.id == id) {
                x = t;
            }
        }
        if (x == null) {
            System.out.println("ERROR: no existe el turno #" + id);
            return;
        }
        if (x.estado == EstadoTurno.ATENDIDO) {
            System.out.println("ERROR: no se puede cancelar un turno ya atendido");
            return;
        }
        if (x.estado == EstadoTurno.CANCELADO) {
            System.out.println("ERROR: el turno ya estaba cancelado");
            return;
        }
        x.estado = EstadoTurno.CANCELADO;
        System.out.println("Turno #" + id + " cancelado");
    }

    public void atender(int id) {
        Turno x = null;
        for (Turno t : Datos.turnos) {
            if (t.id == id) {
                x = t;
            }
        }
        if (x == null) {
            System.out.println("ERROR: no existe el turno #" + id);
            return;
        }
        if (x.estado == EstadoTurno.CANCELADO) {
            System.out.println("ERROR: no se puede atender un turno cancelado");
            return;
        }
        if (x.estado == EstadoTurno.ATENDIDO) {
            System.out.println("ERROR: el turno ya fue atendido");
            return;
        }
        x.estado = EstadoTurno.ATENDIDO;
        System.out.println("Turno #" + id + " marcado como atendido");
    }
}
