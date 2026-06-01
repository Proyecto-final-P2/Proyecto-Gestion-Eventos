package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
    private int       id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double    monto;

    public Reserva() {}

    public Reserva(int id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, double monto) {
        this.id         = id;
        this.fecha      = fecha;
        this.horaInicio = horaInicio;
        this.horaFin    = horaFin;
        this.monto      = monto;
    }

    public int       getId()         { return id; }
    public LocalDate getFecha()      { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin()    { return horaFin; }
    public double    getMonto()      { return monto; }

    public void setId(int id)                { this.id = id; }
    public void setFecha(LocalDate f)        { this.fecha = f; }
    public void setHoraInicio(LocalTime h)   { this.horaInicio = h; }
    public void setHoraFin(LocalTime h)      { this.horaFin = h; }
    public void setMonto(double m)           { this.monto = m; }

    @Override
    public String toString() { return "Reserva #" + id + " - " + fecha; }
}
