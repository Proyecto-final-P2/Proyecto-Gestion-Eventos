package model;

import java.time.LocalDate;
import java.time.LocalTime;

// plantilla para crear eventos
public class Evento {
    // atributos
    private int       id;
    private LocalDate fecha;
    private LocalTime horario;
    private String    tipo;
    private int       cantInvitados;
    private String    estado;        // "confirmado" | "pendiente de confirmacion" | "cancelado"
    private double    costoFinal;
    private int       clienteId;
    private int       salonId;

    public Evento() {} // crea un evento en blanco

    // crea evento con todos los datos
    public Evento(int id, LocalDate fecha, LocalTime horario, String tipo,
                  int cantInvitados, String estado, double costoFinal,
                  int clienteId, int salonId) {
        this.id            = id;
        this.fecha         = fecha;
        this.horario       = horario;
        this.tipo          = tipo;
        this.cantInvitados = cantInvitados;
        this.estado        = estado;
        this.costoFinal    = costoFinal;
        this.clienteId     = clienteId;
        this.salonId       = salonId;
    }

    // Getters
    public int       getId()            { return id; }
    public LocalDate getFecha()         { return fecha; }
    public LocalTime getHorario()       { return horario; }
    public String    getTipo()          { return tipo; }
    public int       getCantInvitados() { return cantInvitados; }
    public String    getEstado()        { return estado; }
    public double    getCostoFinal()    { return costoFinal; }
    public int       getClienteId()     { return clienteId; }
    public int       getSalonId()       { return salonId; }

    // Setters
    public void setId(int id)                   { this.id = id; }
    public void setFecha(LocalDate f)           { this.fecha = f; }
    public void setHorario(LocalTime h)         { this.horario = h; }
    public void setTipo(String t)               { this.tipo = t; }
    public void setCantInvitados(int c)         { this.cantInvitados = c; }
    public void setEstado(String e)             { this.estado = e; }
    public void setCostoFinal(double c)         { this.costoFinal = c; }
    public void setClienteId(int id)            { this.clienteId = id; }
    public void setSalonId(int id)              { this.salonId = id; }

    @Override //mostrar tipo y fecha del evento
    public String toString() { return tipo + " - " + fecha; }
}
