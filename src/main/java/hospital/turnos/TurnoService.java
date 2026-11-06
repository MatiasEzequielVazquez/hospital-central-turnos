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
    private final ValidadorTurno validador;

    public TurnoService(TurnoRepositorio turnos, PacienteRepositorio pacientes,
                        ProfesionalRepositorio profesionales, ValidadorTurno validador) {
        this.turnos = turnos;
        this.pacientes = pacientes;
        this.profesionales = profesionales;
        this.validador = validador;
    }

    public TurnoRegistrado registrar(SolicitudTurno solicitud) {
        Paciente paciente = buscarPaciente(solicitud.dniPaciente());
        Profesional profesional = buscarProfesional(solicitud.matriculaProfesional());
        Turno turno = crearTurno(solicitud);
        validador.validar(turno);
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

    private Paciente buscarPaciente(String dni) {
        return pacientes.buscarPorDni(dni)
                .orElseThrow(() -> new TurnoInvalidoException("el paciente " + dni + " no existe"));
    }

    private Profesional buscarProfesional(String matricula) {
        return profesionales.buscarPorMatricula(matricula)
                .orElseThrow(() -> new TurnoInvalidoException("el profesional " + matricula + " no existe"));
    }

    private Turno crearTurno(SolicitudTurno solicitud) {
        try {
            LocalDate fecha = LocalDate.parse(solicitud.fecha());
            LocalTime hora = LocalTime.parse(solicitud.hora());
            return new Turno(turnos.siguienteId(), solicitud.dniPaciente(), solicitud.matriculaProfesional(),
                    fecha, hora, solicitud.urgente(), solicitud.motivo());
        } catch (DateTimeParseException e) {
            throw new TurnoInvalidoException("fecha u hora invalida");
        }
    }

    private Turno buscarTurno(int idTurno) {
        return turnos.buscarPorId(idTurno)
                .orElseThrow(() -> new TurnoInvalidoException("no existe el turno #" + idTurno));
    }
}
