package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SalonTest {

    @Test
    void testCreacionSalon() {
        // Creamos una instancia de salón para probar su estructura
        Salon salon = new Salon(1, "Av. Siempre Viva 123", "Salón Dorado", 200, 100, 20, 50000.0);
        
        // Validamos que los valores pasen correctamente al modelo
        assertEquals(1, salon.getId());
        assertEquals("Av. Siempre Viva 123", salon.getDireccion());
        assertEquals("Salón Dorado", salon.getNombre());
        assertEquals(200, salon.getCapacidad());
        assertEquals(100, salon.getCantSillas());
        assertEquals(20, salon.getCantMesas());
        assertEquals(50000.0, salon.getCosto());
    }
    
    @Test
    void testSettersSalon() {
        // Instanciamos un salón vacío y verificamos el funcionamiento de los métodos modificadores (setters)
        Salon salon = new Salon();
        salon.setNombre("Salón Plateado");
        salon.setCosto(75000.0);
        
        // Comprobamos la correcta actualización
        assertEquals("Salón Plateado", salon.getNombre());
        assertEquals(75000.0, salon.getCosto());
    }
}
