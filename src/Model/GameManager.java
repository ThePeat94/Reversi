package Model;

public class GameManager {
    private Spieler spieler1, spieler2, neutral;
    private Spielfeld spielFeld;

    public GameManager()
    {
        spieler1 = new Spieler('X');
        spieler2 = new Spieler('R');
        neutral = new Spieler('O');
    }

    public void StartNewGame(int n)
    {
        spielFeld = new Spielfeld(n);
        spielFeld.initialisiereFeld(spieler1, spieler2, neutral);
        spielFeld.output();


    }


}
