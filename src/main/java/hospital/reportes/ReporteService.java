package hospital.reportes;

import hospital.dominio.EstadoTurno;
import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import hospital.repositorio.PacienteRepositorio;
import hospital.repositorio.ProfesionalRepositorio;
import hospital.repositorio.TurnoRepositorio;
import java.util.List;

/** Caso de uso: generar el reporte general (conteos, carga y facturacion). */
public class ReporteService {

    private final TurnoRepositorio turnos;
    private final PacienteRepositorio pacientes;
    private final ProfesionalRepositorio profesionales;
    private final CalculadoraArancel calculadora;

    public ReporteService(TurnoRepositorio turnos, PacienteRepositorio pacientes,
                          ProfesionalRepositorio profesionales, CalculadoraArancel calculadora) {
        this.turnos = turnos;
        this.pacientes = pacientes;
        this.profesionales = profesionales;
        this.calculadora = calculadora;
    }

    public ReporteGeneral generar() {
        List<Turno> todos = turnos.listarTodos();
        return new ReporteGeneral(
                todos.size(),
                contarPorEstado(todos, EstadoTurno.PENDIENTE),
                contarPorEstado(todos, EstadoTurno.ATENDIDO),
                contarPorEstado(todos, EstadoTurno.CANCELADO),
                calcularCargaPorProfesional(todos),
                calcularFacturacion(todos));
    }

    private long contarPorEstado(List<Turno> todos, EstadoTurno estado) {
        return todos.stream().filter(turno -> turno.getEstado() == estado).count();
    }

    private List<CargaProfesional> calcularCargaPorProfesional(List<Turno> todos) {
        return profesionales.listarTodos().stream()
                .map(profesional -> new CargaProfesional(profesional, contarActivos(todos, profesional)))
                .toList();
    }

    private long contarActivos(List<Turno> todos, Profesional profesional) {
        return todos.stream()
                .filter(turno -> turno.getMatriculaProfesional().equals(profesional.matricula()))
                .filter(Turno::estaActivo)
                .count();
    }

    private DetalleFacturacion calcularFacturacion(List<Turno> todos) {
        return todos.stream()
                .filter(turno -> turno.getEstado() == EstadoTurno.ATENDIDO)
                .map(this::facturar)
                .reduce(DetalleFacturacion.SIN_IMPORTES, DetalleFacturacion::sumar);
    }

    private DetalleFacturacion facturar(Turno turno) {
        Paciente paciente = pacientes.buscarPorDni(turno.getDniPaciente()).orElseThrow();
        Profesional profesional = profesionales.buscarPorMatricula(turno.getMatriculaProfesional()).orElseThrow();
        return calculadora.calcular(turno, paciente, profesional);
    }
}
