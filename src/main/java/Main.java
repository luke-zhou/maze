import java.util.Date;
import java.util.List;

/**
 * Created by Luke on 29/05/15.
 */
public class Main
{
    public static void main(String[] args)
    {
        //System.out.println("start:" + new Date());
        Maze maze = new Maze(20, 40);
        //System.out.println("end:" + new Date());
        maze.show();

        Player player = new Player(maze);

        maze.show();

        while(player.move(Direction.RIGHT))
        {

        }

        maze.show();
    }

}

