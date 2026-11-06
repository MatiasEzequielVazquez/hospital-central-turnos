package hospital;

import hospital.reportes.BuscadorTurnos;
import hospital.reportes.CalculadoraArancel;
import hospital.reportes.ReporteService;
import hospital.reportes.ReportesConsola;
import hospital.repositorio.DatosIniciales;
import hospital.repositorio.PacienteRepositorioEnMemoria;
import hospital.repositorio.ProfesionalRepositorioEnMemoria;
import hospital.repositorio.TurnoRepositorioEnMemoria;
import hospital.turnos.TurnoService;
import hospital.turnos.TurnosConsola;
import hospital.turnos.ValidadorTurno;
import java.time.Clock;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        PacienteRepositorioEnMemoria pacientes = new PacienteRepositorioEnMemoria();
        ProfesionalRepositorioEnMemoria profesionales = new ProfesionalRepositorioEnMemoria();
        TurnoRepositorioEnMemoria turnos = new TurnoRepositorioEnMemoria();
        DatosIniciales.cargarEn(pacientes, profesionales, turnos);

        Scanner sc = new Scanner(System.in);

        // modulo de turnos
        TurnosConsola turnosConsola = new TurnosConsola(sc,
                new TurnoService(turnos, pacientes, profesionales,
                        new ValidadorTurno(turnos, Clock.systemDefaultZone())));

        // modulo de busqueda y reportes
        ReportesConsola reportesConsola = new ReportesConsola(sc,
                new BuscadorTurnos(turnos, pacientes, profesionales),
                new ReporteService(turnos, pacientes, profesionales, new CalculadoraArancel()));

        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("=== HOSPITAL CENTRAL - TURNOS ===");
            System.out.println("1) Registrar turno");
            System.out.println("2) Cancelar turno");
            System.out.println("3) Marcar turno como atendido");
            System.out.println("4) Buscar turnos");
            System.out.println("5) Reporte general");
            System.out.println("0) Salir");
            System.out.print("Opcion: ");
            if (!sc.hasNextLine()) {
                break;
            }
            String op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    turnosConsola.registrar();
                    break;
                case "2":
                    turnosConsola.cancelar();
                    break;
                case "3":
                    turnosConsola.atender();
                    break;
                case "4":
                    reportesConsola.buscar();
                    break;
                case "5":
                    reportesConsola.mostrarReporte();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Hasta luego");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }
}
