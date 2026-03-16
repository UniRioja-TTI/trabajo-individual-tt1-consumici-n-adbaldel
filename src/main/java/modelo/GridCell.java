package modelo;

/** Representa una celda coloreada en un instante de tiempo de la simulación. */
public class GridCell {
    private int tiempo;
    private int y;
    private int x;
    private String color;

    public int getTiempo() { return tiempo; }
    public void setTiempo(int tiempo) { this.tiempo = tiempo; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
