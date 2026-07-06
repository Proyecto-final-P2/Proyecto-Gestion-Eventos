package model;

import java.time.LocalDate;
import java.time.LocalTime;

// plantilla para crear eventos
public class Evento {
    // atributos
    private int       id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String    tipo;
    private int       cantInvitados;
    private String    estado;        // "confirmado" | "pendiente de confirmacion" | "cancelado"
    private double    costoTotal;    // Calculado (solo lectura)
    private double    saldoPendiente; // Calculado (solo lectura)
    private int       clienteId;
    private int       salonId;

    public Evento() {} // crea un evento en blanco

    // crea evento con todos los datos (para BD / lectura)
    public Evento(int id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String tipo,
                  int cantInvitados, String estado, double costoTotal, double saldoPendiente,
                  int clienteId, int salonId) {
        this.id            = id;
        this.fecha         = fecha;
        this.horaInicio    = horaInicio;
        this.horaFin       = horaFin;
        this.tipo          = tipo;
        this.cantInvitados = cantInvitados;
        this.estado        = estado;
        this.costoTotal    = costoTotal;
        this.saldoPendiente= saldoPendiente;
        this.clienteId     = clienteId;
        this.salonId       = salonId;
    }

    // Getters
    public int       getId()            { return id; }
    public LocalDate getFecha()         { return fecha; }
    public LocalTime getHoraInicio()    { return horaInicio; }
    public LocalTime getHoraFin()       { return horaFin; }
    public String    getTipo()          { return tipo; }
    public int       getCantInvitados() { return cantInvitados; }
    public String    getEstado()        { return estado; }
    public double    getCostoTotal()    { return costoTotal; }
    public double    getSaldoPendiente(){ return saldoPendiente; }
    public int       getClienteId()     { return clienteId; }
    public int       getSalonId()       { return salonId; }

    // Setters
    public void setId(int id)                   { this.id = id; }
    public void setFecha(LocalDate f)           { this.fecha = f; }
    public void setHoraInicio(LocalTime h)      { this.horaInicio = h; }
    public void setHoraFin(LocalTime h)         { this.horaFin = h; }
    public void setTipo(String t)               { this.tipo = t; }
    public void setCantInvitados(int c)         { this.cantInvitados = c; }
    public void setEstado(String e)             { this.estado = e; }
    public void setCostoTotal(double c)         { this.costoTotal = c; }
    public void setSaldoPendiente(double s)     { this.saldoPendiente = s; }
    public void setClienteId(int id)            { this.clienteId = id; }
    public void setSalonId(int id)              { this.salonId = id; }

    @Override //mostrar tipo y fecha del evento
    public String toString() { return tipo + " - " + fecha; }
}
