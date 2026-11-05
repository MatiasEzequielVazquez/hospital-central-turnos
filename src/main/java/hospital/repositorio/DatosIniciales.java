package hospital.repositorio;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import java.time.LocalDate;
import java.time.LocalTime;

/** Datos sinteticos de ejemplo (no corresponden a personas reales). */
public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static void cargarEn(PacienteRepositorioEnMemoria pacientes,
                                ProfesionalRepositorioEnMemoria profesionales,
                                TurnoRepositorioEnMemoria turnos) {
        pacientes.agregar(new Paciente("30111222", "Ana Gomez", "OSDE"));
        pacientes.agregar(new Paciente("28999888", "Bruno Diaz", "PAMI"));
        pacientes.agregar(new Paciente("40555666", "Carla Ruiz", "PARTICULAR"));
        pacientes.agregar(new Paciente("35123456", "Diego Sosa", "SWISS"));

        profesionales.agregar(new Profesional("MN1001", "Dra. Laura Perez", "CLINICA"));
        profesionales.agregar(new Profesional("MN2002", "Dr. Martin Lopez", "CARDIOLOGIA"));
        profesionales.agregar(new Profesional("MN3003", "Dra. Sofia Medina", "PEDIATRIA"));

        Turno controlAnual = new Turno(1, "30111222", "MN1001",
                LocalDate.of(2026, 10, 5), LocalTime.of(9, 0), false, "Control anual");
        controlAnual.atender();
        turnos.guardar(controlAnual);

        Turno dolorDePecho = new Turno(2, "28999888", "MN2002",
                LocalDate.of(2026, 10, 5), LocalTime.of(10, 0), true, "Dolor de pecho");
        dolorDePecho.atender();
        turnos.guardar(dolorDePecho);

        turnos.guardar(new Turno(3, "40555666", "MN3003",
                LocalDate.of(2028, 3, 6), LocalTime.of(11, 30), false, "Vacunacion"));
    }
}
