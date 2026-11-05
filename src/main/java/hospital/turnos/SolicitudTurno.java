package hospital.turnos;

/** Datos ingresados para pedir un turno (Introduce Parameter Object). */
public record SolicitudTurno(String dniPaciente,
                             String matriculaProfesional,
                             String fecha,
                             String hora,
                             boolean urgente,
                             String motivo) {
}
