package model;

import java.time.LocalDate;

// plantilla para crear pagos
public class Pago {

    // atributos
    private int       id;
    private double    montoPagado;
    private int       reservaId;
    private String    pagador;       // nombre de quien realizó el pago (texto libre)
    private String    metodoPago;    // transferencia, débito, crédito, efectivo, PagoFácil
    private LocalDate fechaPago;     // se asigna automáticamente al insertar

    public Pago() {}

    // constructor completo
    public Pago(int id, double montoPagado, int reservaId,
                String pagador, String metodoPago, LocalDate fechaPago) {
        this.id          = id;
        this.montoPagado = montoPagado;
        this.reservaId   = reservaId;
        this.pagador     = pagador;
        this.metodoPago  = metodoPago;
        this.fechaPago   = fechaPago;
    }

    // Getters
    public int       getId()          { return id; }
    public double    getMontoPagado() { return montoPagado; }
    public int       getReservaId()   { return reservaId; }
    public String    getPagador()     { return pagador; }
    public String    getMetodoPago()  { return metodoPago; }
    public LocalDate getFechaPago()   { return fechaPago; }

    // Setters
    public void setId(int id)                 { this.id = id; }
    public void setMontoPagado(double m)      { this.montoPagado = m; }
    public void setReservaId(int r)           { this.reservaId = r; }
    public void setPagador(String p)          { this.pagador = p; }
    public void setMetodoPago(String m)       { this.metodoPago = m; }
    public void setFechaPago(LocalDate f)     { this.fechaPago = f; }

    @Override
    public String toString() {
        return "Pago #" + id + " - $" + montoPagado + " [" + metodoPago + "]";
    }
}
