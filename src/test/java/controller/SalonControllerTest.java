package controller;

import model.Salon;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SalonControllerTest {

    @Test
    void testListarSalones() {
        SalonController controller = new SalonController();
        
        // Solicitamos el listado completo de salones al controlador
        List<Salon> salones = controller.listar();
        assertNotNull(salones);
    }
}
