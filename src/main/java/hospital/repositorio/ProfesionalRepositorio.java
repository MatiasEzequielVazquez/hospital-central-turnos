package hospital.repositorio;

import hospital.dominio.Profesional;
import java.util.List;
import java.util.Optional;

public interface ProfesionalRepositorio {

    Optional<Profesional> buscarPorMatricula(String matricula);

    List<Profesional> listarTodos();
}
