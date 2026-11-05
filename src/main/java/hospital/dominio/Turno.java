package hospital.dominio;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {

    private final int id;
    private final String dniPaciente;
    private final String matriculaProfesional;
    private final LocalDate fecha;
    private final LocalTime hora;
    private final boolean urgente;
    private final String motivo;
    private EstadoTurno estado;

    public Turno(int id, String dniPaciente, String matriculaProfesional,
                 LocalDate fecha, LocalTime hora, boolean urgente, String motivo) {
        this.id = id;
        this.dniPaciente = dniPaciente;
        this.matriculaProfesional = matriculaProfesional;
        this.fecha = fecha;
        this.hora = hora;
        this.urgente = urgente;
        this.motivo = motivo;
        this.estado = EstadoTurno.PENDIENTE;
    }

    public void cancelar() {
        if (estado == EstadoTurno.ATENDIDO) {
            throw new TurnoInvalidoException("no se puede cancelar un turno ya atendido");
        }
        if (estado == EstadoTurno.CANCELADO) {
            throw new TurnoInvalidoException("el turno ya estaba cancelado");
        }
        estado = EstadoTurno.CANCELADO;
    }

    public void atender() {
        if (estado == EstadoTurno.CANCELADO) {
            throw new TurnoInvalidoException("no se puede atender un turno cancelado");
        }
        if (estado == EstadoTurno.ATENDIDO) {
            throw new TurnoInvalidoException("el turno ya fue atendido");
        }
        estado = EstadoTurno.ATENDIDO;
    }

    public boolean estaActivo() {
        return estado != EstadoTurno.CANCELADO;
    }

    public boolean estaPendiente() {
        return estado == EstadoTurno.PENDIENTE;
    }

    public boolean ocupaHorario(String matricula, LocalDate fecha, LocalTime hora) {
        return estaActivo()
                && matriculaProfesional.equals(matricula)
                && this.fecha.equals(fecha)
                && this.hora.equals(hora);
    }

    public int getId() {
        return id;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public String getMatriculaProfesional() {
        return matriculaProfesional;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public boolean isUrgente() {
        return urgente;
    }

    public String getMotivo() {
        return motivo;
    }

    public EstadoTurno getEstado() {
        return estado;
    }
}
