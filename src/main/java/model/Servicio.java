package model;

// plantilla para crear servicios
public class Servicio {
    // atributos
    private int    id;
    private String tipo;
    private String proveedor;
    private double costo;
    private int    cantidad;
    private String estado;  // "confirmado" | "pendiente de confirmacion" | "cancelado"

    public Servicio() {}

    // crea servicio con todos los datos
    public Servicio(int id, String tipo, String proveedor, double costo, int cantidad, String estado) {
        this.id        = id;
        this.tipo      = tipo;
        this.proveedor = proveedor;
        this.costo     = costo;
        this.cantidad  = cantidad;
        this.estado    = estado;
    }

    // Getters
    public int    getId()        { return id; }
    public String getTipo()      { return tipo; }
    public String getProveedor() { return proveedor; }
    public double getCosto()     { return costo; }
    public int    getCantidad()  { return cantidad; }
    public String getEstado()    { return estado; }

    // Setters
    public void setId(int id)            { this.id = id; }
    public void setTipo(String t)        { this.tipo = t; }
    public void setProveedor(String p)   { this.proveedor = p; }
    public void setCosto(double c)       { this.costo = c; }
    public void setCantidad(int c)       { this.cantidad = c; }
    public void setEstado(String e)      { this.estado = e; }

    @Override //mostrar tipo y proveedor
    public String toString() { return tipo + " - " + proveedor; }
}
