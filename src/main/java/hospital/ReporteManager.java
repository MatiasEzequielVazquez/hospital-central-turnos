package hospital;

import java.util.ArrayList;
import java.util.List;

public class ReporteManager {

    // busca turnos; los parametros vacios o null significan "sin filtro"
    public void buscar(String mat, String estado, String desde, String hasta, boolean soloUrg) {
        List<Turno> lista2 = new ArrayList<>();
        for (Turno t : Datos.turnos) {
            boolean ok = true;
            if (mat != null && !mat.isEmpty() && !t.mat.equals(mat)) ok = false;
            if (estado != null && !estado.isEmpty() && !t.estado.name().equals(estado.toUpperCase())) ok = false;
            if (desde != null && !desde.isEmpty() && t.fecha.compareTo(desde) < 0) ok = false;
            if (hasta != null && !hasta.isEmpty() && t.fecha.compareTo(hasta) > 0) ok = false;
            if (soloUrg && !t.urg) ok = false;
            if (ok) lista2.add(t);
        }
        if (lista2.isEmpty()) {
            System.out.println("No se encontraron turnos");
            return;
        }
        System.out.println("Se encontraron " + lista2.size() + " turno(s):");
        for (Turno t : lista2) {
            String nom = "";
            for (Paciente p : Datos.pacientes) {
                if (p.dni.equals(t.dni)) nom = p.nombre;
            }
            String nomPr = "";
            for (Profesional pr : Datos.profesionales) {
                if (pr.matricula.equals(t.mat)) nomPr = pr.nombre;
            }
            System.out.println("#" + t.id + " | " + t.fecha + " " + t.hora + " | " + nom + " | " + nomPr
                    + " | " + t.estado + (t.urg ? " | URGENTE" : ""));
        }
    }

    public void reporte() {
        int pend = 0, aten = 0, canc = 0;
        for (Turno t : Datos.turnos) {
            if (t.estado == EstadoTurno.PENDIENTE) pend++;
            else if (t.estado == EstadoTurno.ATENDIDO) aten++;
            else if (t.estado == EstadoTurno.CANCELADO) canc++;
        }
        System.out.println("===== REPORTE GENERAL =====");
        System.out.println("Total de turnos: " + Datos.turnos.size());
        System.out.println("Pendientes: " + pend);
        System.out.println("Atendidos: " + aten);
        System.out.println("Cancelados: " + canc);
        System.out.println("--- Turnos activos por profesional ---");
        for (Profesional pr : Datos.profesionales) {
            int c = 0;
            for (Turno t : Datos.turnos) {
                if (t.mat.equals(pr.matricula) && t.estado != EstadoTurno.CANCELADO) c++;
            }
            System.out.println(pr.nombre + " (" + pr.especialidad + "): " + c);
        }
        System.out.println("--- Facturacion estimada (turnos atendidos) ---");
        double total = 0, pac = 0, os = 0;
        for (Turno t : Datos.turnos) {
            if (t.estado == EstadoTurno.ATENDIDO) {
                Profesional pr = null;
                for (Profesional x : Datos.profesionales) {
                    if (x.matricula.equals(t.mat)) pr = x;
                }
                Paciente p = null;
                for (Paciente x : Datos.pacientes) {
                    if (x.dni.equals(t.dni)) p = x;
                }
                double precio = 0;
                if (pr.especialidad.equals("CLINICA")) precio = 10000;
                else if (pr.especialidad.equals("CARDIOLOGIA")) precio = 15000;
                else if (pr.especialidad.equals("PEDIATRIA")) precio = 12000;
                if (t.urg) precio = precio * 1.2;
                double cos = 0;
                switch (p.obraSocial) {
                    case "OSDE":
                        cos = precio * 0.1;
                        break;
                    case "SWISS":
                        cos = precio * 0.1;
                        break;
                    case "PAMI":
                        cos = 0;
                        break;
                    default:
                        cos = precio;
                }
                total += precio;
                pac += cos;
                os += precio - cos;
            }
        }
        System.out.println("Importe total: $" + String.format("%.2f", total));
        System.out.println("A cargo de pacientes: $" + String.format("%.2f", pac));
        System.out.println("A cargo de obras sociales: $" + String.format("%.2f", os));
    }
}
