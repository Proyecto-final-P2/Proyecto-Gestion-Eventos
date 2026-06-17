package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * POJO que representa una Reserva.
 */
public class Reserva {
    private int R_ID;
    private LocalDate R_Fecha;
    private LocalTime R_HoraInicio;
    private LocalTime R_HoraFin;
    private double R_Monto;

    // Constructor vacío
    public Reserva() {}

    // Constructor completo
    public Reserva(int R_ID, LocalDate R_Fecha, LocalTime R_HoraInicio, LocalTime R_HoraFin, double R_Monto) {
        this.R_ID = R_ID;
        this.R_Fecha = R_Fecha;
        this.R_HoraInicio = R_HoraInicio;
        this.R_HoraFin = R_HoraFin;
        this.R_Monto = R_Monto;
    }

    // Getters
    public int getR_ID() {
        return R_ID;
    }

    public LocalDate getR_Fecha() {
        return R_Fecha;
    }

    public LocalTime getR_HoraInicio() {
        return R_HoraInicio;
    }

    public LocalTime getR_HoraFin() {
        return R_HoraFin;
    }

    public double getR_Monto() {
        return R_Monto;
    }

    // Setters
    public void setR_ID(int R_ID) {
        this.R_ID = R_ID;
    }

    public void setR_Fecha(LocalDate R_Fecha) {
        this.R_Fecha = R_Fecha;
    }

    public void setR_HoraInicio(LocalTime R_HoraInicio) {
        this.R_HoraInicio = R_HoraInicio;
    }

    public void setR_HoraFin(LocalTime R_HoraFin) {
        this.R_HoraFin = R_HoraFin;
    }

    public void setR_Monto(double R_Monto) {
        this.R_Monto = R_Monto;
    }

    @Override
    public String toString() {
        return "Reserva #" + R_ID + " [Fecha=" + R_Fecha + ", HoraInicio=" + R_HoraInicio + ", HoraFin=" + R_HoraFin + ", Monto=" + R_Monto + "]";
    }
}
