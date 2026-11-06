package hospital.reportes;

import hospital.dominio.Profesional;

/** Cantidad de turnos activos (no cancelados) asignados a un profesional. */
public record CargaProfesional(Profesional profesional, long turnosActivos) {
}
