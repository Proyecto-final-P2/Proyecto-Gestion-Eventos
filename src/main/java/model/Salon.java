package model;

public class Salon {
    private int    id;
    private String direccion;
    private String nombre;
    private int    capacidad;
    private int    cantSillas;
    private int    cantMesas;
    private double costo;

    public Salon() {}

    public Salon(int id, String direccion, String nombre, int capacidad,
                 int cantSillas, int cantMesas, double costo) {
        this.id         = id;
        this.direccion  = direccion;
        this.nombre     = nombre;
        this.capacidad  = capacidad;
        this.cantSillas = cantSillas;
        this.cantMesas  = cantMesas;
        this.costo      = costo;
    }

    public int    getId()         { return id; }
    public String getDireccion()  { return direccion; }
    public String getNombre()     { return nombre; }
    public int    getCapacidad()  { return capacidad; }
    public int    getCantSillas() { return cantSillas; }
    public int    getCantMesas()  { return cantMesas; }
    public double getCosto()      { return costo; }

    public void setId(int id)               { this.id = id; }
    public void setDireccion(String d)      { this.direccion = d; }
    public void setNombre(String n)         { this.nombre = n; }
    public void setCapacidad(int c)         { this.capacidad = c; }
    public void setCantSillas(int s)        { this.cantSillas = s; }
    public void setCantMesas(int m)         { this.cantMesas = m; }
    public void setCosto(double c)          { this.costo = c; }

    @Override
    public String toString() { return nombre + " (cap. " + capacidad + ")"; }
}
