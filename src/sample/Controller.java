package sample;

import Model.Feld;
import Model.Spieler;
import Model.Spielfeld;
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
import java.util.InputMismatchException;
import java.util.Optional;


public class Controller {
    public GridPane gpSpielFeld;
    public BorderPane dpMain;


    Image feldImage = new Image("file:.\\Resources\\feld.png");
    Image weissImage = new Image("file:.\\Resources\\weiß.png");
    Image schwarzImage = new Image("file:.\\Resources\\schwarz.png");

    Spielfeld spielFeld;

    Spieler spieler1;
    Spieler spieler2;

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
        Feld[][] felder = spielFeld.getSpielFeld();
        GridPane newGp = new GridPane();
        newGp.minHeightProperty().setValue(695);
        newGp.maxHeightProperty().setValue(695);

        newGp.minWidthProperty().setValue(720);
        newGp.maxWidthProperty().setValue(720);

        gpSpielFeld.setHgap(0);
        gpSpielFeld.setVgap(0);
        for(int i = 0; i < felder.length; i++)
        {
            Feld[] reihe = felder[i];
            for(int j = 0; j < reihe.length; j++)
            {
                ImageView iv = new ImageView();
                iv.setImage(feldImage);
                iv.setFitHeight(720/n);
                iv.setFitWidth(695/n);
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
                    ivBesitzer.setFitHeight(720/n);
                    ivBesitzer.setFitWidth(695/n);
                    gpSpielFeld.add(ivBesitzer, j, i);
                }
            }
        }
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

    @FXML
    private void mouseEntered(MouseEvent e) {
        e.getX();
        e.getY();
    }
}
