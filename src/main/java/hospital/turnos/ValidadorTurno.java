package hospital.turnos;

import hospital.dominio.Turno;
import hospital.dominio.TurnoInvalidoException;
import hospital.repositorio.TurnoRepositorio;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

/** Reglas de negocio que debe cumplir un turno nuevo antes de registrarse. */
public class ValidadorTurno {

    private static final LocalTime APERTURA = LocalTime.of(8, 0);
    private static final LocalTime CIERRE = LocalTime.of(20, 0);

    private final TurnoRepositorio turnos;
    private final Clock reloj;

    public ValidadorTurno(TurnoRepositorio turnos, Clock reloj) {
        this.turnos = turnos;
        this.reloj = reloj;
    }

    public void validar(Turno nuevo) {
        validarFechaNoPasada(nuevo.getFecha());
        validarHorarioDeAtencion(nuevo.getHora());
        validarDisponibilidadDelProfesional(nuevo);
        validarSinOtroTurnoPendiente(nuevo);
    }

    private void validarFechaNoPasada(LocalDate fecha) {
        if (fecha.isBefore(LocalDate.now(reloj))) {
            throw new TurnoInvalidoException("no se pueden dar turnos en fechas pasadas");
        }
    }

    private void validarHorarioDeAtencion(LocalTime hora) {
        if (hora.isBefore(APERTURA) || !hora.isBefore(CIERRE)) {
            throw new TurnoInvalidoException("fuera del horario de atencion (08 a 20)");
        }
    }

    private void validarDisponibilidadDelProfesional(Turno nuevo) {
        boolean ocupado = turnos.listarTodos().stream()
                .anyMatch(existente -> existente.ocupaHorario(
                        nuevo.getMatriculaProfesional(), nuevo.getFecha(), nuevo.getHora()));
        if (ocupado) {
            throw new TurnoInvalidoException("el profesional ya tiene un turno en ese horario");
        }
    }

    private void validarSinOtroTurnoPendiente(Turno nuevo) {
        if (nuevo.isUrgente()) {
            return;
        }
        boolean tienePendiente = turnos.listarTodos().stream()
                .anyMatch(existente -> existente.getDniPaciente().equals(nuevo.getDniPaciente())
                        && existente.estaPendiente());
        if (tienePendiente) {
            throw new TurnoInvalidoException(
                    "el paciente ya tiene un turno pendiente (solo se permite otro si es urgente)");
        }
    }
}
