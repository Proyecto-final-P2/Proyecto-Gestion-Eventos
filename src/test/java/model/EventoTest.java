package model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class EventoTest {

    @Test
    void testCreacionEvento() {
        LocalDate fecha = LocalDate.of(2027, 5, 20);
        LocalTime horaInicio = LocalTime.of(20, 0);
        LocalTime horaFin = LocalTime.of(4, 0);

        Evento evento = new Evento(
            1, fecha, horaInicio, horaFin, "Casamiento", 
            150, "confirmado", 100000.0, 50000.0, 
            10, 2
        );

        assertEquals("Casamiento", evento.getTipo());
        assertEquals("confirmado", evento.getEstado());
        assertEquals(150, evento.getCantInvitados());
        assertEquals(100000.0, evento.getCostoTotal());
        assertEquals(50000.0, evento.getSaldoPendiente());
    }
}
