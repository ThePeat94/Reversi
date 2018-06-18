package Model;

/**
 * Repräsentiert ein Feld auf dem Spielfeld
 */
public class Feld {

    /**
     * Der Besitzer des Spielfelds
     */
    private Spieler besitzer;

    public Feld()
    {
        this.besitzer = null;
    }

    public Feld(Spieler besitzer)
    {
        this.besitzer = besitzer;
    }

    /**
     * Setter für den Besitzer des Felds
     * @param besitzer Der Besitzer des Felds
     */
    public void setBesitzer(Spieler besitzer)
    {
        this.besitzer = besitzer;
    }

    /**
     * Getter für den Besitzer des Felds
     * @return Den Besitzer des Felds
     */
    public Spieler getBesitzer()
    {
        return besitzer;
    }
}
