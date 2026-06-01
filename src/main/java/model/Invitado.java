package model;

public class Invitado {
    private int    id;
    private int    dni;
    private String nombreApellido;
    private String email;
    private String telefono;
    private String asistencia;        // "confirmado" | "pendiente de confirmacion" | "cancelado"
    private String preferenciaMenu;   // "Celiaco" | "Vegetariano" | "Vegano" | "Clasico" | "Infantil"

    public Invitado() {}

    public Invitado(int id, int dni, String nombreApellido, String email,
                    String telefono, String asistencia, String preferenciaMenu) {
        this.id              = id;
        this.dni             = dni;
        this.nombreApellido  = nombreApellido;
        this.email           = email;
        this.telefono        = telefono;
        this.asistencia      = asistencia;
        this.preferenciaMenu = preferenciaMenu;
    }

    public int    getId()              { return id; }
    public int    getDni()             { return dni; }
    public String getNombreApellido()  { return nombreApellido; }
    public String getEmail()           { return email; }
    public String getTelefono()        { return telefono; }
    public String getAsistencia()      { return asistencia; }
    public String getPreferenciaMenu() { return preferenciaMenu; }

    public void setId(int id)                    { this.id = id; }
    public void setDni(int dni)                  { this.dni = dni; }
    public void setNombreApellido(String n)      { this.nombreApellido = n; }
    public void setEmail(String e)               { this.email = e; }
    public void setTelefono(String t)            { this.telefono = t; }
    public void setAsistencia(String a)          { this.asistencia = a; }
    public void setPreferenciaMenu(String p)     { this.preferenciaMenu = p; }

    @Override
    public String toString() { return nombreApellido; }
}
