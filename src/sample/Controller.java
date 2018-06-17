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

    /**
     * Das darzustellende Spielfeld
     */
    public GridPane gpSpielFeld;

    /**
     * Das Panel für die Inhalte
     */
    public BorderPane dpMain;

    /**
     * Der darzustellende Statustext in der unteren Leiste
     */
    public Label lbStatusText;

    /**
     * Das Label für den Namen von Spieler1
     */
    public Label lbSpieler1Name;

    /**
     * Das Label für die Anzahl Steine von Spieler1
     */
    public Label lbSpieler1Steine;

    /**
     * Das Label für den Namen von Spieler2
     */
    public Label lbSpieler2Steine;

    /**
     * Das Label für die Anzahl Steine von Spieler2
     */
    public Label lbSpieler2Name;


    /**
     * Das darzustellende Bild für ein leeres Feld
     */
    private Image feldImage = new Image("file:.\\Resources\\feld.png");

    /**
     * Das darzustellende Bild für ein Feld mit weißen Stein
     */
    private Image weissImage = new Image("file:.\\Resources\\weiß.png");

    /**
     * Das darzustellende Bild für ein Feld mit schwarzen Stein
     */
    private Image schwarzImage = new Image("file:.\\Resources\\schwarz.png");

    /**
     * Das darzustellende Bild für ein Feld, wo der aktive Spieler ziehen kann
     */
    private Image verfuegbarImage = new Image("file:.\\Resources\\setzbar.png");

    /**
     * Die interne Repräsentation des Spielfelds
     */
    private Spielfeld spielFeld;

    /**
     * Der 1. Spieler
     */
    private Spieler spieler1;

    /**
     * Der 2. Spieler
     */
    private Spieler spieler2;

    /**
     * Der Spieler, der am Zug ist
     */
    private Spieler aktiverSpieler;

    /**
     * Die berechnete Weite in Pixel eines Felds ({@see GRID_WIDTH}/{@see Spielfeld.groesse})
     * Relevant für die Darstellung des hinterlegten Bildes
     */
    private double feldWeite;

    /**
     * Die berechnete Hähe in Pixel eines Felds ({@see GRID_HEIGHT}/{@see Spielfeld.groesse})
     * Relevant für die Darstellung des hinterlegten Bildes
     */
    private double feldHoehe;

    /**
     * Die Weite des Grids in Pixel
     */
    private final int GRID_WIDTH = 720;

    /**
     * Die Höhe des Grids in Pixel
     */
    private final int GRID_HEIGHT = 720;

    /**
     * Der css-Style-String für den aktiven Spieler
     */
    private final String ACTIVE_STYLE = "-fx-background-color: tomato";

    /**
     * Der css-Style-String für den inaktiven Spieler
     */
    private final String INACTIVE_STYLE = "-fx-background-color: transparent";

    /**
     * Wert, der angibt, ob das Spiel vorbei ist oder nicht
     */
    private boolean gameFinished = false;

    /**
     * Init-Funktion. Wird von FXML aufgerufen, um das hinterlegte Fenster zu initialisieren
     */
    public void initialize()
    {
        gpSpielFeld= new GridPane();
        gpSpielFeld.setMinHeight(GRID_HEIGHT);
        gpSpielFeld.setMaxHeight(GRID_HEIGHT);

        gpSpielFeld.setMinWidth(GRID_WIDTH);
        gpSpielFeld.setMaxWidth(GRID_WIDTH);

        gpSpielFeld.setPrefWidth(GRID_WIDTH);
        gpSpielFeld.setPrefHeight(GRID_HEIGHT);

        startNewGame();
    }

    /**
     * Startet ein neues Spiel. Dazu werden die Spielfeldgröße ({@see spielfeldGroesseInput}) und die Spielernamen erfasst({@see erfasseSpielerName}). Anschließend wird das GridPane initialisiert und gerendert ({@see renderSpielfeld}).
     */
    public void startNewGame()
    {
        // 1. Eingabe der Spielfeldgröße
        int n = spielfeldGroesseInput();
        gameFinished = false;

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
        lbSpieler2Name.setStyle(INACTIVE_STYLE);
        renderSpielFeld();

    }

    /**
     * Erfasst den Namen für einen Spieler
     * @param zielSpieler Der Spieler, für den der Name erfasst werden soll.
     * @param spielerZahl Die Spielerzahl (1 oder 2 im Falle von Othello)
     */
    private void erfasseSpielerName(Spieler zielSpieler, int spielerZahl)
    {
        String spielerName = "";
        // Solange abfragen, wie das Ergebnis nicht leer ist
        do
        {
            // Aufforderung zur Eingabe des Namen
            Optional<String> nameResult = inputDialog("Spieler " + String.valueOf(spielerZahl) + " Name", "Geben Sie den Namen des " + String.valueOf(spielerZahl) + ". Spieler an", "Name: ");

            // Wurde der Dialog bestätigt, ist isPresent() true, bei Abbrechen false
            if(nameResult.isPresent())
                spielerName = nameResult.get().trim();
            else
                System.exit(0);

        }while(spielerName.isEmpty());

        zielSpieler.setName(spielerName);
    }

    /**
     * Parset aus einem String eine Ganzzahl
     * @param min Der Mindestwert der Zahl
     * @param max Der Maximalwert der Zahl
     * @param toParse Der zu parsende String
     * @return Die geparste Zahl
     * @throws InputMismatchException Sollte die Zahl nicht im angegebenen Bereich zwischen min und max liegen
     * @throws NumberFormatException Sollte der zu parsende String ungültig sein
     */
    private int getNumberFromString(int min, int max, String toParse) throws NumberFormatException, InputMismatchException
    {
        int num = Integer.parseInt(toParse);
        if(num >= min && num <= max)
            return num;
        else
            throw new InputMismatchException(String.format("Die Zahl liegt nicht im zulässigen Bereich! (Min: %d, Max: %d)", min, max));
    }

    /**
     * Erstellt eine Eingabeaufforderung
     * @param title Der Titel der Eingabeaufforderung
     * @param headerText Der Kopftext
     * @param contentText Der Inhalt
     * @return Ein Ergebnis, das einen String enthalten KANN
     */
    private Optional<String> inputDialog(String title, String headerText, String contentText)
    {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle(title);
        textInputDialog.setHeaderText(headerText);
        textInputDialog.setContentText(contentText);

        return textInputDialog.showAndWait();
    }

    /**
     * Setzt einen Stein an den gegebenen Koordinaten
     * @param gridZiel Die rohen Feldkoordinaten, die bei Klick auf das Gridpane entstehen
     */
    private void aktiverSpielerSetztStein(Vektor gridZiel)
    {
        //Umwandlung der GridPane-Koordinaten in Spielfeld-Koordinaten
        int feldZeile = (int) (gridZiel.getY()/feldHoehe);
        int feldSpalte = (int) (gridZiel.getX()/feldWeite);

        // Das resultierende Zielfeld
        Vektor zielFeld = new Vektor(feldSpalte, feldZeile);

        lbStatusText.setText(String.format("Zielfeld: %s", zielFeld));

        try
        {
            // Setze den Stein
            if(spielFeld.setzeStein(aktiverSpieler, zielFeld, true))
            {

                // Spielerwechsel
                if(aktiverSpieler == spieler1)
                {
                    aktiverSpieler = spieler2;
                    lbSpieler2Name.setStyle(ACTIVE_STYLE);
                    lbSpieler1Name.setStyle(INACTIVE_STYLE);
                }
                else if(aktiverSpieler == spieler2)
                {
                    aktiverSpieler = spieler1;
                    lbSpieler1Name.setStyle(ACTIVE_STYLE);
                    lbSpieler2Name.setStyle(INACTIVE_STYLE);
                }
                renderSpielFeld();
            }
        }
        catch(GameRuleException gEx)
        {
            lbStatusText.setText(gEx.getMessage());
        }
    }

    /**
     * Überprüft, ob das Spiel vorbei ist (wenn ein Spieler nicht ziehen kann oder wenn das Spielfeld voll ist)
     */
    private void checkSpielStatus()
    {
        Spieler sieger = null;

        // Das Spielfeld ist voll
        if(spielFeld.getFreieFelder() == 0)
        {
            // Wer hat nun mehr Steine?
            if(spieler1.getAnzSteine() > spieler2.getAnzSteine())
                sieger = spieler1;
            if(spieler2.getAnzSteine() > spieler1.getAnzSteine())
                sieger = spieler2;

            // Infos für den darzustellenden Dialog
            Alert.AlertType at = Alert.AlertType.INFORMATION;
            String title = "Spielende";
            String headerText = "Alle Felder sind belegt.";
            String contentText;

            // Wenn es keinen Sieger gab => unentschieden
            if(sieger != null)
            {
               contentText = String.format("%s hat gewonnen.", sieger.getName());
            }
            else
            {
                contentText = "Unentschieden, beide Spieler haben gleich viel Steine auf dem Feld!";
            }
            displayMessage(at, title, headerText, contentText);
            gameFinished = true;

        }
        else
        {
            if(ermittleVerfuegbareFelderFuerSpieler(aktiverSpieler).size() == 0)
            {
                gameFinished = true;

                if(spieler1 == aktiverSpieler)
                    sieger = spieler2;

                if(spieler2 == aktiverSpieler)
                    sieger = spieler1;

                displayMessage(Alert.AlertType.INFORMATION, "Spielende", String.format("%s kann nicht mehr setzen.", aktiverSpieler.getName()), sieger.getName() + " hat gewonnen.");
            }
        }
    }

    /**
     * Ermittelt für den gegebenen Spieler alle Felder, an denen dieser setzen kann
     * @param spieler Der Spieler für den die Felder ermittelt werden sollen
     * @return Alle verfügbaren Felder in Form von Vektoren
     */
    private ArrayList<Vektor> ermittleVerfuegbareFelderFuerSpieler(Spieler spieler)
    {
       ArrayList<Vektor> ergebnis = new ArrayList<Vektor>();

       // Derzeitiges Spielfeld
       Feld[][] felder = spielFeld.getSpielFeld();
       try
       {
           // Reihen durchgehen
           for(int i = 0; i < felder.length; i++)
           {
               Feld[] reihe = felder[i];
               // Felder der Reihe
               for(int j = 0; j < reihe.length; j++)
               {
                   Feld feld = reihe[j];
                   Vektor zielFeld = new Vektor(j, i);
                   // Ist das Feld leer und kann hier gesetzt werden? (ersteres lediglich optimierungstechnisch)
                   if(feld.getBesitzer() == null && spielFeld.setzeStein(spieler, zielFeld, false))
                   {
                       ergebnis.add(zielFeld);
                   }
               }
           }
       }
       catch(GameRuleException gEx)
       {

       }

       return ergebnis;
    }

    /**
     * 
     */
    private void renderSpielFeld()
    {
        Feld[][] felder = spielFeld.getSpielFeld();


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
                if(!gameFinished)
                {
                    Vektor gridZiel = new Vektor((int) event.getX(), (int) event.getY());
                    aktiverSpielerSetztStein(gridZiel);
                    checkSpielStatus();
                }
                else
                {
                    lbStatusText.setText("Das Spiel ist vorbei. Es können keine weiteren Steine gesetzt werden.");
                }
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
            Optional<String> result = inputDialog("Spielfeld initialisieren", "Geben Sie die Größe des Spielfeldes an. Mindestens 6, maximal 10!", "Größe: ");

            if(result.isPresent())
            {
                try
                {
                    n = getNumberFromString(6, 10, result.get());
                    erneutAuffordern = false;
                }
                catch(NumberFormatException nEx)
                {
                    displayMessage(Alert.AlertType.ERROR, "Ungültige Zahl", "Die angegebene Zahl ist ungültig!",
                            nEx.getMessage());
                }
                catch(Exception ex)
                {
                    displayMessage(Alert.AlertType.ERROR, "Fehler aufgetreten", "Es ist ein unerwarteter Fehler aufgetreten!",
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
