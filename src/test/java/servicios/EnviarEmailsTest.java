package servicios;

import modelo.Destinatario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class EnviarEmailsTest {

    private EnviarEmails enviarEmails;

    @BeforeEach
    void setUp() {
        enviarEmails = new EnviarEmails(LoggerFactory.getLogger("EnviarEmailsTest"));
    }

    @AfterEach
    void tearDown() {
        enviarEmails = null;
    }

    @Test
    void enviarEmailExitoso() {
        Destinatario dest = new Destinatario();
        String mensaje = "Hello World!";

        boolean enviado = enviarEmails.enviarEmail(dest, mensaje);

        assertTrue(enviado);
    }
}
