package servicios;

import modelo.Destinatario;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class EnviarEmailsTest
{
    private EnviarEmails enviarEmails;

    // --- Arrange Before/After each test -------------------------------------------------------------------

    @BeforeEach
    void setUp()
    {
        enviarEmails = new EnviarEmails(LoggerFactory.getLogger("EnviarEmailsTest"));
    }

    @AfterEach
    void tearDown()
    {
        enviarEmails = null;
    }

    // --- --------------- -------------------------------------------------------------------
    // --- TESTS UNITARIOS -------------------------------------------------------------------
    // --- --------------- -------------------------------------------------------------------

    // --- Test enviarEmail -------------------------------------------------------------------

    @Test
    void enviarEmailExitoso()
    {
        Destinatario dest = new Destinatario();
        String email = "Hello World!";
        boolean enviado;

        enviado = enviarEmails.enviarEmail(dest, email);

        assertTrue(enviado);
    }
}