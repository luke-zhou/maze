import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Luke on 29/05/15.
 */
public class Maze
{
    private static double DEFAULT_PATH_DENSITY = 0.5D;
    private static double DEFAULT_ALTITUDE_DENSITY = 0.5D;

    private int height;
    private int width;
    private double pathDensity;
    private double altitudeDensity;
    private MazeCell[][] cells;
    private List<List<MazeCell>> connectedComponents;
    private MazeCell start;
    private MazeCell end;
    private Player player;

    public Maze(int height, int width, double pathDensity, double altitudeDensity)
    {
        this.height = height;
        this.width = width;
        this.pathDensity = pathDensity;
        this.altitudeDensity = altitudeDensity;

        generate();
        connectedComponents = calculateConnectedComponent();
        refactorMazeConnection();
        refactorMazeAltitude();

        start = cells[0][0];
        end = cells[height-1][width-1];

    }

    public Maze(int height, int width)
    {
        this(height,width,DEFAULT_PATH_DENSITY, DEFAULT_ALTITUDE_DENSITY);
    }

    private void refactorMazeAltitude()
    {
        for (int i = 0; i < height * width * altitudeDensity; i++)
        {
            int x = (int) (Math.random() * height);
            int y = (int) (Math.random() * width);

            cellAltitudeIncrease(cells[x][y]);
        }
    }

    private void cellAltitudeIncrease(MazeCell cell)
    {
        cell.increaseAltitude();

        for (Direction direction : Direction.values())
        {
            MazeCell nextCell = cell.getCell(direction);
            if (nextCell != null)
            {
                int diff = cell.getAltitude() - nextCell.getAltitude();
                if (diff == 2) cellAltitudeIncrease(nextCell);
            }
        }
    }

    public void refactorMazeConnection()
    {
        while (connectedComponents.size() > 1)
        {
            Collections.sort(connectedComponents, connectedComponentsOrder);
            List<MazeCell> connectedComponent = connectedComponents.get(0);

            int randomIndex = (int) (Math.random() * connectedComponent.size());

            MazeCell cell = connectedComponent.get(randomIndex);

            Direction direction = Direction.getRandomDirection();
            openDoorToCell(cell, connectedComponent, direction);
        }

    }

    private Boolean openDoorToCell(MazeCell cell, List<MazeCell> connectedComponent, Direction direction)
    {
        if (!cell.getDoor(direction))
        {
            MazeCell targetCell = cell.getCell(direction);
            if (targetCell != null && !connectedComponent.contains(targetCell))
            {
                List<MazeCell> otherConnectedComponent = getConnectedComponent(targetCell, connectedComponents);
                targetCell.setDoor(direction.getOppositeDirection(), true);
                cell.setDoor(direction, true);
                List<MazeCell> mergedResult = merge(connectedComponent, otherConnectedComponent);
                connectedComponents.remove(connectedComponent);
                connectedComponents.remove(otherConnectedComponent);
                connectedComponents.add(mergedResult);

                return true;
            }
        }

        return false;
    }

    public void show()
    {
        for (int i = 0; i < height * 2 - 1; i++)
        {
            for (int j = 0; j < width * 2 - 1; j++)
            {
                if (i % 2 == 0 && j % 2 == 0)
                {
                    printCell(cells[i / 2][j / 2]);
                }
                else if (i % 2 == 0 && j % 2 == 1)
                {
                    printHDoor(cells[i / 2][j / 2].getRightDoor());
                }
                else if (i % 2 == 1 && j % 2 == 1)
                {
                    printSpace();
                }
                else if (i % 2 == 1 && j % 2 == 0)
                {
                    printVDoor(cells[i / 2][j / 2].getDownDoor());
                }
            }
            System.out.println();
        }
    }

    private void printCell(MazeCell cell)
    {
        String printOutChar = (player!=null&&player.getCurrent().equals(cell)? "*":String.valueOf(cell.getAltitude()));
        System.out.print(printOutChar);
    }

    private void printSpace()
    {
        System.out.print(' ');
    }

    private void printHDoor(Boolean isOpen)
    {
        String door = isOpen ? "-" : " ";
        System.out.print(door);
    }

