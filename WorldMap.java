package csse2002.block.world;

import java.io.*;
import java.util.*;

/**
 * A class to store a world map.
 */
public class WorldMap {
    /*The starting tile*/
    private Tile startingTile;
    /*The start position*/
    private Position startPosition;
    /*The builder*/
    private Builder builder;
    /*The LinkedHashMap<Position, Tile>*/
    private SparseTileArray sparseTileArray;

    /**
     * Constructs a new block world map from a startingTile,position and builder
     * such that getBuilder() == builder,getStartPosition() == startPosition,and
     * getTiles() returns a list of tiles that arelinked to startingTile. Hint:
     * create a SparseTileArray as a member, and use the addLinkedTiles to
     * populate it.
     * @param startingTile the tile which the builder starts on
     * @param startPosition the position of the starting tile
     * @param builder the builder who will traverse the block world
     * @throws WorldMapInconsistentException if there are inconsistencies in the
     * positions of tiles
     */
    public WorldMap(csse2002.block.world.Tile startingTile,
                    Position startPosition,
                    csse2002.block.world.Builder builder)
            throws WorldMapInconsistentException {
        this.startingTile = startingTile;
        this.startPosition = startPosition;
        this.builder = builder;
        SparseTileArray sparseTileArray = new SparseTileArray();
        sparseTileArray.addLinkedTiles(this.startingTile,
                this.startPosition.getX(),
                this.startPosition.getY());
    }

