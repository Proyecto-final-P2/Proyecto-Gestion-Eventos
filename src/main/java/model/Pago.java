package model;

// plantilla para crear pagos
public class Pago {
    // atributos
    private int    id;
    private double montoPagado;
    private int    reservaId;

    public Pago() {} // crea un pago en blanco

    // crea pago con todos los datos
    public Pago(int id, double montoPagado, int reservaId) {
        this.id          = id;
        this.montoPagado = montoPagado;
        this.reservaId   = reservaId;
    }

    // Getters
    public int    getId()          { return id; }
    public double getMontoPagado() { return montoPagado; }
    public int    getReservaId()   { return reservaId; }

    // Setters
    public void setId(int id)              { this.id = id; }
    public void setMontoPagado(double m)   { this.montoPagado = m; }
    public void setReservaId(int r)        { this.reservaId = r; }

    @Override //mostrar solo el id y el monto pagado
    public String toString() { return "Pago #" + id + " - $" + montoPagado; }
}
