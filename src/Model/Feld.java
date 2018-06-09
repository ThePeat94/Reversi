package Model;

public class Feld {
    private Spieler besitzer;

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