    /**
     * Construct a block world map from the given filename.
     * @param filename the name to load the file from
     * @throws WorldMapFormatException if the file is incorrectly formatted
     * @throws WorldMapInconsistentException if the file is correctly formatted,
     * but has inconsistencies (such as overlapping tiles)
     */
    public WorldMap(java.lang.String filename)
            throws WorldMapFormatException,
            WorldMapInconsistentException {

        Map<String, Block> stringToBlock = new HashMap<>();
        this.sparseTileArray = new SparseTileArray();
        stringToBlock.put("wood", new WoodBlock());
        stringToBlock.put("grass", new GrassBlock());
        stringToBlock.put("soil", new SoilBlock());
        stringToBlock.put("stone", new StoneBlock());

        List<Block> startingInventory = new LinkedList<>();
        List<Block> startingBlocks = new LinkedList<>();
        List<Tile> linkedTiles = new LinkedList<>();

        FileReader fr = null;
        try {
            fr = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(fr);
            String line = "";
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (line == null) {
                throw new WorldMapFormatException();
        }

        try {
            int x = Integer.parseInt(line);
            //Read the line 2.
            line = reader.readLine();
            if (line == null) {
                throw new WorldMapFormatException();
            }

            int y = Integer.parseInt(line);
            this.startPosition = new Position(x, y);

            //Read the line 3.
            line = reader.readLine();
            if (line == null) {
                throw new WorldMapFormatException();
            }

            String builderName = line;

            //Read the line 4.
            line = reader.readLine();
            if (line == null) {
                throw new WorldMapFormatException();
            }

        try {
            for (int i = 0; i < line.split(",").length; i ++) {
                startingInventory.add
                        (stringToBlock.get(line.split(",")[i]));
            }
        } catch (Exception e) {}

        //Read the line 5.
        line = reader.readLine();

        //Read the line 6.
        line = reader.readLine();
        if (line == null) {
            throw new WorldMapFormatException();
        }

        int totalNumberOfTiles = Integer.parseInt
                (line.split(":")[1]);

        //Read all blocks, line 6.

        for (int i = 0; i < totalNumberOfTiles; i++) {
            line = reader.readLine();
            if (line == null) {
                throw new WorldMapFormatException();
            }

            String blocksString = line.split(" ")[1];

            for (int n = 0; n < blocksString.split(",").length;
                    n++) {
                startingBlocks.add
                        (stringToBlock.get
                                (blocksString.split(",")[n]));
            }

            linkedTiles.add(new Tile(startingBlocks));
            startingBlocks.clear();
        }

        //Read a blank line.
        line = reader.readLine();

        //Read exits first line.
        line = reader.readLine();

        //Read all exits.
        for (int i = 0; i < totalNumberOfTiles; i ++) {
            line = reader.readLine();
            if (line == null) {
                throw new WorldMapFormatException();
            }

            String exitsString = line.split(" ")[1];

            for(int n = 0; n < exitsString.split(",").length;
                    n++) {
                String exitAndTarget = exitsString.split(",")[n];
                String exit = exitAndTarget.split(":")[0];
                Tile targetTile = linkedTiles.get
                        (Integer.parseInt
                        (exitAndTarget.split(":")[1]));
                linkedTiles.get(i).addExit(exit,targetTile);
            }

        }
        this.startingTile = linkedTiles.get(0);
        this.sparseTileArray.addLinkedTiles(this.startingTile, x,
                y);
        this.builder = new Builder(builderName, this.startingTile,
                startingInventory);

        fr.close();
        reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TooHighException e) {
            e.printStackTrace();
        } catch (NoExitException e) {
            e.printStackTrace();
        } catch (InvalidBlockException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets the builder associated with this block world.
     * @return the builder object
     */
    public csse2002.block.world.Builder getBuilder() {
        return this.builder;
    }

    /**
     * Gets the starting position.
     * @return the starting position.
     */
    public Position getStartPosition() {
        return this.startPosition;
    }

  /**
   * Get a tile by position. Hint: call SparseTileArray.getTile()
   * @param position get the Tile at this position
   * @return the tile at that position
   */
    public csse2002.block.world.Tile getTile(Position position) {
        return this.sparseTileArray.getTile(position);
    }

    /**
     * Get a list of tiles in a breadth-first-search order
     * (see SparseTileArray.getTiles() for details).
     * @return a list of ordered tiles
     */
    public java.util.List<csse2002.block.world.Tile> getTiles() {
        return this.sparseTileArray.getTiles();
    }

    /**
     * Saves the given WorldMap to a file specified by the filename.
     * @param filename the filename to be written to
     * @throws java.io.IOException if the file cannot be opened or written to.
     */
    public void saveMap(java.lang.String filename)
            throws java.io.IOException {

        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            //Print line 1.
            pw.println(getStartPosition().getX());

            //Print line 2.
            pw.println(getStartPosition().getY());

            //Print line 3.
            pw.println(getBuilder().getName());

            //Print line 4.
            List<Block> inventory = getBuilder().getInventory();
            List<String> inventoryString = new ArrayList<String>();
            String inventoryBlockString = "";

            for (int i = 0; i < inventory.size(); i++) {
                inventoryString.add(inventory.get(i).getBlockType());
            }

            pw.println(inventoryBlockString.join(",",inventoryString));

            //Print line 5, a empty line.
            pw.println();

            //Print line 6.
            pw.println("total:" + getTiles().size());

            //Print line 7, getBlocks().
            int id1 = 0;

            for (Tile tile : getTiles()) {
                String titleBlocksString = "";
                List<String> getBlocksString = new ArrayList<String>();

                for(int i = 0; i < tile.getBlocks().size(); i++) {
                    getBlocksString.add
                            (getTiles().get(id1).getBlocks()
                                    .get(i).getBlockType());
                }

                pw.println("" + id1 + " " + titleBlocksString.join
                        (",",getBlocksString));
                id1++;
        }

        //Print a empty line;
        pw.println();

        //Print exits.
        int id2 = 0;

        for (Tile tile : getTiles()) {
            String line = "";
            List<String> exitsList = new ArrayList<String>();
            for (Map.Entry<String, Tile> entry : tile.getExits().entrySet()) {
                Tile tileToAdd = entry.getValue();
                String exitName = entry.getKey();
                exitsList.add(exitName + ":"
                        + getTiles().indexOf(tileToAdd));
                line = line.join(",",exitsList);
                }

            pw.println("" + id2 + " " + line);
            id2++;
            }

        pw.close();
        bw.close();
        fw.close();
    } catch (IOException e) {
        throw new IOException("file cannot be opened or written to");
    }

    }

}
