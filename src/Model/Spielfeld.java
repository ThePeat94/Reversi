package Model;

public class Spielfeld {
    private Feld[][] spielFeld;
    private int groesse;

    public Spielfeld(int n)
    {
        groesse = n;
        spielFeld = new Feld[n][n];
    }

    public boolean spielerKannPlatzieren(Spieler spieler, int zeile, int spalte)
    {
        Feld zuSetzen = spielFeld[zeile][spalte];
        if(zuSetzen.getBesitzer().getValue() != 'O')
            return false;

        return false;
    }

    public void setzeStein(Spieler spieler, int spalte, int zeile)
    {
        spielFeld[zeile][spalte].setBesitzer(spieler);
    }

    public void initialisiereFeld(Spieler spieler1, Spieler spieler2, Spieler neutral)
    {
        for(int i = 0; i < spielFeld.length; i++)
        {
            for(int j = 0; j < spielFeld[i].length; j++)
            {
                spielFeld[i][j] = new Feld(neutral);
            }
        }

        int mitte = groesse/2;

        spielFeld[mitte][mitte].setBesitzer(spieler1);
        spielFeld[mitte-1][mitte-1].setBesitzer(spieler1);
        spielFeld[mitte-1][mitte].setBesitzer(spieler2);
        spielFeld[mitte][mitte-1].setBesitzer(spieler2);

    }

    public void output()
    {
        for(Feld[] zeile : spielFeld)
        {
            for(Feld feld : zeile)
            {
                System.out.print(feld.getBesitzer().getValue());
            }
            System.out.println();
        }
    }


}
