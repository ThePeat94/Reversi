package Model;

public class Spieler {
    private String name;
    private char value;
    private int anzSteine;

    public Spieler(char value)
    {
        this.value = value;
    }

    public char getValue()
    {
        return value;
    }

}
