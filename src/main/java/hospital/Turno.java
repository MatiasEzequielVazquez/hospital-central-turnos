package hospital;

public class Turno {
    public int id;
    public String dni;
    public String mat;
    public String fecha;   // formato AAAA-MM-DD
    public String hora;    // formato HH:MM
    public EstadoTurno estado;
    public boolean urg;
    public String motivo;
}
