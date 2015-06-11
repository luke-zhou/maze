import java.util.Date;
import java.util.List;

/**
 * Created by Luke on 29/05/15.
 */
public class Main
{
    public static void main(String[] args)
    {
        System.out.println("start:"+new Date());
        Maze maze = new Maze(25, 50);
        System.out.println("end:"+new Date());
        maze.show();
    }

}

