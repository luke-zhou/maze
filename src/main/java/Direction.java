import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luke on 29/05/15.
 */
public enum Direction
{
    UP(0), DOWN(1), LEFT(2), RIGHT(3);

    private final int value;
    private static final Map<Integer, Direction> map = new HashMap<Integer, Direction>();
    private Direction(int value) {
        this.value = value;
    }

    static {
        for (Direction d : Direction.values()) {
            map.put(d.value, d);
        }
    }


    public static Direction getRandomDirection()
    {
        int random = (int) (Math.random() * 4);
        return map.get(random);
    }

    public Direction getOppositeDirection()
    {
        switch (this)
        {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            default:
                return LEFT;
        }

    }
}
