package model;

// plantilla para crear clientes
public class Cliente {
    // atributos cliente
    private int    id;
    private int    dni;
    private String nombreApellido;
    private String email;
    private String telefono;

    // crea un cliente en blanco
    public Cliente() {}

    public Cliente(int id, int dni, String nombreApellido, String email, String telefono) {
        this.id            = id;
        this.dni           = dni;
        this.nombreApellido = nombreApellido;
        this.email         = email;
        this.telefono      = telefono;
    }

    // Getters obtiene los datos
    public int    getId()             { return id; }
    public int    getDni()            { return dni; }
    public String getNombreApellido() { return nombreApellido; }
    public String getEmail()          { return email; }
    public String getTelefono()       { return telefono; }

    // Setters modifica los datos
    public void setId(int id)                       { this.id = id; }
    public void setDni(int dni)                     { this.dni = dni; }
    public void setNombreApellido(String n)         { this.nombreApellido = n; }
    public void setEmail(String email)              { this.email = email; }
    public void setTelefono(String telefono)        { this.telefono = telefono; }

    @Override
    public String toString() { return nombreApellido; }
}
