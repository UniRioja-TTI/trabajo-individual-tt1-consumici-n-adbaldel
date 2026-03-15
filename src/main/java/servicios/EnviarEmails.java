package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class EnviarEmails implements InterfazEnviarEmails
{
    private Logger logger;

    public EnviarEmails(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email)
    {
        logger.info("Solicitud de envío de email: - destinatario: {} - mensaje: {}", dest, email);
        return true;
    }
}
