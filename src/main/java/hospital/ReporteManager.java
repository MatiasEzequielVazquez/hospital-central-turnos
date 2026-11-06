package hospital;

import hospital.dominio.EstadoTurno;
import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import hospital.reportes.CalculadoraArancel;
import hospital.reportes.DetalleFacturacion;
import hospital.reportes.FiltroTurnos;
import hospital.repositorio.PacienteRepositorio;
import hospital.repositorio.ProfesionalRepositorio;
import hospital.repositorio.TurnoRepositorio;
import java.util.ArrayList;
import java.util.List;

public class ReporteManager {

    private final TurnoRepositorio turnos;
    private final PacienteRepositorio pacientes;
    private final ProfesionalRepositorio profesionales;
    private final CalculadoraArancel calculadora;

    public ReporteManager(TurnoRepositorio turnos, PacienteRepositorio pacientes,
                          ProfesionalRepositorio profesionales, CalculadoraArancel calculadora) {
        this.turnos = turnos;
        this.pacientes = pacientes;
        this.profesionales = profesionales;
        this.calculadora = calculadora;
    }

    public void buscar(FiltroTurnos filtro) {
        List<Turno> lista2 = new ArrayList<>();
        for (Turno t : turnos.listarTodos()) {
            if (filtro.acepta(t)) lista2.add(t);
        }
        if (lista2.isEmpty()) {
            System.out.println("No se encontraron turnos");
            return;
        }
        System.out.println("Se encontraron " + lista2.size() + " turno(s):");
        for (Turno t : lista2) {
            String nom = pacientes.buscarPorDni(t.getDniPaciente()).map(Paciente::nombre).orElse("");
            String nomPr = profesionales.buscarPorMatricula(t.getMatriculaProfesional()).map(Profesional::nombre).orElse("");
            System.out.println("#" + t.getId() + " | " + t.getFecha() + " " + t.getHora() + " | " + nom + " | " + nomPr
                    + " | " + t.getEstado() + (t.isUrgente() ? " | URGENTE" : ""));
        }
    }

    public void reporte() {
        int pend = 0, aten = 0, canc = 0;
        for (Turno t : turnos.listarTodos()) {
            if (t.getEstado() == EstadoTurno.PENDIENTE) pend++;
            else if (t.getEstado() == EstadoTurno.ATENDIDO) aten++;
            else if (t.getEstado() == EstadoTurno.CANCELADO) canc++;
        }
        System.out.println("===== REPORTE GENERAL =====");
        System.out.println("Total de turnos: " + turnos.listarTodos().size());
        System.out.println("Pendientes: " + pend);
        System.out.println("Atendidos: " + aten);
        System.out.println("Cancelados: " + canc);
        System.out.println("--- Turnos activos por profesional ---");
        for (Profesional pr : profesionales.listarTodos()) {
            int c = 0;
            for (Turno t : turnos.listarTodos()) {
                if (t.getMatriculaProfesional().equals(pr.matricula()) && t.estaActivo()) c++;
            }
            System.out.println(pr.nombre() + " (" + pr.especialidad() + "): " + c);
        }
        System.out.println("--- Facturacion estimada (turnos atendidos) ---");
        DetalleFacturacion facturacion = DetalleFacturacion.SIN_IMPORTES;
        for (Turno t : turnos.listarTodos()) {
            if (t.getEstado() == EstadoTurno.ATENDIDO) {
                Profesional pr = profesionales.buscarPorMatricula(t.getMatriculaProfesional()).orElseThrow();
                Paciente p = pacientes.buscarPorDni(t.getDniPaciente()).orElseThrow();
                facturacion = facturacion.sumar(calculadora.calcular(t, p, pr));
            }
        }
        System.out.println("Importe total: $" + String.format("%.2f", facturacion.importeTotal()));
        System.out.println("A cargo de pacientes: $" + String.format("%.2f", facturacion.aCargoPaciente()));
        System.out.println("A cargo de obras sociales: $" + String.format("%.2f", facturacion.aCargoObraSocial()));
    }
}
