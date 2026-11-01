package hospital;

public class Turno {
    public int id;
    public String dni;
    public String mat;
    public String fecha;   // formato AAAA-MM-DD
    public String hora;    // formato HH:MM
    public String estado;  // PENDIENTE, ATENDIDO o CANCELADO
    public boolean urg;
    public String motivo;
}
