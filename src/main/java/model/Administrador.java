package model;

public class Administrador {
    private int id;
    private String nombreApellido;
    private String email;
    private String password;

    public Administrador() {}

    public Administrador(int id, String nombreApellido, String email, String password) {
        this.id = id;
        this.nombreApellido = nombreApellido;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getNombreApellido() { return nombreApellido; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setId(int id) { this.id = id; }
    public void setNombreApellido(String nombreApellido) { this.nombreApellido = nombreApellido; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() { return nombreApellido; }
}
