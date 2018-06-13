package Model;

import Utils.GameRuleException;
import Utils.Vektor;

import java.util.Scanner;

public class GameManager {
    private Spieler spieler1, spieler2, neutral;
    private Spielfeld spielFeld;

    Scanner in = new Scanner(System.in);

    public GameManager()
    {
        spieler1 = new Spieler('X');
        spieler2 = new Spieler('R');
    }

    public void StartNewGame(int n)
    {
        System.out.println("Name des 1. Spielers: ");
        String name = in.nextLine();
        spieler1.setName(name);

        System.out.println("Name des 2. Spielers: ");
        name = in.nextLine();
        spieler2.setName(name);

        spielFeld = new Spielfeld(n);
        spielFeld.initialisiereFeld(spieler1, spieler2);
        spielFeld.output();

        boolean freieFelderVerfuegbar = true;
        Spieler aktiverSpieler = spieler1;
        while(freieFelderVerfuegbar)
        {
            try
            {
                System.out.println(aktiverSpieler.getName() + " du bist am Zug.");
                int zeile = -1;
                int spalte = -1;
                Vektor ziel = new Vektor();
                boolean spielerKannSetzen = false;

                Feld[][] felder = spielFeld.getSpielFeld();

                for(int idReihe = 0; idReihe < felder.length; idReihe++)
                {
                    Feld[] feldReihe = felder[idReihe];

                    for(int idSpalte = 0; idSpalte < feldReihe.length; idSpalte++)
                    {
                        Vektor zielFeld = new Vektor(idSpalte, idReihe);
                        if(spielFeld.setzeStein(aktiverSpieler, zielFeld, false))
                        {
                            spielerKannSetzen = true;
                            break;
                        }
                    }
                }

                if(spielerKannSetzen)
                {
                    do
                    {
                        if(zeile > -1 && spalte > -1)
                        {
                            System.out.println("Der Stein konnte nich gesetzt werden, versuche es erneut!");
                        }

                        System.out.println("Setzende Zeile: ");
                        zeile = getNumberInput(0, spielFeld.getGroesse() - 1);

                        System.out.println("Setzende Spalte: ");
                        spalte = getNumberInput(0, spielFeld.getGroesse() - 1);

                        ziel.setX(spalte);
                        ziel.setY(zeile);
                    } while(!spielFeld.setzeStein(aktiverSpieler, ziel, true));
                }
                else
                {
                    System.out.println(aktiverSpieler.getName() + " kann nicht setzen. Sorry.");
                }


                if(aktiverSpieler == spieler1)
                    aktiverSpieler = spieler2;
                else if(aktiverSpieler == spieler2)
                    aktiverSpieler = spieler1;

                spielFeld.output();
                if(spielFeld.getFreieFelder() <= 0)
                {
                    freieFelderVerfuegbar = false;
                }
            }
            catch(GameRuleException gEx)
            {
                System.out.println("Die Spielregeln wurden verletzt!");
                System.out.println(gEx.getMessage());
            }
            catch(Exception ex)
            {
                System.out.println("Ein unerwarteter Fehler ist aufgetreten.");
                System.out.println(ex.getMessage());
            }
        }


    }

    private int getNumberInput(int min, int max)
    {
        while(true)
        {
            try
            {
                int number = in.nextInt();

                if(number >= min && number <= max)
                {
                    return number;
                }
                else
                {
                    System.out.println("Die eingegebene Zahl ist leider ungültig. Versuche es bitte erneut!");
                }

            }
            catch(Exception ex)
            {
                System.out.println("Die eingegebene Zahl ist leider ungültig. Versuche es bitte erneut!");
            }
        }
    }


}
