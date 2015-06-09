import java.util.List;

/**
 * Created by Luke on 29/05/15.
 */
public class Main
{
    public static void main(String[] args)
    {
        Maze maze = new Maze(20,40);

        maze.show();

//        System.out.println(maze.getConnectedComponents().size());
//
//        for (List<MazeCell> group : maze.getConnectedComponents())
//        {
//           System.out.println(group);
//
//        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        maze.refactorMaze();

        maze.show();

//        System.out.println(maze.getConnectedComponents().size());
//
//        for (List<MazeCell> group : maze.getConnectedComponents())
//        {
//            System.out.println(group);
//
//        }
    }

}

