/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 12/06/2015
 * Time: 2:48 PM
 */
public class Player
{
    private MazeCell current;
    private MazeCell start;
    private MazeCell end;
    private int steps;

    public Player(Maze maze)
    {
        this.steps = 0;
        this.start = maze.getStart();
        this.end = maze.getEnd();
        this.current = start;
        maze.setPlayer(this);
    }

    public boolean move(Direction direction)
    {
        if (current.getDoor(direction))
        {
            MazeCell targetCell = current.getCell(direction);
            current = targetCell;
            steps++;
        }

        return current.getDoor(direction);
    }

    public MazeCell getCurrent()
    {
        return current;
    }

    public void setCurrent(MazeCell current)
    {
        this.current = current;
    }
}
