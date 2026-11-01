package hospital;

public class Paciente {
    public String dni;
    public String nombre;
    public String obraSocial; // OSDE, SWISS, PAMI o PARTICULAR

    public Paciente(String dni, String nombre, String obraSocial) {
        this.dni = dni;
        this.nombre = nombre;
        this.obraSocial = obraSocial;
    }
}
