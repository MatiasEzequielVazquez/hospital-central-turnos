package hospital.reportes;

import java.util.List;

/** Datos del reporte general, independientes de como se presenten. */
public record ReporteGeneral(int totalTurnos,
                             long pendientes,
                             long atendidos,
                             long cancelados,
                             List<CargaProfesional> cargaPorProfesional,
                             DetalleFacturacion facturacion) {
}
