package servicios;

import com.tt1.utilidades.model.ResultsResponse;
import com.tt1.utilidades.model.Solicitud;
import com.tt1.utilidades.model.SolicitudResponse;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utilidades.ServicioConsumibleClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactoSimTest {

    @Mock
    private ServicioConsumibleClient mockClient;

    private ContactoSim contactoSim;

    @BeforeEach
    void setUp() {
        contactoSim = new ContactoSim(mockClient);
    }

    @AfterEach
    void tearDown() {
        contactoSim = null;
    }

    // --- TESTS UNITARIOS ---

    @Test
    void solicitarSimulation_devuelveTokenCuandoServicioOk() {
        // Arrange: configurar el mock para que devuelva un token=42
        SolicitudResponse respuesta = new SolicitudResponse();
        respuesta.setDone(true);
        respuesta.setTokenSolicitud(42);
        when(mockClient.solicitar(anyString(), any(Solicitud.class))).thenReturn(respuesta);

        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 10);
        nums.put(2, 5);
        DatosSolicitud sol = new DatosSolicitud(nums);

        // Act
        int tok = contactoSim.solicitarSimulation(sol);

        // Assert
        assertEquals(42, tok);
    }

    @Test
    void solicitarSimulation_devuelveMinusUnoSiServicioFalla() {
        when(mockClient.solicitar(anyString(), any(Solicitud.class))).thenReturn(null);

        int tok = contactoSim.solicitarSimulation(new DatosSolicitud(new HashMap<>()));

        assertEquals(-1, tok);
    }

    @Test
    void descargarDatos_parsea_rawDataCorrectamente() {
        ResultsResponse respuesta = new ResultsResponse();
        respuesta.setDone(true);
        respuesta.setData("3\n0,1,0,red\n0,1,1,yellow\n1,1,2,red");
        when(mockClient.obtenerResultados(anyString(), anyInt())).thenReturn(respuesta);

        DatosSimulation sim = contactoSim.descargarDatos(99);

        assertEquals(3, sim.getAncho());
        assertEquals(2, sim.getAlto());
        assertEquals(2, sim.getNumFrames());
        assertEquals("red",    sim.getFrames().get(0).get(1).get(0));
        assertEquals("yellow", sim.getFrames().get(0).get(1).get(1));
    }

    @Test
    void descargarDatos_devuelveVacioSiServicioFalla() {
        when(mockClient.obtenerResultados(anyString(), anyInt())).thenReturn(null);

        DatosSimulation sim = contactoSim.descargarDatos(1);

        assertEquals(0, sim.getAncho());
        assertEquals(0, sim.getNumFrames());
    }

    @Test
    void getEntities_devuelveListaCorrecta() {
        List<Entidad> entities = contactoSim.getEntities();

        assertEquals(ContactoSim.NOMBRES_ENTIDADES.size(), entities.size());
        assertEquals("Entidad 1", entities.get(0).getName());
        assertEquals(1, entities.get(0).getId());
    }

    @Test
    void isValidEntityId_idValidoDevuelveTrue() {
        assertTrue(contactoSim.isValidEntityId(1));
        assertTrue(contactoSim.isValidEntityId(2));
    }

    @Test
    void isValidEntityId_idInvalidoDevuelveFalse() {
        assertFalse(contactoSim.isValidEntityId(0));
        assertFalse(contactoSim.isValidEntityId(999));
    }
}
