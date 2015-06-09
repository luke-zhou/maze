import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luke on 29/05/15.
 */
public class Maze
{
    private int height;
    private int width;
    private MazeCell[][] cells;
    private List<List<MazeCell>> connectedComponents;

    public Maze(int height, int width)
    {
        this.height = height;
        this.width = width;
        generate();
        connectedComponents = calculateConnectedComponent();
        //refactorMaze();
    }

    public void refactorMaze()
    {
        int x = (int) Math.random() * height;
        int y = (int) Math.random() * width;

        MazeCell cell = cells[x][y];

        List<MazeCell> connectedComponent = getConnectedComponent(cell, connectedComponents);
        if (!cell.getUpDoor())
        {
            MazeCell upCell = getUpCellFor(cell);
            if(upCell!=null&&!connectedComponent.contains(upCell))
            {
                List<MazeCell> otherConnectedComponent = getConnectedComponent(upCell, connectedComponents);
                upCell.setDownDoor(true);
                cell.setUpDoor(true);
                List<MazeCell> mergedResult = merge(connectedComponent, otherConnectedComponent);
                connectedComponents.remove(connectedComponent);
                connectedComponents.remove(otherConnectedComponent);
                connectedComponents.add(mergedResult);

            }
        }

    }

    public void show()
    {
        for (int i = 0; i < height * 2 - 1; i++)
        {
            for (int j = 0; j < width * 2 - 1; j++)
            {
                if (i % 2 == 0 && j % 2 == 0)
                {
                    printCell();
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

    private void printCell()
    {
        System.out.print('*');
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
                MazeCell cell = new MazeCell(coordinate);

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
        return (int) (Math.random() * 10) < 6;
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
            MazeCell upCell = getUpCellFor(cell);
            upConnectedComponents = getConnectedComponent(upCell, connectedComponents);
        }

        if (cell.getLeftDoor())
        {
            MazeCell leftCell = getLeftCellFor(cell);
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

    private MazeCell getUpCellFor(MazeCell cell)
    {
        int originalX = cell.getCoordinate().getX();
        int originalY = cell.getCoordinate().getY();
        return originalX > 0 ? cells[originalX - 1][originalY] : null;
    }

    private MazeCell getLeftCellFor(MazeCell cell)
    {
        int originalX = cell.getCoordinate().getX();
        int originalY = cell.getCoordinate().getY();
        return originalY > 0 ? cells[originalX][originalY - 1] : null;
    }

    public List<List<MazeCell>> getConnectedComponents()
    {
        return connectedComponents;
    }
}
