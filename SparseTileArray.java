package csse2002.block.world;

import java.util.*;

/**
 * A sparse representation of tiles in an Array. Contains Tiless stored with
 * an associated Position(x, y) in a map.
 */
public class SparseTileArray {
    //Tiless stored with an associated Position (x, y) in a map.
    private Map<Position, Tile> sparseTileArray;

    /**
     * Constructor for a SparseTileArray.
     */
    public SparseTileArray() {
        sparseTileArray = new LinkedHashMap<Position, Tile>();
    }

    /**
     * Get the tile at position at (x, y), given by position.getX()
     * and position.getY(). Return null if there is no tile at (x, y).
     * @param position the tile position
     * @return the tile at (x, y) or null if no such tile exists.
     */
    public csse2002.block.world.Tile getTile(Position position){
        if (sparseTileArray.get(position) == null){
            return null;
        } else {
            return sparseTileArray.get(position);
        }

    }

    /**
     * Get a set of ordered tiles from SparseTileArray
     * in breadth-first-search order.
     * @return
     */
    public java.util.List<csse2002.block.world.Tile> getTiles(){
        List<Tile> linkedTiles = new LinkedList<Tile>(sparseTileArray.values());
        return linkedTiles;
    }

    /**
     * Add a set of tiles to the sparse tilemap.
     * @param tile a tile in the sparseTileArray.
     * @return the position of this tile.
     */
    private Position getPosition(Tile tile) {
        for (Map.Entry<Position, Tile> entry : sparseTileArray.entrySet()) {
            if (entry.getValue() == tile) {
                return entry.getKey();
            }

        }

        return null;
    }

    /**
     * Add a set of tiles to the sparse tilemap.
     * @param startingTile the starting point in adding the linked tiles.
     * All added tiles must have a path (via multiple exits) to this tile.
     * @param startingX the x coordinate of startingTile in the array
     * @param startingY the y coordinate of startingTile in the array
     * @throws WorldMapInconsistentException if the tiles in the set are not
     * Geometrically consistent
     */
    public void addLinkedTiles(Tile startingTile,
                               int startingX,
                               int startingY)
            throws WorldMapInconsistentException{
        //Create a Queue and Set for BFS.
        Queue<Tile> nodesToVisit = new LinkedList<>();
        Set<Tile> alreadyVisited = new LinkedHashSet<>();
        //List to check exit names.
        List<String> nesw = Arrays.asList("north", "east" ,"south", "west");

        sparseTileArray.put(new Position(startingX, startingY), startingTile);
        nodesToVisit.add(startingTile);

        while (nodesToVisit.size() != 0) {
            Tile tile = nodesToVisit.remove();

            if (!alreadyVisited.contains(tile)) {
                alreadyVisited.add(tile);

        // process tile

            for (String exits : nesw) {

                for (Map.Entry<String, Tile> entry : tile.getExits().entrySet())
                {

                    Tile tileToAdd = entry.getValue();
                    if (!alreadyVisited.contains(tileToAdd)) {

                        String exitName = entry.getKey();

                        if (exitName.equals(exits) && exitName.equals(
                               "north")) {
                            Position positionToAdd =
                                    new Position(getPosition(tile).getX(),
                                        getPosition(tile).getY() - 1);
                            if (sparseTileArray.containsKey(positionToAdd)
                                    || sparseTileArray.containsValue(tileToAdd))
                            {
                              throw new WorldMapInconsistentException();
                            }
                            sparseTileArray.put(positionToAdd, tileToAdd);
                            nodesToVisit.add(tileToAdd);
                        } else if (exitName.equals(exits)
                                && exitName.equals("east")) {
                            Position positionToAdd =
                                new Position(getPosition(tile).getX()
                                        + 1, getPosition(tile).getY());
                            if (sparseTileArray.containsKey(positionToAdd)
                                    || sparseTileArray.containsValue(tileToAdd))
                            {
                              throw new WorldMapInconsistentException();
                            }
                            sparseTileArray.put(positionToAdd, tileToAdd);
                            nodesToVisit.add(tileToAdd);
                        } else if (exitName.equals(exits)
                                && exitName.equals("south")) {
                            Position positionToAdd =
                                    new Position(getPosition(tile).getX(),
                                    getPosition(tile).getY() + 1);
                            if (sparseTileArray.containsKey(positionToAdd)
                                    || sparseTileArray.containsValue(tileToAdd))
                            {
                                throw new WorldMapInconsistentException();
                            }
                            sparseTileArray.put(positionToAdd, tileToAdd);
                            nodesToVisit.add(tileToAdd);
                        } else if (exitName.equals(exits)
                                && exitName.equals("west")) {
                            Position
                                    positionToAdd = new Position
                                    (getPosition(tile).getX() - 1,
                                    getPosition(tile).getY());
                            if (sparseTileArray.containsKey(positionToAdd)
                                    || sparseTileArray.containsValue(tileToAdd))
                            {
                                throw new WorldMapInconsistentException();
                            }
                            sparseTileArray.put(positionToAdd, tileToAdd);
                            nodesToVisit.add(tileToAdd);
                            }

                        }
                    }
                }
            }
        }
    }
}










