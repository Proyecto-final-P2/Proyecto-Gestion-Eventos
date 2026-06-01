package model;

public class Pago {
    private int    id;
    private double montoPagado;
    private int    reservaId;

    public Pago() {}

    public Pago(int id, double montoPagado, int reservaId) {
        this.id          = id;
        this.montoPagado = montoPagado;
        this.reservaId   = reservaId;
    }

    public int    getId()          { return id; }
    public double getMontoPagado() { return montoPagado; }
    public int    getReservaId()   { return reservaId; }

    public void setId(int id)              { this.id = id; }
    public void setMontoPagado(double m)   { this.montoPagado = m; }
    public void setReservaId(int r)        { this.reservaId = r; }

    @Override
    public String toString() { return "Pago #" + id + " - $" + montoPagado; }
}
