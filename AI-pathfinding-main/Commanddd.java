import java.util.LinkedList;
import java.util.Queue;

public class Commanddd {

    public boolean add_effy(Tiles[][] tileset, int y, int x, int tilesizey, int tilesizex) {//O(n^2)

        if (y < tilesizey) {
            add_effx(tileset, y, x, tilesizex);
            return add_effy(tileset, y + 1, x, tilesizey, tilesizex);
        } else {
            return true;
        }

    }

    public boolean add_effx(Tiles[][] tileset, int y, int x, int tilesizex) {
        if (x < tilesizex) {
            String desig = Integer.toString(x + 1) + Integer.toString(y + 1);
            tileset[y][x] = new Tiles(x, y, desig, tileset);
            return add_effx(tileset, y, x + 1, tilesizex);
        } else {
            return true;
        }
    }

    public boolean pathfind(LinkedList<Tiles> tile_path, Tiles[][] tileset, int targety, int targetx) {

        Queue<Tiles> queue = new LinkedList<>();
        Tiles startTile = tile_path.getFirst();
        queue.add(startTile);

        boolean[][] visited = new boolean[tileset.length][tileset[0].length];
        visited[startTile.returny()][startTile.returnx()] = true;

        Tiles[][] previousTile = new Tiles[tileset.length][tileset[0].length];

        while (!queue.isEmpty()) {//O()
            Tiles currentTile = queue.poll();
            if (currentTile.returnx() == targetx - 1 && currentTile.returny() == targety - 1) {
                // Reconstruct path
                Tiles pathTile = currentTile;
                while (pathTile != null) {
                    tile_path.addFirst(pathTile);
                    pathTile = previousTile[pathTile.returny()][pathTile.returnx()];
                }
                return true;
            }

            // Explore neighbors
            int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // left, right, up, down
            for (int[] dir : directions) {//O(n)
                int newX = currentTile.returnx() + dir[0];
                int newY = currentTile.returny() + dir[1];

                if (newX >= 0 && newX < tileset[0].length && newY >= 0 && newY < tileset.length
                        && !visited[newY][newX] && tileset[newY][newX].returnstate() != 9) { // not visited and not a wall
                    queue.add(tileset[newY][newX]);
                    visited[newY][newX] = true;
                    previousTile[newY][newX] = currentTile;
                }
            }
        }

        return false; // No path found
    }
}
