package hospital.reportes;

import hospital.dominio.Turno;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/** Interaccion por consola de busqueda y reportes (opciones 4 y 5 del menu). */
public class ReportesConsola {

    private final Scanner entrada;
    private final BuscadorTurnos buscador;
    private final ReporteService reportes;

    public ReportesConsola(Scanner entrada, BuscadorTurnos buscador, ReporteService reportes) {
        this.entrada = entrada;
        this.buscador = buscador;
        this.reportes = reportes;
    }

    public void buscar() {
        List<TurnoDetallado> encontrados = buscador.buscar(leerFiltro());
        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron turnos");
            return;
        }
        System.out.println("Se encontraron " + encontrados.size() + " turno(s):");
        encontrados.forEach(this::imprimirFila);
    }

    public void mostrarReporte() {
        ReporteGeneral reporte = reportes.generar();
        imprimirResumenPorEstado(reporte);
        imprimirCargaPorProfesional(reporte.cargaPorProfesional());
        imprimirFacturacion(reporte.facturacion());
    }

    private FiltroTurnos leerFiltro() {
        String matricula = preguntar("Matricula (vacio = todas): ").toUpperCase();
        String estado = preguntar("Estado PENDIENTE/ATENDIDO/CANCELADO (vacio = todos): ");
        String desde = preguntar("Desde AAAA-MM-DD (vacio = sin limite): ");
        String hasta = preguntar("Hasta AAAA-MM-DD (vacio = sin limite): ");
        boolean soloUrgentes = preguntar("Solo urgentes (s/n): ").equalsIgnoreCase("s");
        return new FiltroTurnos(matricula, estado, desde, hasta, soloUrgentes);
    }

    private void imprimirFila(TurnoDetallado detalle) {
        Turno turno = detalle.turno();
        System.out.println("#" + turno.getId() + " | " + turno.getFecha() + " " + turno.getHora()
                + " | " + detalle.nombrePaciente() + " | " + detalle.nombreProfesional()
                + " | " + turno.getEstado() + (turno.isUrgente() ? " | URGENTE" : ""));
    }

    private void imprimirResumenPorEstado(ReporteGeneral reporte) {
        System.out.println("===== REPORTE GENERAL =====");
        System.out.println("Total de turnos: " + reporte.totalTurnos());
        System.out.println("Pendientes: " + reporte.pendientes());
        System.out.println("Atendidos: " + reporte.atendidos());
        System.out.println("Cancelados: " + reporte.cancelados());
    }

    private void imprimirCargaPorProfesional(List<CargaProfesional> cargas) {
        System.out.println("--- Turnos activos por profesional ---");
        for (CargaProfesional carga : cargas) {
            System.out.println(carga.profesional().nombre() + " (" + carga.profesional().especialidad() + "): "
                    + carga.turnosActivos());
        }
    }

    private void imprimirFacturacion(DetalleFacturacion facturacion) {
        System.out.println("--- Facturacion estimada (turnos atendidos) ---");
        System.out.println("Importe total: " + formatearMonto(facturacion.importeTotal()));
        System.out.println("A cargo de pacientes: " + formatearMonto(facturacion.aCargoPaciente()));
        System.out.println("A cargo de obras sociales: " + formatearMonto(facturacion.aCargoObraSocial()));
    }

    private static String formatearMonto(BigDecimal monto) {
        return "$" + String.format("%.2f", monto);
    }

    private String preguntar(String etiqueta) {
        System.out.print(etiqueta);
        return entrada.nextLine().trim();
    }
}
