package sample;

import Model.Feld;
import Model.Spieler;
import Model.Spielfeld;
import Utils.GameRuleException;
import Utils.Vektor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
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


    Image feldImage = new Image("file:.\\Resources\\feld.png");
    Image weissImage = new Image("file:.\\Resources\\weiß.png");
    Image schwarzImage = new Image("file:.\\Resources\\schwarz.png");
    Image verfuegbarImage = new Image("file:.\\Resources\\setzbar.png");

    Spielfeld spielFeld;

    Spieler spieler1;
    Spieler spieler2;
    Spieler aktiverSpieler;

    double feldWeite;
    double feldHoehe;

    final int GRID_WIDTH = 720;
    final int GRID_HEIGHT = 720;

    public void initialize()
    {
        startNewGame();
    }

    public void startNewGame()
    {
        int n = 0;
        do {
            // 1. Eingabe der Groesse des Spielfelds
            Optional<String> result = inputDialog("Spielfeld initialisieren", "Geben Sie die Größe des Spielfeldes an. Mindestens 6, maximal 10!", "Größe: ");
            if(result.isPresent())
            {
                try
                {
                    n = getNumberFromString(6, 10, result.get());
                }
                catch(Exception ex)
                {

                }
            }

        } while(n == 0);

        // 2. Eingabe der Spielernamen
        String spieler1Name = "";

        do
        {
            Optional<String> nameResult = inputDialog("Spieler 1 Name", "Geben Sie den Namen des 1. Spieler an", "Name: ");
            if(nameResult.isPresent())
                spieler1Name = nameResult.get();
        }while(spieler1Name.isEmpty());

        spieler1 = new Spieler('S');
        spieler1.setName(spieler1Name);

        String spieler2Name = "";

        do
        {
            Optional<String> nameResult = inputDialog("Spieler 2 Name", "Geben Sie den Namen des 2. Spieler an", "Name: ");
            if(nameResult.isPresent())
                spieler2Name = nameResult.get();
        }while(spieler2Name.isEmpty());

        spieler2 = new Spieler('W');
        spieler2.setName(spieler1Name);

        // 3. Erstellen des Feldes
        spielFeld = new Spielfeld(n);
        spielFeld.initialisiereFeld(spieler1, spieler2);

        // 4. Grafische Darstellung

        feldWeite = GRID_WIDTH/n;
        feldHoehe = GRID_HEIGHT/n;
        aktiverSpieler = spieler1;
        renderSpielFeld();

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
                    aktiverSpieler = spieler2;
                else if(aktiverSpieler == spieler2)
                    aktiverSpieler = spieler1;
                renderSpielFeld();
            }
        }
        catch(GameRuleException gEx)
        {

        }
    }

    private ArrayList<Vektor> ermittleVerfuegbareFelderFuerSpieler()
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
                   if(feld.getBesitzer() == null && spielFeld.setzeStein(aktiverSpieler, zielFeld, false))
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

        ArrayList<Vektor> verfuegbareFelder = ermittleVerfuegbareFelderFuerSpieler();

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

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
