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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAnzSteine() {
        return anzSteine;
    }

    public void setAnzSteine(int anzSteine) {
        this.anzSteine = anzSteine;
    }
}
