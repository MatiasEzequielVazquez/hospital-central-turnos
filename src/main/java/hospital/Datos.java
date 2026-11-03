package hospital;

import hospital.dominio.Paciente;
import hospital.dominio.Profesional;
import hospital.dominio.Turno;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// "Base de datos" del sistema: listas en memoria compartidas por todas las clases
public class Datos {
    public static List<Paciente> pacientes = new ArrayList<>();
    public static List<Profesional> profesionales = new ArrayList<>();
    public static List<Turno> turnos = new ArrayList<>();
    public static int ultimoId = 0;

    public static void cargar() {
        pacientes = new ArrayList<>();
        profesionales = new ArrayList<>();
        turnos = new ArrayList<>();
        ultimoId = 0;

        pacientes.add(new Paciente("30111222", "Ana Gomez", "OSDE"));
        pacientes.add(new Paciente("28999888", "Bruno Diaz", "PAMI"));
        pacientes.add(new Paciente("40555666", "Carla Ruiz", "PARTICULAR"));
        pacientes.add(new Paciente("35123456", "Diego Sosa", "SWISS"));

        profesionales.add(new Profesional("MN1001", "Dra. Laura Perez", "CLINICA"));
        profesionales.add(new Profesional("MN2002", "Dr. Martin Lopez", "CARDIOLOGIA"));
        profesionales.add(new Profesional("MN3003", "Dra. Sofia Medina", "PEDIATRIA"));

        Turno t1 = new Turno(1, "30111222", "MN1001", LocalDate.of(2026, 10, 5), LocalTime.of(9, 0), false, "Control anual");
        t1.atender();
        turnos.add(t1);
        Turno t2 = new Turno(2, "28999888", "MN2002", LocalDate.of(2026, 10, 5), LocalTime.of(10, 0), true, "Dolor de pecho");
        t2.atender();
        turnos.add(t2);
        turnos.add(new Turno(3, "40555666", "MN3003", LocalDate.of(2028, 3, 6), LocalTime.of(11, 30), false, "Vacunacion"));
        ultimoId = 3;
    }
}
