package hospital;

public class Profesional {
    public String matricula;
    public String nombre;
    public String especialidad; // CLINICA, CARDIOLOGIA o PEDIATRIA

    public Profesional(String matricula, String nombre, String especialidad) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }
}
