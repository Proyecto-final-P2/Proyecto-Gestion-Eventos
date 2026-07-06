package model;

// plantilla para crear servicios
public class Servicio {
    // atributos
    private int    id;
    private String tipo;
    private String proveedor;
    private double costo;

    public Servicio() {}

    // crea servicio con todos los datos
    public Servicio(int id, String tipo, String proveedor, double costo) {
        this.id        = id;
        this.tipo      = tipo;
        this.proveedor = proveedor;
        this.costo     = costo;
    }

    // Getters
    public int    getId()        { return id; }
    public String getTipo()      { return tipo; }
    public String getProveedor() { return proveedor; }
    public double getCosto()     { return costo; }

    // Setters
    public void setId(int id)            { this.id = id; }
    public void setTipo(String t)        { this.tipo = t; }
    public void setProveedor(String p)   { this.proveedor = p; }
    public void setCosto(double c)       { this.costo = c; }

    @Override //mostrar tipo y proveedor
    public String toString() { return tipo + " - " + proveedor; }
}
