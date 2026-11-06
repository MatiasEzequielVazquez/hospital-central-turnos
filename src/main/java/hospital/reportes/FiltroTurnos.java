package hospital.reportes;

import hospital.dominio.Turno;

/**
 * Criterios de busqueda de turnos (Introduce Parameter Object).
 * Un criterio vacio o null significa "sin filtro".
 */
public record FiltroTurnos(String matricula,
                           String estado,
                           String desde,
                           String hasta,
                           boolean soloUrgentes) {

    public boolean acepta(Turno turno) {
        return coincideMatricula(turno)
                && coincideEstado(turno)
                && estaDentroDelRango(turno)
                && (!soloUrgentes || turno.isUrgente());
    }

    private boolean coincideMatricula(Turno turno) {
        return sinFiltro(matricula) || turno.getMatriculaProfesional().equals(matricula);
    }

    private boolean coincideEstado(Turno turno) {
        return sinFiltro(estado) || turno.getEstado().name().equals(estado.toUpperCase());
    }

    private boolean estaDentroDelRango(Turno turno) {
        String fecha = turno.getFecha().toString();
        boolean despuesDelInicio = sinFiltro(desde) || fecha.compareTo(desde) >= 0;
        boolean antesDelFin = sinFiltro(hasta) || fecha.compareTo(hasta) <= 0;
        return despuesDelInicio && antesDelFin;
    }

    private static boolean sinFiltro(String criterio) {
        return criterio == null || criterio.isEmpty();
    }
}
