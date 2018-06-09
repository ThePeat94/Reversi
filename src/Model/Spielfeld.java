package Model;

public class Spielfeld {

    private Feld[][] spielFeld;
    private int groesse;

    public Spielfeld(int n)
    {
        groesse = n;
        spielFeld = new Feld[n][n];
    }

    private boolean spielerKannPlatzieren(Spieler spieler, int zeile, int spalte)
    {
        Feld zuSetzen = spielFeld[zeile][spalte];
        if(zuSetzen.getBesitzer() != null)
            return false;

        /**
         * Prüfen, ob der Stein andere Steine umschließt
         * DIAGONAL
         * VERTIKAL
         * HORIZONTAL
         */

        return true;
    }

    private Feld getBegrenzer(Spieler spieler, int zeile, int spalte)
    {
        for(int i = 0; i < groesse; i++)
        {
            // Horizontal
            spielFeld[zeile][i];

            // Vertikal
            spielFeld[i][spalte];
        }
    }

    public boolean setzeStein(Spieler spieler, int zeile, int spalte)
    {
        if(spielerKannPlatzieren(spieler, zeile, spalte))
        {
            spielFeld[zeile][spalte].setBesitzer(spieler);
            return true;
        }

        return false;
    }

    public void initialisiereFeld(Spieler spieler1, Spieler spieler2)
    {
        for(int i = 0; i < spielFeld.length; i++)
        {
            for(int j = 0; j < spielFeld[i].length; j++)
            {
                spielFeld[i][j] = new Feld();
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

        String tHead = "";
        for(int i = 1; i <= groesse; i++)
        {
            tHead += "\t" + String.valueOf(i);
        }

        System.out.println(tHead);
        int row = 1;

        for(Feld[] zeile : spielFeld)
        {
            String rowContent = String.valueOf(row) + "\t";
            for(Feld feld : zeile)
            {
                if(feld.getBesitzer() != null)
                    rowContent += feld.getBesitzer().getValue() + "\t";
                else
                    rowContent += "O\t";
            }
            System.out.println(rowContent);
            row++;
        }
    }


}
