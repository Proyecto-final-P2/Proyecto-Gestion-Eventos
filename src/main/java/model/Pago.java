package model;

import java.time.LocalDate;

// plantilla para crear pagos
public class Pago {
    // atributos
    private int id;
    private double montoPagado;
    private int eventoId;
    private String pagador;       // puede ser null
    private String metodoPago;    // "Efectivo", "Transferencia", "Otro" (not null)
    private LocalDate fechaPago;  // automático (not null)

    public Pago() {
        this.metodoPago = "Efectivo";
        this.fechaPago = LocalDate.now();
    } // crea un pago en blanco

    // crea pago con los datos basicos legacy
    public Pago(int id, double montoPagado, int eventoId) {
        this.id = id;
        this.montoPagado = montoPagado;
        this.eventoId = eventoId;
        this.pagador = null;
        this.metodoPago = "Efectivo";
        this.fechaPago = LocalDate.now();
    }

    // crea pago con todos los datos nuevos
    public Pago(int id, double montoPagado, int eventoId, String pagador, String metodoPago, LocalDate fechaPago) {
        this.id = id;
        this.montoPagado = montoPagado;
        this.eventoId = eventoId;
        this.pagador = pagador;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
    }

    // Getters
    public int getId() { return id; }
    public double getMontoPagado() { return montoPagado; }
    public int getEventoId() { return eventoId; }
    public String getPagador() { return pagador; }
    public String getMetodoPago() { return metodoPago; }
    public LocalDate getFechaPago() { return fechaPago; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setMontoPagado(double m) { this.montoPagado = m; }
    public void setEventoId(int r) { this.eventoId = r; }
    public void setPagador(String pagador) { this.pagador = pagador; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    @Override
    public String toString() {
        return "Pago #" + id + " - $" + montoPagado + " (" + metodoPago + ") - " + fechaPago;
    }
}
