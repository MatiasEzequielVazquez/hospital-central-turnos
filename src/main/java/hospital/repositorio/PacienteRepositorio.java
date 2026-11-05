package hospital.repositorio;

import hospital.dominio.Paciente;
import java.util.Optional;

public interface PacienteRepositorio {

    Optional<Paciente> buscarPorDni(String dni);
}
