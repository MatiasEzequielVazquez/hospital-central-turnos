package hospital.reportes;

import hospital.dominio.Turno;

/** Turno acompanado de los nombres necesarios para mostrarlo en un listado. */
public record TurnoDetallado(Turno turno, String nombrePaciente, String nombreProfesional) {
}
