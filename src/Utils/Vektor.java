package Utils;

public class Vektor {

    private int x;

    private int y;

    public Vektor()
    {
        x = 0;
        y = 0;
    }

    public Vektor(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return String.format("(%d/%d)", x, y);
    }

    public Vektor distanzVektorZu(Vektor ziel)
    {
        Vektor distanzVektor = new Vektor();
        distanzVektor.setX(ziel.x - this.x);
        distanzVektor.setY(ziel.y - this.y);

        return distanzVektor;
    }
}
