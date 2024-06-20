import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Runda extends JFrame {
    static int tilesize_x;

    static int tilesize_y;
    static int targetx;
    static int targety;
    static int excX;
    static int excY;
    static LinkedList<Tiles> tile_path = new LinkedList<>();
    static Commanddd meth = new Commanddd();
    static Tiles[][] tileset;

    public Runda() {
        setTitle("Pathfinding Visualization");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GridPanel());
    }

    public static void main(String[] args) {
        // Prompt user for grid size, start and target coordinates
        try {
            tilesize_x = Integer.parseInt(JOptionPane.showInputDialog("Enter the grid width (x): "));
            tilesize_y = Integer.parseInt(JOptionPane.showInputDialog("Enter the grid height (y): "));
            excX = Integer.parseInt(JOptionPane.showInputDialog("Enter the start X coordinate: "));
            excY = Integer.parseInt(JOptionPane.showInputDialog("Enter the start Y coordinate: "));
            targetx = Integer.parseInt(JOptionPane.showInputDialog("Enter the target X coordinate: "));
            targety = Integer.parseInt(JOptionPane.showInputDialog("Enter the target Y coordinate: "));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter integers only.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long beforeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc();
        long start = System.nanoTime();

        tileset = new Tiles[tilesize_y][tilesize_x];
        // Initialize tile set
        meth.add_effy(tileset, 0, 0, tilesize_y, tilesize_x);

        // Set walls in the grid
        setWalls();

        // Set the start tile
        Tiles exc = tileset[excY][excX];
        exc.setstate(3);  // Assuming 3 represents the starting state
        tile_path.add(exc);

        // Pathfinding to the target
        boolean pathFound = meth.pathfind(tile_path, tileset, targety, targetx);

        // Print the path if found
        if (pathFound) {
            System.out.println("Path found:");
            for (Tiles tilish : tile_path) {
                if (tilish.returnx() == excX && tilish.returny() == excY) {
                    System.out.println(tilish.returndesignation() + " start");
                } else if (tilish.returnx() == targetx - 1 && tilish.returny() == targety - 1) {
                    System.out.println(tilish.returndesignation() + " target");
                } else {
                    System.out.println(tilish.returndesignation());
                }
            }
        } else {
            System.out.println("No path found.");
        }
        long dur= (System.nanoTime()-start);
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        //if the result minus gget the absolute number
        System.out.println(Math.abs(endMemory- beforeMemory)+" bytes");

        System.out.println(dur +" nano secs");
        // Display the visualization
        SwingUtilities.invokeLater(() -> {
            Runda runda = new Runda();
            runda.setVisible(true);
        });
    }

    private static void setWalls() {
        // Prompt user to specify wall positions
        while (true) {
            String inputX = JOptionPane.showInputDialog("Enter wall X coordinate (or type 'done' to finish): ");
            if (inputX.equalsIgnoreCase("done")) {
                break;
            }

            try {
                int x = Integer.parseInt(inputX);
                if (x < 0 || x >= tilesize_x) {
                    JOptionPane.showMessageDialog(null, "Invalid X coordinate. Please enter a valid coordinate.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                String inputY = JOptionPane.showInputDialog("Enter wall Y coordinate (or type 'done' to finish): ");
                if (inputY.equalsIgnoreCase("done")) {
                    break;
                }

                try {
                    int y = Integer.parseInt(inputY);
                    if (y < 0 || y >= tilesize_y) {
                        JOptionPane.showMessageDialog(null, "Invalid Y coordinate. Please enter a valid coordinate.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Set the tile at (y, x) as a wall
                    tileset[y][x].setWall();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter integers only for Y coordinate.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter integers only for X coordinate.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    class GridPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cellWidth = getWidth() / tilesize_x;
            int cellHeight = getHeight() / tilesize_y;

            for (int y = 0; y < tilesize_y; y++) {
                for (int x = 0; x < tilesize_x; x++) {
                    Tiles tile = tileset[y][x];
                    if (tile.returnstate() == 9) {
                        g.setColor(Color.BLACK);
                    } else if (tile_path.contains(tile)) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                    g.setColor(Color.GRAY);
                    g.drawRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                }
            }
        }
    }
}
