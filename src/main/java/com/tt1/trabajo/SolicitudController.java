package com.tt1.trabajo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SolicitudController {

    private final InterfazContactoSim ics;
    private final Logger logger;
    private final ObjectMapper objectMapper;

    public SolicitudController(InterfazContactoSim ics, Logger logger, ObjectMapper objectMapper) {
        this.ics = ics;
        this.logger = logger;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/solicitud")
    public String solicitud(Model model) {
        model.addAttribute("entities", ics.getEntities());
        return "solicitud";
    }

    @PostMapping("/solicitud")
    public String handleSolicitud(@RequestParam Map<String, String> formData, Model model) {
        Map<Integer, Integer> validData = new HashMap<>();
        List<String> errors = new ArrayList<>();

        formData.forEach((key, value) -> {
            try {
                int num = Integer.parseInt(value);
                if (num < 0) {
                    errors.add(key + " no puede ser negativo");
                    return;
                }
                int id = Integer.parseInt(key);
                if (ics.isValidEntityId(id)) {       // <-- corregido: se pasa el id
                    validData.put(id, num);
                } else {
                    errors.add(key + " no se corresponde con una entidad");
                }
            } catch (NumberFormatException e) {
                errors.add(key + " debe ser un número entero");
            }
        });

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            logger.warn("Atendida petición con errores");
        } else {
            logger.info("Atendida petición");
            DatosSolicitud ds = new DatosSolicitud(validData);
            int tok = ics.solicitarSimulation(ds);
            if (tok != -1) {
                model.addAttribute("token", tok);
            } else {
                logger.error("Error en comunicación con servidor de simulación");
                model.addAttribute("errors", List.of("Error al contactar con el servidor de simulación"));
            }
        }
        return "formResult";
    }

    /**
     * Descarga los resultados de una simulación y los renderiza como cuadrícula animada.
     * URL: /grid?tok={token}
     */
    @GetMapping("/grid")
    public String grid(@RequestParam int tok, Model model) {
        logger.info("Solicitando resultados para tok={}", tok);
        DatosSimulation sim = ics.descargarDatos(tok);

        model.addAttribute("tok", tok);
        model.addAttribute("ancho", sim.getAncho());
        model.addAttribute("alto", sim.getAlto());
        model.addAttribute("numFrames", sim.getNumFrames());

        // Serializar los frames a JSON para que el JS del template los anime
        try {
            model.addAttribute("framesJson", objectMapper.writeValueAsString(sim.getFrames()));
        } catch (JsonProcessingException e) {
            logger.error("Error serializando frames: {}", e.getMessage());
            model.addAttribute("framesJson", "[]");
        }

        return "grid";
    }
}
