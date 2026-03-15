package servicios;

import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactoSimTest
{
    private ContactoSim contactoSim;

    // --- Arrange Before/After each test -------------------------------------------------------------------

    @BeforeEach
    void setUp()
    {
        contactoSim = new ContactoSim();
    }

    @AfterEach
    void tearDown()
    {
        contactoSim = null;
    }

    // --- --------------- -------------------------------------------------------------------
    // --- TESTS UNITARIOS -------------------------------------------------------------------
    // --- --------------- -------------------------------------------------------------------

    // --- Test solicitarSimulation -------------------------------------------------------------------

    @Test
    void solicitarSimulation()
    {
        Map<Integer, Integer> nums = new HashMap<>(); // Datos para los que se sabría el resultado
        DatosSolicitud sol = new DatosSolicitud(nums);
        int tok;

        tok = contactoSim.solicitarSimulation(sol);

        // assert...(): Comprobar si el token es válido
        // O quizá, descargar datos con el token y comprobar si la simulación devuelve los datos correctos (requiere de
        // llamar al método descargarDatos)
    }

    // --- Test descargarDatos -------------------------------------------------------------------

    @Test
    void descargarDatos()
    {
        int tok = 1; // Token que espera el mock, o quizá, un token generado al solicitar la simulación (requiere de
                     // llamar al método solicitarSimulation)
        DatosSimulation sim;

        sim = contactoSim.descargarDatos(tok);

        // assert...(): Comprobar si los datos de simulación son los esperados
    }

    // --- Test getEntities -------------------------------------------------------------------

    @Test
    void getEntities()
    {
        List<Entidad> entities;

        entities = contactoSim.getEntities();

        assertEquals(2, entities.size());
        // assert...(): Comprobar que se devuelven la cantidad de entidades esperadas
        // También se puede comprobar que sean iguales a las que se esperan
    }

    // --- Test isValidEntityId -------------------------------------------------------------------

    @Test
    void isValidEntityId()
    {
        boolean valid;

        valid = contactoSim.isValidEntityId();

        // assert...(): No entiendo que hace este método si no recibe un int entidad como parámetro
    }
}