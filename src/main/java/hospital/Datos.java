package hospital;

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

        Turno t1 = new Turno();
        t1.id = 1; t1.dni = "30111222"; t1.mat = "MN1001"; t1.fecha = "2026-10-05"; t1.hora = "09:00";
        t1.estado = EstadoTurno.ATENDIDO; t1.urg = false; t1.motivo = "Control anual";
        turnos.add(t1);
        Turno t2 = new Turno();
        t2.id = 2; t2.dni = "28999888"; t2.mat = "MN2002"; t2.fecha = "2026-10-05"; t2.hora = "10:00";
        t2.estado = EstadoTurno.ATENDIDO; t2.urg = true; t2.motivo = "Dolor de pecho";
        turnos.add(t2);
        Turno t3 = new Turno();
        t3.id = 3; t3.dni = "40555666"; t3.mat = "MN3003"; t3.fecha = "2028-03-06"; t3.hora = "11:30";
        t3.estado = EstadoTurno.PENDIENTE; t3.urg = false; t3.motivo = "Vacunacion";
        turnos.add(t3);
        ultimoId = 3;
    }
}
