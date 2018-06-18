package Utils;

/**
 * Vektorenklasse
 */
public class Vektor {

    /**
     * x-Koordinate
     */
    private int x;

    /**
     * y-Koordinate
     */
    private int y;

    public Vektor()
    {
        x = 0;
        y = 0;
    }

    public Vektor(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter für die x-Koordinate
     * @return Die x-Koordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Setter für die x-Koordinate
     * @param x Neuer Wert der x-Koordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter für die y-Koordinate
     * @return Die y-Koordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Setter für die y-Koordinate
     * @param y Neuer Wert der y-Koordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return String.format("(%d/%d)", x, y);
    }

    /**
     * Gibt einen Distanzvektor zum gegebenen Ziel zurück (ziel - this)
     * @param ziel Das Ziel
     * @return Die Distanz zum Ziel
     */
    public Vektor distanzVektorZu(Vektor ziel)
    {
        Vektor distanzVektor = new Vektor();
        distanzVektor.setX(ziel.x - this.x);
        distanzVektor.setY(ziel.y - this.y);

        return distanzVektor;
    }
}
