package servicios;

import com.tt1.utilidades.model.Solicitud;
import com.tt1.utilidades.model.SolicitudResponse;
import com.tt1.utilidades.model.ResultsResponse;
import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.springframework.stereotype.Service;
import utilidades.ServicioConsumibleClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactoSim implements InterfazContactoSim {

    /**
     * Nombres de entidades que conoce el servidor.
     * Actualizar si el servidor expone entidades distintas (consultable en Swagger UI).
     */
    public static final List<String> NOMBRES_ENTIDADES = List.of("Entidad 1", "Entidad 2");

    /**
     * Nombre de usuario constante mientras no haya autenticación real.
     * El enunciado de práctica 5 indica usar una cadena constante por ahora.
     */
    public static final String NOMBRE_USUARIO = "usuario";

    private final ServicioConsumibleClient client;

    public ContactoSim(ServicioConsumibleClient client) {
        this.client = client;
    }

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        // Construir el objeto Solicitud que espera el servidor
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(new ArrayList<>(NOMBRES_ENTIDADES));

        // Mapear el Map<id, cantidad> interno a una lista ordenada por índice de entidad
        List<Integer> cantidades = new ArrayList<>();
        for (int i = 0; i < NOMBRES_ENTIDADES.size(); i++) {
            cantidades.add(sol.getNums().getOrDefault(i + 1, 0));
        }
        solicitud.setCantidadesIniciales(cantidades);

        SolicitudResponse response = client.solicitar(NOMBRE_USUARIO, solicitud);
        if (response != null && Boolean.TRUE.equals(response.getDone())) {
            return response.getTokenSolicitud();
        }
        return -1;
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        ResultsResponse response = client.obtenerResultados(NOMBRE_USUARIO, ticket);
        if (response != null && Boolean.TRUE.equals(response.getDone()) && response.getData() != null) {
            return DatosSimulation.parse(response.getData());
        }
        return new DatosSimulation();
    }

    @Override
    public List<Entidad> getEntities() {
        List<Entidad> entidades = new ArrayList<>();
        for (int i = 0; i < NOMBRES_ENTIDADES.size(); i++) {
            Entidad e = new Entidad();
            e.setId(i + 1);
            e.setName(NOMBRES_ENTIDADES.get(i));
            e.setDescripcion("Descripción de " + NOMBRES_ENTIDADES.get(i));
            entidades.add(e);
        }
        return entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return id >= 1 && id <= NOMBRES_ENTIDADES.size();
    }
}
