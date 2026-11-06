package hospital.turnos;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import hospital.dominio.TurnoInvalidoException;
import hospital.repositorio.PacienteRepositorio;
import hospital.repositorio.ProfesionalRepositorio;
import hospital.repositorio.TurnoRepositorio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/** Casos de uso del modulo de turnos: registrar, cancelar y atender. */
public class TurnoService {

    private final TurnoRepositorio turnos;
    private final PacienteRepositorio pacientes;
    private final ProfesionalRepositorio profesionales;

    public TurnoService(TurnoRepositorio turnos, PacienteRepositorio pacientes,
                        ProfesionalRepositorio profesionales) {
        this.turnos = turnos;
        this.pacientes = pacientes;
        this.profesionales = profesionales;
    }

    public TurnoRegistrado registrar(SolicitudTurno solicitud) {
        Paciente paciente = pacientes.buscarPorDni(solicitud.dniPaciente())
                .orElseThrow(() -> new TurnoInvalidoException(
                        "el paciente " + solicitud.dniPaciente() + " no existe"));
        Profesional profesional = profesionales.buscarPorMatricula(solicitud.matriculaProfesional())
                .orElseThrow(() -> new TurnoInvalidoException(
                        "el profesional " + solicitud.matriculaProfesional() + " no existe"));
        LocalDate fecha;
        LocalTime hora;
        try {
            fecha = LocalDate.parse(solicitud.fecha());
            hora = LocalTime.parse(solicitud.hora());
        } catch (DateTimeParseException e) {
            throw new TurnoInvalidoException("fecha u hora invalida");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new TurnoInvalidoException("no se pueden dar turnos en fechas pasadas");
        }
        if (hora.getHour() < 8 || hora.getHour() >= 20) {
            throw new TurnoInvalidoException("fuera del horario de atencion (08 a 20)");
        }
        for (Turno existente : turnos.listarTodos()) {
            if (existente.ocupaHorario(solicitud.matriculaProfesional(), fecha, hora)) {
                throw new TurnoInvalidoException("el profesional ya tiene un turno en ese horario");
            }
        }
        if (!solicitud.urgente()) {
            for (Turno existente : turnos.listarTodos()) {
                if (existente.getDniPaciente().equals(solicitud.dniPaciente()) && existente.estaPendiente()) {
                    throw new TurnoInvalidoException(
                            "el paciente ya tiene un turno pendiente (solo se permite otro si es urgente)");
                }
            }
        }
        Turno turno = new Turno(turnos.siguienteId(), solicitud.dniPaciente(),
                solicitud.matriculaProfesional(), fecha, hora, solicitud.urgente(), solicitud.motivo());
        turnos.guardar(turno);
        return new TurnoRegistrado(turno, paciente, profesional);
    }

    public Turno cancelar(int idTurno) {
        Turno turno = buscarTurno(idTurno);
        turno.cancelar();
        return turno;
    }

    public Turno atender(int idTurno) {
        Turno turno = buscarTurno(idTurno);
        turno.atender();
        return turno;
    }

    private Turno buscarTurno(int idTurno) {
        return turnos.buscarPorId(idTurno)
                .orElseThrow(() -> new TurnoInvalidoException("no existe el turno #" + idTurno));
    }
}
