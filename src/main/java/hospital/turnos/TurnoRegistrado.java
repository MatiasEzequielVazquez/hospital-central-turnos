package hospital.turnos;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;

/** Resultado de un alta exitosa: el turno creado junto con sus participantes. */
public record TurnoRegistrado(Turno turno, Paciente paciente, Profesional profesional) {
}
