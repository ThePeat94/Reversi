package sample;

import Model.Feld;
import Model.Spieler;
import Model.Spielfeld;
import Utils.GameRuleException;
import Utils.Vektor;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;


public class Controller {
    public GridPane gpSpielFeld;
    public BorderPane dpMain;
    public Label lbStatusText;
    public Label lbSpieler1Name;
    public Label lbSpieler1Steine;
    public Label lbSpieler2Steine;
    public Label lbSpieler2Name;


    private Image feldImage = new Image("file:.\\Resources\\feld.png");
    private Image weissImage = new Image("file:.\\Resources\\weiß.png");
    private Image schwarzImage = new Image("file:.\\Resources\\schwarz.png");
    private Image verfuegbarImage = new Image("file:.\\Resources\\setzbar.png");

    private Spielfeld spielFeld;

    private Spieler spieler1;
    private Spieler spieler2;
    private Spieler aktiverSpieler;

    private double feldWeite;
    private double feldHoehe;

    private final int GRID_WIDTH = 720;
    private final int GRID_HEIGHT = 720;

    private final String ACTIVE_STYLE = "-fx-background-color: tomato";
    private final String INACTIVE_STYLE = "-fx-background-color: transparent";

    public void initialize()
    {
        startNewGame();
    }

    public void startNewGame()
    {
        int n = spielfeldGroesseInput();

        // 2. Eingabe der Spielernamen
        spieler1 = new Spieler('W');
        spieler2 = new Spieler('S');
        erfasseSpielerName(spieler1, 1);
        erfasseSpielerName(spieler2, 2);
        lbSpieler1Name.setText(spieler1.getName());
        lbSpieler2Name.setText(spieler2.getName());


        // 3. Erstellen des Feldes
        spielFeld = new Spielfeld(n);
        spielFeld.initialisiereFeld(spieler1, spieler2);

        // 4. Grafische Darstellung

        feldWeite = GRID_WIDTH/n;
        feldHoehe = GRID_HEIGHT/n;
        aktiverSpieler = spieler1;
        lbSpieler1Name.setStyle(ACTIVE_STYLE);
        renderSpielFeld();

    }

    private void erfasseSpielerName(Spieler zielSpieler, int spielerZahl)
    {
        String spielerName = "";

        do
        {
            Optional<String> nameResult = inputDialog("Spieler " + String.valueOf(spielerZahl) + " Name", "Geben Sie den Namen des " + String.valueOf(spielerZahl) + ". Spieler an", "Name: ");
            if(nameResult.isPresent())
                spielerName = nameResult.get();
        }while(spielerName.isEmpty());
        zielSpieler.setName(spielerName);
    }

    private int getNumberFromString(int min, int max, String toParse) throws Exception
    {
        int num = Integer.parseInt(toParse);
        if(num >= min && num <= max)
            return num;
        else
            throw new InputMismatchException(String.format("Die Zahl liegt nicht im zulässigen Bereich! (Min: %d, Max: %d)", min, max));
    }

    private Optional<String> inputDialog(String title, String headerText, String contentText)
    {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle(title);
        textInputDialog.setHeaderText(headerText);
        textInputDialog.setContentText(contentText);

        return textInputDialog.showAndWait();
    }

    private void aktiverSpielerSetztStein(Vektor gridZiel)
    {
        int feldZeile = (int) (gridZiel.getY()/feldHoehe);
        int feldSpalte = (int) (gridZiel.getX()/feldWeite);

        Vektor zielFeld = new Vektor(feldSpalte, feldZeile);

        lbStatusText.setText(String.format("Zielfeld: %s", zielFeld));

        try
        {
            if(spielFeld.setzeStein(aktiverSpieler, zielFeld, true))
            {
                if(aktiverSpieler == spieler1)
                {
                    if(ermittleVerfuegbareFelderFuerSpieler(spieler2).size() > 0)
                    {
                        aktiverSpieler = spieler2;
                        lbSpieler2Name.setStyle(ACTIVE_STYLE);
                        lbSpieler1Name.setStyle(INACTIVE_STYLE);
                    }
                }
                else if(aktiverSpieler == spieler2)
                {
                    if(ermittleVerfuegbareFelderFuerSpieler(spieler1).size() > 0)
                    {
                        aktiverSpieler = spieler1;
                        lbSpieler1Name.setStyle(ACTIVE_STYLE);
                        lbSpieler2Name.setStyle(INACTIVE_STYLE);
                    }
                }
                renderSpielFeld();
            }
        }
        catch(GameRuleException gEx)
        {
            lbStatusText.setText(gEx.getMessage());
        }

        if(spielFeld.getFreieFelder() == 0)
        {
            Spieler sieger = null;
            if(spieler1.getAnzSteine() > spieler2.getAnzSteine())
                sieger = spieler1;

            if(spieler2.getAnzSteine() > spieler1.getAnzSteine())
                sieger = spieler2;

            if(sieger != null)
            {
                displayMessage(Alert.AlertType.INFORMATION, "Spielende", "Alle Felder sind belegt", sieger.getName() + " hat gewonnen.");
            }
            else
            {
                displayMessage(Alert.AlertType.INFORMATION, "Spielende", "Alle Felder sind belegt", "Unentschieden, beide Spieler haben gleich viel Steine auf dem Feld!");
            }

        }
    }

