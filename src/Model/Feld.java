package Model;

public class Feld {
    private Spieler besitzer;

    public Feld()
    {
        this.besitzer = null;
    }

    public Feld(Spieler besitzer)
    {
        this.besitzer = besitzer;
    }

    public void setBesitzer(Spieler besitzer)
    {
        this.besitzer = besitzer;
    }

    public Spieler getBesitzer()
    {
        return besitzer;
    }
}
