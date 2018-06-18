package Model;

/**
 * Stellt einen Spieler dar
 */
public class Spieler {
    /**
     * Der Name des Spielers
     */
    private String name;

    /**
     * Das Zeichen des Steins des Spielers
     */
    private char steinZeichen;

    /**
     * Anzahl der Steine des Spielers
     */
    private int anzSteine;


    public Spieler(char steinZeichen)
    {
        this.steinZeichen = steinZeichen;
    }

    /**
     * Getter für das steinZeichen
     * @return Wert des Steinzeichens
     */
    public char getSteinZeichen()
    {
        return steinZeichen;
    }

    /**
     * Getter für den Namen
     * @return Name des Spielers
     */
    public String getName() {
        return name;
    }

    /**
     * Setter für den Namen des Spielers
     * @param name neuer Name des Spielers
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter für die Anzahl der Steine
     * @return Anzahl der Steine
     */
    public int getAnzSteine() {
        return anzSteine;
    }

    /**
     * Setter für die Anzahl der Steine
     * @param anzSteine Neuer Wert
     */
    public void setAnzSteine(int anzSteine) {
        this.anzSteine = anzSteine;
    }
}
