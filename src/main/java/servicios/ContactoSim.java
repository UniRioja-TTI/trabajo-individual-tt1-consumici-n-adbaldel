package servicios;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;

@Service
public class ContactoSim implements InterfazContactoSim
{
    public static final List<String> NOMBRES_ENTIDADES = List.of("Entidad 1", "Entidad 2");

    private Map<Integer, DatosSolicitud> solicitudes;
    private Random random;

    public ContactoSim()
    {
        solicitudes = new HashMap<>();
        random = new Random(); // Le insertaria una semilla en el constructor pero IntellIJ se queja de que

        // Probablemente necesite una conexion a un simulador pasada como parametro pero no existe dicha clase
        // en mi proyecto entonces lo dejo vacio
    }

    @Override
    public int solicitarSimulation(DatosSolicitud sol)
    {
        int token = random.nextInt(10000);

        solicitudes.put(token, sol);

        return token;
    }

    @Override
    public List<Entidad> getEntities()
    {
        List<Entidad> entidades = new ArrayList<>();
        Entidad entidad;

        entidad = new Entidad();
        entidad.setId(1);
        entidad.setName("Entidad 1");
        entidad.setDescripcion("Descripción 1");
        entidades.add(entidad);

        entidad = new Entidad();
        entidad.setId(2);
        entidad.setName("Entidad 2");
        entidad.setDescripcion("Descripción 2");
        entidades.add(entidad);

        return entidades;
    }

    @Override
    public boolean isValidEntityId()
    {
        // No entiendo que hace este método si no recibe un int entidad como parámetro
        return true;
    }

    @Override
    public DatosSimulation descargarDatos(int ticket)
    {
        DatosSimulation sim = new DatosSimulation();

        return sim;
    }
}