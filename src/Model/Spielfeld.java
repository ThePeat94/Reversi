package Model;

import Utils.GameRuleException;
import Utils.Vektor;

/**
 * Repräsentiert ein Spielfeld
 */
public class Spielfeld {

    /**
     * Das Spielfeld an sich
     */
    private Feld[][] spielFeld;

    /**
     * Die Anzahl der freien Felder
     */
    private int freieFelder;

    /**
     * Die Größe des Spielfelds (groesse x groesse)
     */
    private int groesse;

    public Spielfeld(int n)
    {
        groesse = n;
        spielFeld = new Feld[n][n];
        freieFelder = n*n;
    }


    /**
     * Getter für die Anzahl der freien Felder
     * @return Anzahl freier Felder
     */
    public int getFreieFelder() {
        return freieFelder;
    }

    /**
     * Getter für das Spielfeld
     * @return Ein zweidimensionales Array, welches das Spielfeld darstellt
     */
    public Feld[][] getSpielFeld() {
        return spielFeld;
    }


    /**
     * Erfasst die begrenzenden Steine des Spielers für ein Zielfeld
     * @param spieler Der Spieler, für den die Steine erfasst werden sollen
     * @param zielFeld Das Feld, wo ein neuer Stein gesetzt werden soll
     * @return Ein Array mit allen begrenzenden Steinen. Die Begrenzer sind wie folgt: <br>
     * [0] Horizontal, [0][0] links, [0][1] rechts <br>
     * [1] Vertikal, [1][0] oben, [1][1] unten <br>
     * [2] Diagonal, [2][0] oben links, [2][1] oben rechts [2][2] unten rechts, [2][3] unten links
     */
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

        // 8 Felder müssen immer geprüft werden
        Vektor[] pruefendeFelder = new Vektor[8];

        for(int i = 1; i < groesse; i++)
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

                // Ist der Vektor überhaupt noch im Spielfeld?
                if(aktuellerVektor.getY() < 0 || aktuellerVektor.getX() < 0 || aktuellerVektor.getY() >= groesse || aktuellerVektor.getX() >= groesse)
                    continue;

                Feld pruefendesFeld = getFeldByVektor(aktuellerVektor);

                // Welches Feld wurde geprüft?
                // Stein in Richtung schräg oben links
                if(j == 0)
                {
                    // Wenn kein Begrenzer gefunden wurde und das aktuelle Feld keinen Besitzer hat, so ist das Zielfeld der Begrenzer
                    if(ergebnis[2][0] == null && pruefendesFeld.getBesitzer() == null)
                        ergebnis[2][0] = zielFeld;

                    // Wenn der Begrenzer dieser Richtung noch nicht gefunden wurde und der Besitzer des Felds der Spieler ist, so ist dies der Begrenzer
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

    /**
     * Setzt einen Stein für einen Spieler an einem gegebenem Vektor
     * @param spieler Der Spieler, für den gesetzt werden soll
     * @param vektor Das Zielfeld
     * @param steinSollGesetztWerden Soll der Stein wirklich gesetzt werden, oder wird nur zu Berechnungszwecken simuliert?
     * @return true, wenn der Stein gesetzt wurde, false wenn nicht
     * @throws GameRuleException Sollte eine Spielregel verletzt worden sein, so wird diese Exception geworfen
     */
    public boolean setzeStein(Spieler spieler, Vektor vektor, boolean steinSollGesetztWerden) throws GameRuleException
    {
        int setzendeZeile = vektor.getY();
        int setzendeSpalte = vektor.getX();

        // Feld ist leer
        if(getFeldByVektor(vektor).getBesitzer() == null )
        {
            // Liegt der Stein an einem gegnerischen Stein an?
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

                // Umschließen die Steine gegnerische Steine?
                if(begrenzerUmschliessenGegner(vektor, begrenzendeSteine))
                {
                    // Stein soll nicht gesetzt werden, wenn die Methode genutzt wird,
                    // um zu prüfen, ob der Spieler irgendwo setzen kann
                    if(steinSollGesetztWerden)
                    {
                        dreheSteineAufHorizontalerAchseUm(spieler, begrenzendeSteine[0]);
                        dreheSteineAufVertikalerchseUm(spieler, begrenzendeSteine[1]);
                        dreheSteineAufDiagonalerchseUm(spieler, begrenzendeSteine[2]);
                        spielFeld[vektor.getY()][vektor.getX()].setBesitzer(spieler);
                        spieler.setAnzSteine(spieler.getAnzSteine() + 1);
                        freieFelder--;
                    }
                    return true;
                }
                else
                {
                    // Nur eine Exception werfen, wenn der Stein wirklich gesetzt werden soll
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
                // Nur eine Exception werfen, wenn der Stein wirklich gesetzt werden soll
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
            // Nur eine Exception werfen, wenn der Stein wirklich gesetzt werden soll
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

    /**
     * Prüft, ob gegebene Begrenzer und das Zielfeld gegnerische Steine umschließen (Distanz zwischen Ziel und Begrenzer ist dann >= 2 Felder)
     * @param ziel Das Zielfeld, wo gesetzt werden soll
     * @param begrenzendeSteine Die gefundenen begrenzenden Steine
     * @return true, wenn Gegner duch das Ziel und die Begrenzer umschlossen werden, false wenn nicht
     */
    private boolean begrenzerUmschliessenGegner(Vektor ziel, Vektor[][] begrenzendeSteine)
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

    /**
     * Wenn ein Begrenzer für eine Richtung nicht vorhanden ist, so gibt es hier keinen => Auffüllen mit dem Zielfeld
     * @param zielVektor Das Zielfeld
     * @param begrenzer Die Begrenzersteine
     * @return Das aufgefüllte Begrenzerarray
     */
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

    /**
     * Dreht alle Steine zwischen den Begrenzern auf horizontaler Ebene um
     * @param neuerBesitzer Der neue Besitzer des Felds
     * @param begrenzer Die Begrenzer für die horizontale
     */
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

    /**
     * Dreht alle Steine zwischen den Begrenzern auf vertikaler Ebene um
     * @param neuerBesitzer Der neue Besitzer des Felds
     * @param begrenzer Die Begrenzer für die vertikale
     */
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

    /**
     * Dreht alle Steine zwischen den Begrenzern auf diagonaler Ebene um
     * @param neuerBesitzer Der neue Besitzer
     * @param begrenzer Die Begrenzer auf der diagonalen
     */
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
     * Prüft, ob der zu setzende Stein an einen gegnerischen Stein angrenzt
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

    /**
     * Initialisiert das Spielfeld => Mittig die Steine der Spieler, rest leer
     * @param spieler1 1. Spieler
     * @param spieler2 2. Spieler
     */
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

        spieler1.setAnzSteine(2);
        spieler2.setAnzSteine(2);

        freieFelder -= 4;

    }

    /**
     * Konsolenausgabe des Spielfelds
     */
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
                    rowContent += feld.getBesitzer().getSteinZeichen() + "\t";
                else
                    rowContent += "O\t";
            }
            System.out.println(rowContent);
            row++;
        }
    }

    /**
     * Greift ein Feld nach einem Vektor ab
     * @param ziel Das Zielfeld
     * @return Das Feld bei gegebenem Vektor
     */
    private Feld getFeldByVektor(Vektor ziel)
    {
        return spielFeld[ziel.getY()][ziel.getX()];
    }

    /**
     * Getter für die Größe des Felds
     * @return Größe des Spielfelds
     */
    public int getGroesse() {
        return groesse;
    }
}
