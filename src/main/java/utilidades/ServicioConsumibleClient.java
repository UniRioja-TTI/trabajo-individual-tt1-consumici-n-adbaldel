package utilidades;

import com.tt1.utilidades.model.ResultsResponse;
import com.tt1.utilidades.model.Solicitud;
import com.tt1.utilidades.model.SolicitudResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP para el servidor de simulaciones (ServicioConsumible).
 *
 * Las clases de modelo (Solicitud, SolicitudResponse, ResultsResponse) son
 * generadas automáticamente por OpenAPI Generator a partir de swagger.json.
 * Si la API del servidor cambia, basta con actualizar swagger.json y ejecutar
 * "mvn clean compile" para regenerar los modelos.
 */
@Component
public class ServicioConsumibleClient {

    private final RestClient restClient;
    private final Logger logger;

    public ServicioConsumibleClient(
            RestClient.Builder restClientBuilder,
            @Value("${consumible.base-url}") String baseUrl,
            Logger logger) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        this.logger = logger;
    }

    /**
     * Envía una solicitud de simulación al servidor.
     *
     * @param nombreUsuario identificador del usuario (constante por ahora)
     * @param solicitud     cuerpo con nombres de entidades y cantidades iniciales
     * @return respuesta con el token asignado, o null si hubo error de red
     */
    public SolicitudResponse solicitar(String nombreUsuario, Solicitud solicitud) {
        try {
            return restClient.post()
                    .uri("/Solicitud/Solicitar?nombreUsuario={u}", nombreUsuario)
                    .body(solicitud)
                    .retrieve()
                    .body(SolicitudResponse.class);
        } catch (Exception e) {
            logger.error("Error al solicitar simulación: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Descarga los resultados de una simulación ya procesada.
     *
     * @param nombreUsuario identificador del usuario
     * @param tok           token devuelto al solicitar la simulación
     * @return respuesta con los datos en formato texto, o null si hubo error
     */
    public ResultsResponse obtenerResultados(String nombreUsuario, int tok) {
        try {
            return restClient.post()
                    .uri("/Resultados?nombreUsuario={u}&tok={t}", nombreUsuario, tok)
                    .retrieve()
                    .body(ResultsResponse.class);
        } catch (Exception e) {
            logger.error("Error al obtener resultados (tok={}): {}", tok, e.getMessage());
            return null;
        }
    }
}
