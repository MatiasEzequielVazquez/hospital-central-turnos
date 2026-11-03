package hospital.repositorio;

import hospital.dominio.Turno;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TurnoRepositorioEnMemoria implements TurnoRepositorio {

    private final Map<Integer, Turno> turnosPorId = new LinkedHashMap<>();
    private int ultimoId = 0;

    @Override
    public int siguienteId() {
        return ultimoId + 1;
    }

    @Override
    public void guardar(Turno turno) {
        turnosPorId.put(turno.getId(), turno);
        ultimoId = Math.max(ultimoId, turno.getId());
    }

    @Override
    public Optional<Turno> buscarPorId(int id) {
        return Optional.ofNullable(turnosPorId.get(id));
    }

    @Override
    public List<Turno> listarTodos() {
        return new ArrayList<>(turnosPorId.values());
    }
}
