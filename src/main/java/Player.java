/**
 * Created with IntelliJ IDEA.
 * User: lzhou
 * Date: 12/06/2015
 * Time: 2:48 PM
 */
public class Player
{
    private MazeCell currentCell;

    public MazeCell getCurrentCell()
    {
        return currentCell;
    }

    public void setCurrentCell(MazeCell currentCell)
    {
        this.currentCell = currentCell;
    }
}
