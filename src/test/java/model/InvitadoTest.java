package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvitadoTest {

    @Test
    void testCreacionInvitado() {
        // Se crea un invitado para comprobar la correcta asignación de datos personales
        Invitado invitado = new Invitado(1, 40123456, "Ana Gomez", "ana@gmail.com", "1234567", "confirmado", "Vegetariano");
        
        // Se validan los atributos de la instancia
        assertEquals(1, invitado.getId());
        assertEquals(40123456, invitado.getDni());
        assertEquals("Ana Gomez", invitado.getNombreApellido());
        assertEquals("ana@gmail.com", invitado.getEmail());
        assertEquals("1234567", invitado.getTelefono());
        assertEquals("confirmado", invitado.getAsistencia());
        assertEquals("Vegetariano", invitado.getPreferenciaMenu());
    }
    
    @Test
    void testSettersInvitado() {
        // Comprobamos la modificación de estados clave del invitado
        Invitado invitado = new Invitado();
        invitado.setEventoId(10);
        invitado.setAsistencia("cancelado");
        
        // Verificamos que los cambios se hayan guardado en memoria
        assertEquals(10, invitado.getEventoId());
        assertEquals("cancelado", invitado.getAsistencia());
    }
}
