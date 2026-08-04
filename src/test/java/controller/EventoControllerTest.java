package controller;

import model.Evento;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EventoControllerTest {

    @Test
    void testListarEventos() {
        EventoController controller = new EventoController();
        
        // Solicitamos la lista general de eventos
        List<Evento> eventos = controller.listar();
        assertNotNull(eventos);
    }

    @Test
    void testBuscarEventoInexistente() {
        EventoController controller = new EventoController();
        
        // Evaluamos el comportamiento ante un ID inválido
        Evento evento = controller.buscarPorId(-999);
        assertNull(evento);
    }
}