    private void printVDoor(Boolean isOpen)
    {
        char door = isOpen ? '|' : ' ';
        System.out.print(door);
    }

    private void generate()
    {
        cells = new MazeCell[height][width];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Point coordinate = new Point(i, j);
                MazeCell cell = new MazeCell(coordinate, this);

                Boolean leftDoor = j == 0 ? false : cells[i][j - 1].getRightDoor();
                cell.setLeftDoor(leftDoor);

                Boolean rightDoor = j == width - 1 ? false : randomBoolean();
                cell.setRightDoor(rightDoor);

                Boolean upDoor = i == 0 ? false : cells[i - 1][j].getDownDoor();
                cell.setUpDoor(upDoor);

                Boolean downDoor = i == height - 1 ? false : randomBoolean();
                cell.setDownDoor(downDoor);

                cells[i][j] = cell;
            }
        }
    }

    private Boolean randomBoolean()
    {
        return Math.random() < pathDensity;
    }

    private List<List<MazeCell>> calculateConnectedComponent()
    {
        List<List<MazeCell>> connectedComponents = new ArrayList<List<MazeCell>>();

        for (MazeCell[] row : cells)
        {
            for (MazeCell cell : row)
            {
                resolveConnectedComponents(cell, connectedComponents);
            }
        }

        return connectedComponents;
    }

    private void resolveConnectedComponents(MazeCell cell, List<List<MazeCell>> connectedComponents)
    {
        List<MazeCell> upConnectedComponents = null;
        List<MazeCell> leftConnectedComponents = null;

        if (cell.getUpDoor())
        {
            MazeCell upCell = cell.getCell(Direction.UP);
            upConnectedComponents = getConnectedComponent(upCell, connectedComponents);
        }

        if (cell.getLeftDoor())
        {
            MazeCell leftCell = cell.getCell(Direction.LEFT);
            leftConnectedComponents = getConnectedComponent(leftCell, connectedComponents);
        }

        if (upConnectedComponents == null && leftConnectedComponents == null)
        {
            List<MazeCell> connectedComponent = new ArrayList<MazeCell>();
            connectedComponent.add(cell);
            connectedComponents.add(connectedComponent);
        }
        else if (upConnectedComponents == null)
        {
            leftConnectedComponents.add(cell);
        }
        else if (leftConnectedComponents == null)
        {
            upConnectedComponents.add(cell);
        }
        else if (leftConnectedComponents.equals(upConnectedComponents))
        {
            upConnectedComponents.add(cell);
        }
        else if (!leftConnectedComponents.equals(upConnectedComponents))
        {
            List<MazeCell> mergedConnectedComponents = merge(upConnectedComponents, leftConnectedComponents);
            mergedConnectedComponents.add(cell);
            connectedComponents.remove(upConnectedComponents);
            connectedComponents.remove(leftConnectedComponents);
            connectedComponents.add(mergedConnectedComponents);
        }
        else
        {
            System.out.println("Something is wrong!!!");
        }
    }



    private List<MazeCell> merge(List<MazeCell> upConnectedComponents, List<MazeCell> leftConnectedComponents)
    {
        List<MazeCell> mergeResult = new ArrayList<MazeCell>();
        mergeResult.addAll(upConnectedComponents);
        mergeResult.addAll(leftConnectedComponents);

        return mergeResult;
    }

    private List<MazeCell> getConnectedComponent(MazeCell cell, List<List<MazeCell>> connectedComponents)
    {
        for (List<MazeCell> connectedComponent : connectedComponents)
        {
            if (connectedComponent.contains(cell)) return connectedComponent;
        }

        return null;
    }

    public MazeCell getCell(int x, int y)
    {
       return cells[x][y];
    }

    public List<List<MazeCell>> getConnectedComponents()
    {
        return connectedComponents;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public MazeCell getStart()
    {
        return start;
    }

    public MazeCell getEnd()
    {
        return end;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    private static final Comparator<List<MazeCell>> connectedComponentsOrder =
            new Comparator<List<MazeCell>>()
            {
                public int compare(List<MazeCell> o1, List<MazeCell> o2)
                {
                    return o1.size() > o2.size() ? 1 : (o1.size() < o2.size() ? -1 : 0);
                }
            };
}
