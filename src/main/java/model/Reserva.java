package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * POJO que representa una Reserva, vinculada a un Cliente y a un Salón.
 */
public class Reserva {
    private int R_ID;
    private LocalDate R_Fecha;
    private LocalTime R_HoraInicio;
    private LocalTime R_HoraFin;
    private double R_Monto;
    private int R_ClienteID;
    private int R_SalonID;

    // Campos solo para visualización (no persisten en la BD)
    private String clienteNombre;
    private String salonNombre;

    // Constructor vacío
    public Reserva() {}

    // Constructor completo original (para mantener compatibilidad si es necesario)
    public Reserva(int R_ID, LocalDate R_Fecha, LocalTime R_HoraInicio, LocalTime R_HoraFin, double R_Monto) {
        this.R_ID = R_ID;
        this.R_Fecha = R_Fecha;
        this.R_HoraInicio = R_HoraInicio;
        this.R_HoraFin = R_HoraFin;
        this.R_Monto = R_Monto;
    }

    // Constructor completo actualizado
    public Reserva(int R_ID, LocalDate R_Fecha, LocalTime R_HoraInicio, LocalTime R_HoraFin, double R_Monto, 
                   int R_ClienteID, int R_SalonID, String clienteNombre, String salonNombre) {
        this.R_ID = R_ID;
        this.R_Fecha = R_Fecha;
        this.R_HoraInicio = R_HoraInicio;
        this.R_HoraFin = R_HoraFin;
        this.R_Monto = R_Monto;
        this.R_ClienteID = R_ClienteID;
        this.R_SalonID = R_SalonID;
        this.clienteNombre = clienteNombre;
        this.salonNombre = salonNombre;
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

    public int getR_ClienteID() {
        return R_ClienteID;
    }

    public int getR_SalonID() {
        return R_SalonID;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getSalonNombre() {
        return salonNombre;
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

    public void setR_ClienteID(int R_ClienteID) {
        this.R_ClienteID = R_ClienteID;
    }

    public void setR_SalonID(int R_SalonID) {
        this.R_SalonID = R_SalonID;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public void setSalonNombre(String salonNombre) {
        this.salonNombre = salonNombre;
    }

    @Override
    public String toString() {
        return "Reserva #" + R_ID + " [Fecha=" + R_Fecha + ", HoraInicio=" + R_HoraInicio + ", HoraFin=" + R_HoraFin + 
               ", Monto=" + R_Monto + ", ClienteID=" + R_ClienteID + ", SalonID=" + R_SalonID + 
               ", Cliente=" + clienteNombre + ", Salon=" + salonNombre + "]";
    }
}
