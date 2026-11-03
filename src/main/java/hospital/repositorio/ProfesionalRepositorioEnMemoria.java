package hospital.repositorio;

import hospital.dominio.Profesional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProfesionalRepositorioEnMemoria implements ProfesionalRepositorio {

    private final Map<String, Profesional> profesionalesPorMatricula = new LinkedHashMap<>();

    public void agregar(Profesional profesional) {
        profesionalesPorMatricula.put(profesional.matricula(), profesional);
    }

    @Override
    public Optional<Profesional> buscarPorMatricula(String matricula) {
        return Optional.ofNullable(profesionalesPorMatricula.get(matricula));
    }

    @Override
    public List<Profesional> listarTodos() {
        return new ArrayList<>(profesionalesPorMatricula.values());
    }
}
