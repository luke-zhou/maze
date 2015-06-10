import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luke on 29/05/15.
 */
public class MazeCell
{
    private Point coordinate;
    private Map<Direction, Boolean> doors;
    private int altitude;

    public MazeCell(Point coordinate)
    {
        this.coordinate = coordinate;
        doors = new HashMap<Direction, Boolean>();
        altitude =0;
    }

    public void increaseAltitude()
    {
        altitude++;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MazeCell mazeCell = (MazeCell) o;

        if (coordinate != null ? !coordinate.equals(mazeCell.coordinate) : mazeCell.coordinate != null) return false;
        if (doors != null ? !doors.equals(mazeCell.doors) : mazeCell.doors != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = coordinate != null ? coordinate.hashCode() : 0;
        result = 31 * result + (doors != null ? doors.hashCode() : 0);
        return result;
    }

    public Point getCoordinate()
    {
        return coordinate;
    }

    public Map<Direction, Boolean> getDoors()
    {
        return doors;
    }

    public Boolean getDoor(Direction direction)
    {
        return doors.get(direction);
    }

    public void setDoor(Direction direction, Boolean isOpen)
    {
        doors.put(direction, isOpen);
    }

    public Boolean getLeftDoor()
    {
        return getDoor(Direction.LEFT);
    }

    public Boolean getRightDoor()
    {
        return getDoor(Direction.RIGHT);
    }

    public Boolean getUpDoor()
    {
        return getDoor(Direction.UP);
    }

    public Boolean getDownDoor()
    {
        return getDoor(Direction.DOWN);
    }

    public void setLeftDoor(Boolean isOpen)
    {
        setDoor(Direction.LEFT, isOpen);
    }

    public void setRightDoor(Boolean isOpen)
    {
        setDoor(Direction.RIGHT, isOpen);
    }

    public void setUpDoor(Boolean isOpen)
    {
        setDoor(Direction.UP, isOpen);
    }

    public void setDownDoor(Boolean isOpen)
    {
        setDoor(Direction.DOWN, isOpen);
    }

    public int getAltitude()
    {
        return altitude;
    }

    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }

    @Override
    public String toString()
    {
        return "MazeCell{" +
                "coordinate=" + coordinate +
                ", doors=" + doors +
                ", altitude=" + altitude +
                '}';
    }
}
