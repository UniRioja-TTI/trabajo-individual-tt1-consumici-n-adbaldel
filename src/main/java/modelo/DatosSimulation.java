package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiene los datos de una simulación descargada del servidor.
 * El servidor devuelve un texto con el formato:
 * <pre>
 *   12
 *   0,7,5,red
 *   0,7,4,yellow
 *   1,8,5,red
 * </pre>
 * donde la primera línea es el ancho de la cuadrícula y cada línea siguiente
 * es una celda: tiempo,y,x,color.
 */
public class DatosSimulation {

    private int ancho;
    private int alto;
    private int numFrames;

    /**
     * frames.get(t).get(y).get(x) = nombre CSS del color, o "" si vacío.
     * Así Thymeleaf puede serializar directamente a JSON para el frontend.
     */
    private List<List<List<String>>> frames;

    public DatosSimulation() {
        this.ancho = 0;
        this.alto = 0;
        this.numFrames = 0;
        this.frames = new ArrayList<>();
    }

    /**
     * Parsea la cadena de texto devuelta por el endpoint /Resultados
     * y construye un DatosSimulation listo para renderizar.
     */
    public static DatosSimulation parse(String rawData) {
        DatosSimulation result = new DatosSimulation();
        if (rawData == null || rawData.isBlank()) return result;

        String[] lines = rawData.trim().split("\\r?\\n");
        if (lines.length == 0) return result;

        try {
            result.ancho = Integer.parseInt(lines[0].trim());
        } catch (NumberFormatException e) {
            return result;
        }

        List<GridCell> cells = new ArrayList<>();
        int maxY = 0;
        int maxT = 0;

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 4) continue;
            try {
                GridCell cell = new GridCell();
                cell.setTiempo(Integer.parseInt(parts[0].trim()));
                cell.setY(Integer.parseInt(parts[1].trim()));
                cell.setX(Integer.parseInt(parts[2].trim()));
                cell.setColor(parts[3].trim());
                cells.add(cell);
                maxY = Math.max(maxY, cell.getY());
                maxT = Math.max(maxT, cell.getTiempo());
            } catch (NumberFormatException ignored) {
                // línea malformada, se ignora
            }
        }

        result.alto = maxY + 1;
        result.numFrames = maxT + 1;

        // Inicializar frames vacíos: frames[t][y][x] = ""
        result.frames = new ArrayList<>();
        for (int t = 0; t < result.numFrames; t++) {
            List<List<String>> frame = new ArrayList<>();
            for (int y = 0; y < result.alto; y++) {
                List<String> row = new ArrayList<>();
                for (int x = 0; x < result.ancho; x++) {
                    row.add("");
                }
                frame.add(row);
            }
            result.frames.add(frame);
        }

        // Rellenar celdas con color
        for (GridCell cell : cells) {
            if (cell.getTiempo() < result.numFrames
                    && cell.getY() < result.alto
                    && cell.getX() < result.ancho) {
                result.frames.get(cell.getTiempo()).get(cell.getY()).set(cell.getX(), cell.getColor());
            }
        }

        return result;
    }

    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public int getNumFrames() { return numFrames; }
    public List<List<List<String>>> getFrames() { return frames; }
}
