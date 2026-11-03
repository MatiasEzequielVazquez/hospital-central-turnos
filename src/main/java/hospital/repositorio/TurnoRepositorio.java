package hospital.repositorio;

import hospital.dominio.Turno;
import java.util.List;
import java.util.Optional;

public interface TurnoRepositorio {

    int siguienteId();

    void guardar(Turno turno);

    Optional<Turno> buscarPorId(int id);

    List<Turno> listarTodos();
}
