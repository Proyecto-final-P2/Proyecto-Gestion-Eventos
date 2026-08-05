package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testCreacionCliente() {
        // Datos iniciales para la prueba
        int id = 1;
        int dni = 12345678;
        String nombre = "Juan Pérez";
        String email = "juan@example.com";
        String telefono = "123456789";

        // Instanciación del cliente con los parámetros
        Cliente cliente = new Cliente(id, dni, nombre, email, telefono);

        // Verificamos que los atributos se hayan asignado correctamente
        assertEquals(id, cliente.getId(), "El ID debería ser 1");
        assertEquals(dni, cliente.getDni(), "El DNI debería coincidir");
        assertEquals(nombre, cliente.getNombreApellido(), "El nombre debería coincidir");
        assertEquals(email, cliente.getEmail(), "El email debería coincidir");
        assertEquals(telefono, cliente.getTelefono(), "El teléfono debería coincidir");
    }

    @Test
    void testClienteEmailVacio() {
        // Comprobación de que el sistema tolera clientes sin email
        Cliente cliente = new Cliente(2, 87654321, "María López", "", "987654321");
        
        assertTrue(cliente.getEmail().isEmpty(), "El email debería estar vacío sin tirar errores");
    }
}
