package Utils;

/**
 * Stellt eine Verletzung der Spielregeln dar
 */
public class GameRuleException extends Exception
{
    public GameRuleException(String message)
    {
        super(message);
    }
}
