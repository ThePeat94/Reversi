package Model;

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

        Spieler aktiverSpieler = spieler1;
        while(true)
        {
            System.out.println(aktiverSpieler.getName() + " du bist am Zug.");
            int zeile = -1;
            int spalte = -1;
            do
            {
                if(zeile > -1 && spalte > -1)
                {
                    System.out.println("Der Stein konnte nich gesetzt werden, versuche es erneut!");
                }

                System.out.println("Setzende Zeile: ");
                zeile = getNumberInput(0, Integer.MAX_VALUE) - 1;

                System.out.println("Setzende Spalte: ");
                spalte = getNumberInput(0, Integer.MAX_VALUE) - 1;
            } while(!spielFeld.setzeStein(aktiverSpieler, zeile, spalte));



            if(aktiverSpieler == spieler1)
                aktiverSpieler = spieler2;
            else if(aktiverSpieler == spieler2)
                aktiverSpieler = spieler1;

            spielFeld.output();
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
