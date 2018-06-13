package Model;

import Utils.GameRuleException;
import Utils.Vektor;

public class Spielfeld {



    public void setSpielFeld(Feld[][] spielFeld) {
        this.spielFeld = spielFeld;
    }

    private Feld[][] spielFeld;


    private int freieFelder;
    private int groesse;

    public Spielfeld(int n)
    {
        groesse = n;
        spielFeld = new Feld[n][n];
        freieFelder = n*n;
    }


    public int getFreieFelder() {
        return freieFelder;
    }

    public Feld[][] getSpielFeld() {
        return spielFeld;
    }



    private Vektor[][] erfasseBegrenzendeSteine(Spieler spieler, Vektor zielFeld)
    {
        Vektor[][] ergebnis = new Vektor[3][];

        // Horizontale Begrenzer
        // 0 = Links, 1 = rechts
        ergebnis[0] = new Vektor[2];

        // Vertikale Begrenzer
        // 0 = oben, 1 = unten
        ergebnis[1] = new Vektor[2];

        // Diagonale Begrenzer
        // 0 = oben links, 1 = oben rechts, 2 = unten rechts, 3 = unten links
        ergebnis[2] = new Vektor[4];

        int zielZeile = zielFeld.getY();
        int zielSpalte = zielFeld.getX();

        Vektor[] pruefendeFelder = new Vektor[8];

        for(int i = 1; i < groesse - 2; i++)
        {
            // Oben Links
            pruefendeFelder[0] = new Vektor(zielSpalte - i, zielZeile - i);

            // Oben Mitte
            pruefendeFelder[1] = new Vektor(zielSpalte, zielZeile - i);

            // Oben Rechts
            pruefendeFelder[2] = new Vektor(zielSpalte + i, zielZeile - i);

            // Mitte Rechts
            pruefendeFelder[3] = new Vektor(zielSpalte + i, zielZeile);

            // Unten Rechts
            pruefendeFelder[4] = new Vektor(zielSpalte + i, zielZeile + i);

            // Unten Mitte
            pruefendeFelder[5] = new Vektor(zielSpalte, zielZeile + i);

            // Unten Links
            pruefendeFelder[6] = new Vektor(zielSpalte - i, zielZeile + i);

            // Mitte Links
            pruefendeFelder[7] = new Vektor(zielSpalte - i, zielZeile);

            for(int j = 0; j < 8; j++)
            {
                Vektor aktuellerVektor = pruefendeFelder[j];

                if(aktuellerVektor.getY() < 0 || aktuellerVektor.getX() < 0 || aktuellerVektor.getY() >= groesse || aktuellerVektor.getX() >= groesse)
                    continue;

                Feld pruefendesFeld = getFeldByVektor(aktuellerVektor);

                // Stein in Richtung schräg oben links
                if(j == 0)
                {
                    if(ergebnis[2][0] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[2][0] = zielFeld;

                    if(ergebnis[2][0] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[2][0] = aktuellerVektor;
                }
                // Stein oben
                else if(j == 1)
                {
                    if(ergebnis[1][0] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[1][0] = zielFeld;

                    if(ergebnis[1][0] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[1][0] = aktuellerVektor;
                }
                // Stein schräg oben rechts
                else if(j == 2)
                {
                    if(ergebnis[2][1] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[2][1] = zielFeld;

                    if(ergebnis[2][1] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[2][1] = aktuellerVektor;
                }
                // Stein rechts
                else if(j == 3)
                {
                    if(ergebnis[0][1] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[0][1] = zielFeld;

                    if(ergebnis[0][1] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[0][1] = aktuellerVektor;
                }
                // Stein schräg unten rechts
                else if(j == 4)
                {
                    if(ergebnis[2][2] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[2][2] = zielFeld;

                    if(ergebnis[2][2] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[2][2] = aktuellerVektor;
                }
                // Stein unten
                else if(j == 5)
                {
                    if(ergebnis[1][1] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[1][1] = zielFeld;

                    if(ergebnis[1][1] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[1][1] = aktuellerVektor;
                }
                // Stein schräg unten links
                else if(j == 6)
                {
                    if(ergebnis[2][3] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[2][3] = zielFeld;

                    if(ergebnis[2][3] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[2][3] = aktuellerVektor;
                }
                // Stein links
                else if(j == 7)
                {
                    if(ergebnis[0][0] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[0][0] = zielFeld;

                    if(ergebnis[0][0] == null && pruefendesFeld.getBesitzer() == spieler)
                        ergebnis[0][0] = aktuellerVektor;
                }



            }
        }

        return ergebnis;
    }

    public boolean setzeStein(Spieler spieler, Vektor vektor, boolean steinSollGesetztWerden) throws GameRuleException
    {

        /**
         * Prüfen, ob der Stein andere Steine umschließt
         * DIAGONAL
         * VERTIKAL
         * HORIZONTAL
         */

        int setzendeZeile = vektor.getY();
        int setzendeSpalte = vektor.getX();

        // Feld ist leer
        if(spielFeld[setzendeZeile][setzendeSpalte].getBesitzer() == null )
        {
            if(neuerSteinLiegtAnGegner(spieler, vektor))
            {
                // [0] Horizontale Begrenzer
                // 0 = Links, 1 = rechts
                // [1] Vertikale Begrenzer
                // 0 = oben, 1 = unten
                // [2]Diagonale Begrenzer
                // 0 = oben links, 1 = oben rechts, 2 = unten rechts, 3 = unten links
                Vektor[][] begrenzendeSteine = erfasseBegrenzendeSteine(spieler, vektor);
                begrenzendeSteine = fuelleBegrenzerAuf(vektor, begrenzendeSteine);

                if(pruefeDistanz(vektor, begrenzendeSteine))
                {
                    // Stein soll nicht gesetzt werden, wenn die Methode genutzt wird,
                    // um zu prüfen, ob der Spieler irgendwo setzen kann
                    if(steinSollGesetztWerden)
                    {
                        dreheSteineAufHorizontalerAchseUm(spieler, begrenzendeSteine[0]);
                        dreheSteineAufVertikalerchseUm(spieler, begrenzendeSteine[1]);
                        dreheSteineAufDiagonalerchseUm(spieler, begrenzendeSteine[2]);
                        spielFeld[vektor.getY()][vektor.getX()].setBesitzer(spieler);
                        freieFelder--;
                    }
                    return true;
                }
                else
                {
                    if(steinSollGesetztWerden)
                    {
                        throw new GameRuleException("Durch das Setzen dieses Steines wird kein gegnerischer Stein durch eigene eingeschlossen.");
                    }
                    else
                    {
                        return false;
                    }
                }

            }
            else
            {
                if(steinSollGesetztWerden)
                {
                    throw new GameRuleException("Hier kann kein Stein platziert werden, da er an keinen gegnerischen Stein angrenzt!");
                }
                else
                {
                    return false;
                }

            }
        }
        else
        {
            if(steinSollGesetztWerden)
            {
                throw new GameRuleException("Dieses Feld ist bereits belegt.");
            }
            else
            {
                return false;
            }
        }
    }

    private boolean pruefeDistanz(Vektor ziel, Vektor[][] begrenzendeSteine)
    {
        for(Vektor[] richtung : begrenzendeSteine)
        {
            for(Vektor begrenzer : richtung)
            {
                Vektor distanz = ziel.distanzVektorZu(begrenzer);
                if(Math.abs(distanz.getX()) >= 2 || Math.abs(distanz.getY()) >= 2)
                    return true;
            }
        }
        return false;
    }

    private Vektor[][] fuelleBegrenzerAuf(Vektor zielVektor, Vektor[][] begrenzer)
    {
        Vektor[][] result = begrenzer;
        for(int i = 0; i < begrenzer.length; i++)
        {
            for(int j = 0; j < begrenzer[i].length; j++)
            {
                if(result[i][j] == null)
                    result[i][j] = zielVektor;
            }
        }
        return result;
    }

    private void dreheSteineAufHorizontalerAchseUm(Spieler neuerBesitzer, Vektor[] begrenzer)
    {
        // Horizontale Begrenzer
        // 0 = Links, 1 = rechts
        Vektor links = begrenzer[0];
        Vektor rechts = begrenzer[1];

        int zeile = links.getY();

        for(int spalte = links.getX(); spalte < rechts.getX(); spalte++)
        {
            Spieler aktuellerBesitzer = spielFeld[zeile][spalte].getBesitzer();
            if(aktuellerBesitzer != null && aktuellerBesitzer != neuerBesitzer)
            {
                aktuellerBesitzer.setAnzSteine(aktuellerBesitzer.getAnzSteine() - 1);
                spielFeld[zeile][spalte].setBesitzer(neuerBesitzer);
                neuerBesitzer.setAnzSteine(neuerBesitzer.getAnzSteine() + 1);
            }

        }
    }

    private void dreheSteineAufVertikalerchseUm(Spieler neuerBesitzer, Vektor[] begrenzer)
    {
        // [1] Vertikale Begrenzer
        // 0 = oben, 1 = unten
        Vektor oben = begrenzer[0];
        Vektor unten = begrenzer[1];

        int spalte = oben.getX();

        for(int zeile = oben.getY(); zeile < unten.getY(); zeile++)
        {
            Spieler aktuellerBesitzer = spielFeld[zeile][spalte].getBesitzer();
            if(aktuellerBesitzer != null && aktuellerBesitzer != neuerBesitzer)
            {
                aktuellerBesitzer.setAnzSteine(aktuellerBesitzer.getAnzSteine() - 1);
                spielFeld[zeile][spalte].setBesitzer(neuerBesitzer);
                neuerBesitzer.setAnzSteine(neuerBesitzer.getAnzSteine() + 1);
            }
        }
    }


    private void dreheSteineAufDiagonalerchseUm(Spieler neuerBesitzer, Vektor[] begrenzer)
    {
        // [2]Diagonale Begrenzer
        // 0 = oben links, 1 = oben rechts, 2 = unten rechts, 3 = unten links
        Vektor obenLinks = begrenzer[0];
        Vektor untenRechts = begrenzer[2];

        Vektor obenRechts = begrenzer[1];
        Vektor untenLinks = begrenzer[3];

        // von links oben nach rechts unten
        int schritte = untenRechts.getX() - obenLinks.getX();

        for(int i = 0; i < schritte; i++)
        {
            Spieler aktuellerBesitzer = spielFeld[obenLinks.getY() + i][obenLinks.getX() + i].getBesitzer();

            if(aktuellerBesitzer != null && aktuellerBesitzer != neuerBesitzer)
            {
                aktuellerBesitzer.setAnzSteine(aktuellerBesitzer.getAnzSteine() - 1);
                spielFeld[obenLinks.getY() + i][obenLinks.getX() + i].setBesitzer(neuerBesitzer);
                neuerBesitzer.setAnzSteine(neuerBesitzer.getAnzSteine() + 1);
            }
        }

        // von unten links nach oben rechts

        // von links unten nach rechts oben
        schritte = obenRechts.getX() - untenLinks.getX();

        for(int i = 0; i < schritte; i++)
        {
            Spieler aktuellerBesitzer = spielFeld[untenLinks.getY() - i][untenLinks.getX() + i].getBesitzer();
            if(aktuellerBesitzer != null && aktuellerBesitzer != neuerBesitzer)
            {
                aktuellerBesitzer.setAnzSteine(aktuellerBesitzer.getAnzSteine() - 1);
                spielFeld[untenLinks.getY() - i][untenLinks.getX() + i].setBesitzer(neuerBesitzer);
                neuerBesitzer.setAnzSteine(neuerBesitzer.getAnzSteine() + 1);
            }
        }
    }

    /**
     * Prüft, pb der zu setzende Stein an einen gegnerischen Stein angrenzt
     * @param spieler Der Spieler, welcher am Zug ist
     * @param vektor Das Zielfeld
     * @return true, wenn ein gegnerischer Stein anliegt, false, wenn nicht
     */
    private boolean neuerSteinLiegtAnGegner(Spieler spieler, Vektor vektor)
    {
        int zielZeile = vektor.getY();
        int zielSpalte = vektor.getX();

        Vektor[] pruefendeFelder = new Vektor[8];

        // Oben Links
        pruefendeFelder[0] = new Vektor(zielSpalte - 1, zielZeile - 1);

        // Oben Mitte
        pruefendeFelder[1] = new Vektor(zielSpalte, zielZeile - 1);

        // Oben Rechts
        pruefendeFelder[2] = new Vektor(zielSpalte + 1, zielZeile - 1);

        // Mitte Rechts
        pruefendeFelder[3] = new Vektor(zielSpalte + 1, zielZeile);

        // Unten Rechts
        pruefendeFelder[4] = new Vektor(zielSpalte + 1, zielZeile + 1);

        // Unten Mitte
        pruefendeFelder[5] = new Vektor(zielSpalte, zielZeile + 1);

        // Unten Links
        pruefendeFelder[6] = new Vektor(zielSpalte - 1, zielZeile + 1);

        // Mitte Links
        pruefendeFelder[7] = new Vektor(zielSpalte - 1, zielZeile);

        for(Vektor aktuellerVektor : pruefendeFelder)
        {
            if(aktuellerVektor.getY() < 0 || aktuellerVektor.getX() < 0 || aktuellerVektor.getY() >= groesse || aktuellerVektor.getX() >= groesse)
                continue;

            int pruefendeZeile = aktuellerVektor.getY();
            int pruefendeSpalte = aktuellerVektor.getX();

            Feld pruefendesFeld = getFeldByVektor(aktuellerVektor);

            if(pruefendesFeld.getBesitzer() != null && pruefendesFeld.getBesitzer() != spieler)
            {
                return true;
            }
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
        for(int i = 0; i < groesse; i++)
        {
            tHead += "\t" + String.valueOf(i);
        }

        System.out.println(tHead);

        int row = 0;

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

    private Feld getFeldByVektor(Vektor ziel)
    {
        return spielFeld[ziel.getY()][ziel.getX()];
    }

    public int getGroesse() {
        return groesse;
    }


}