    private ArrayList<Vektor> ermittleVerfuegbareFelderFuerSpieler(Spieler spieler)
    {
       ArrayList<Vektor> ergebnis = new ArrayList<Vektor>();
       Feld[][] felder = spielFeld.getSpielFeld();
       try
       {
           for(int i = 0; i < felder.length; i++)
           {
               Feld[] reihe = felder[i];
               for(int j = 0; j < reihe.length; j++)
               {
                   Feld feld = reihe[j];
                   Vektor zielFeld = new Vektor(j, i);
                   if(feld.getBesitzer() == null && spielFeld.setzeStein(spieler, zielFeld, false))
                   {
                       ergebnis.add(zielFeld);
                   }
               }
           }
       }
       catch(Exception ex)
       {

       }

       return ergebnis;
    }

    private void renderSpielFeld()
    {
        Feld[][] felder = spielFeld.getSpielFeld();
        gpSpielFeld= new GridPane();
        gpSpielFeld.setMinHeight(GRID_HEIGHT);
        gpSpielFeld.setMaxHeight(GRID_HEIGHT);

        gpSpielFeld.setMinWidth(GRID_WIDTH);
        gpSpielFeld.setMaxWidth(GRID_WIDTH);

        gpSpielFeld.setPrefWidth(GRID_WIDTH);
        gpSpielFeld.setPrefHeight(GRID_HEIGHT);

        ArrayList<Vektor> verfuegbareFelder = ermittleVerfuegbareFelderFuerSpieler(aktiverSpieler);

        for(int i = 0; i < felder.length; i++)
        {
            Feld[] reihe = felder[i];
            for(int j = 0; j < reihe.length; j++)
            {
                ImageView iv = new ImageView();
                iv.setImage(feldImage);
                iv.setFitHeight(feldHoehe);
                iv.setFitWidth(feldWeite);

                gpSpielFeld.add(iv, j, i);

                ImageView ivBesitzer = null;

                if(reihe[j].getBesitzer() == spieler1)
                {
                    ivBesitzer = new ImageView();
                    ivBesitzer.setImage(weissImage);
                }
                else if(reihe[j].getBesitzer() == spieler2)
                {
                    ivBesitzer = new ImageView();
                    ivBesitzer.setImage(schwarzImage);
                }

                if(ivBesitzer != null)
                {
                    ivBesitzer.setFitHeight(GRID_HEIGHT/spielFeld.getGroesse());
                    ivBesitzer.setFitWidth(GRID_WIDTH/spielFeld.getGroesse());
                    gpSpielFeld.add(ivBesitzer, j, i);
                }
            }
            lbSpieler1Steine.setText(String.valueOf(spieler1.getAnzSteine()));
            lbSpieler2Steine.setText(String.valueOf(spieler2.getAnzSteine()));
        }

        for(Vektor verfuegbar : verfuegbareFelder)
        {
            ImageView ivVerfuegbar = new ImageView();
            ivVerfuegbar.setImage(verfuegbarImage);
            ivVerfuegbar.setFitHeight(GRID_HEIGHT/spielFeld.getGroesse());
            ivVerfuegbar.setFitWidth(GRID_WIDTH/spielFeld.getGroesse());
            gpSpielFeld.add(ivVerfuegbar, verfuegbar.getX(), verfuegbar.getY());

        }

        dpMain.setCenter(gpSpielFeld);
        EventHandler<MouseEvent> clickHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Vektor gridZiel = new Vektor((int) event.getX(), (int) event.getY());
                aktiverSpielerSetztStein(gridZiel);
            }
        };
        gpSpielFeld.setOnMouseClicked(clickHandler);
    }

    private int spielfeldGroesseInput()
    {
        int n = 0;
        boolean erneutAuffordern = true;

        while(erneutAuffordern)
        {
            // 1. Eingabe der Groesse des Spielfelds
            Optional<String> result = inputDialog("Spielfeld initialisieren", "Geben Sie die Größe des Spielfeldes an. Mindestens 6, maximal 10!", "Größe: ");

            if(result.isPresent())
            {
                try
                {
                    n = getNumberFromString(6, 10, result.get());
                    erneutAuffordern = false;
                }
                catch(Exception ex)
                {
                    displayMessage(Alert.AlertType.ERROR, "Fehler aufgetrten", "Es ist ein unerwarteter Fehler aufgetrten!",
                            ex.getMessage());
                }
            }
            else
            {
                System.exit(0);
            }

        }

        return n;
    }

    private void displayMessage(Alert.AlertType alertType, String title, String headerText, String contentText)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
