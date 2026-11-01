package hospital;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datos.cargar();
        Scanner sc = new Scanner(System.in);
        TurnoManager tm = new TurnoManager();
        ReporteManager rm = new ReporteManager();
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("=== HOSPITAL CENTRAL - TURNOS ===");
            System.out.println("1) Registrar turno");
            System.out.println("2) Cancelar turno");
            System.out.println("3) Marcar turno como atendido");
            System.out.println("4) Buscar turnos");
            System.out.println("5) Reporte general");
            System.out.println("0) Salir");
            System.out.print("Opcion: ");
            if (!sc.hasNextLine()) {
                break;
            }
            String op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    System.out.print("DNI paciente: ");
                    String dni = sc.nextLine().trim();
                    System.out.print("Matricula profesional: ");
                    String mat = sc.nextLine().trim().toUpperCase();
                    System.out.print("Fecha (AAAA-MM-DD): ");
                    String fecha = sc.nextLine().trim();
                    System.out.print("Hora (HH:MM): ");
                    String hora = sc.nextLine().trim();
                    System.out.print("Urgente (s/n): ");
                    boolean urg = sc.nextLine().trim().equalsIgnoreCase("s");
                    System.out.print("Motivo: ");
                    String motivo = sc.nextLine().trim();
                    tm.agregarTurno(dni, mat, fecha, hora, urg, motivo);
                    break;
                case "2":
                    System.out.print("Id de turno: ");
                    try {
                        tm.cancelar(Integer.parseInt(sc.nextLine().trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: id invalido");
                    }
                    break;
                case "3":
                    System.out.print("Id de turno: ");
                    try {
                        tm.atender(Integer.parseInt(sc.nextLine().trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: id invalido");
                    }
                    break;
                case "4":
                    System.out.print("Matricula (vacio = todas): ");
                    String m = sc.nextLine().trim().toUpperCase();
                    System.out.print("Estado PENDIENTE/ATENDIDO/CANCELADO (vacio = todos): ");
                    String e = sc.nextLine().trim();
                    System.out.print("Desde AAAA-MM-DD (vacio = sin limite): ");
                    String d = sc.nextLine().trim();
                    System.out.print("Hasta AAAA-MM-DD (vacio = sin limite): ");
                    String h = sc.nextLine().trim();
                    System.out.print("Solo urgentes (s/n): ");
                    boolean su = sc.nextLine().trim().equalsIgnoreCase("s");
                    rm.buscar(m, e, d, h, su);
                    break;
                case "5":
                    rm.reporte();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Hasta luego");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }
}
