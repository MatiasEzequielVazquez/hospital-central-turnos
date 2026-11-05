package hospital.repositorio;

import hospital.dominio.Paciente;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class PacienteRepositorioEnMemoria implements PacienteRepositorio {

    private final Map<String, Paciente> pacientesPorDni = new LinkedHashMap<>();

    public void agregar(Paciente paciente) {
        pacientesPorDni.put(paciente.dni(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPorDni(String dni) {
        return Optional.ofNullable(pacientesPorDni.get(dni));
    }
}
