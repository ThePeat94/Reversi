package sample;

import Model.GameManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application {

    Scanner in = new Scanner(System.in);

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Konsolenprogramm
        //GameManager gm = new GameManager();
        //System.out.println("Geben sie die Groesse des Spielfelds an!");
        //int n = getNumberInput(6, 10);
        //gm.startNewGame(n);


        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Reversi");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(800);
        primaryStage.setMaxHeight(900);
        primaryStage.setMaxWidth(800);
        primaryStage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}
