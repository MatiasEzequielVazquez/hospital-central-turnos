package hospital.reportes;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import hospital.repositorio.PacienteRepositorio;
import hospital.repositorio.ProfesionalRepositorio;
import hospital.repositorio.TurnoRepositorio;
import java.util.List;

/** Caso de uso: buscar turnos segun un filtro. */
public class BuscadorTurnos {

    private final TurnoRepositorio turnos;
    private final PacienteRepositorio pacientes;
    private final ProfesionalRepositorio profesionales;

    public BuscadorTurnos(TurnoRepositorio turnos, PacienteRepositorio pacientes,
                          ProfesionalRepositorio profesionales) {
        this.turnos = turnos;
        this.pacientes = pacientes;
        this.profesionales = profesionales;
    }

    public List<TurnoDetallado> buscar(FiltroTurnos filtro) {
        return turnos.listarTodos().stream()
                .filter(filtro::acepta)
                .map(this::detallar)
                .toList();
    }

    private TurnoDetallado detallar(Turno turno) {
        String nombrePaciente = pacientes.buscarPorDni(turno.getDniPaciente())
                .map(Paciente::nombre).orElse("");
        String nombreProfesional = profesionales.buscarPorMatricula(turno.getMatriculaProfesional())
                .map(Profesional::nombre).orElse("");
        return new TurnoDetallado(turno, nombrePaciente, nombreProfesional);
    }
}
